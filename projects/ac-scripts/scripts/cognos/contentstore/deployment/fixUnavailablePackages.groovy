import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Traverses Public Folders and fixes broken links to packages'

	parameters {
		targetPath { description = 'Limits the scope of changes' }
		objectTypes
		dryRun { optional }
	}
}

dsl.cognos.play {
	eachObject( searchPath: buildSearchPath( targetPath: script.parameters.targetPath, objectTypes: script.parameters.objectTypes ), properties: [PropEnum.metadataModelPackage]) { object ->
		if ( !object.metadataModelPackage.value ) {
			def spec = find( searchPath: object.searchPath.value, properties: [PropEnum.specification]).specification.value
			def modelPath = extractModelPath ( spec )

			assert modelPath, 'model path not found'

			def pckg = modelPath.elements.find{ it.type == 'package' }

			assert pckg, 'No package found in object model path'

			def existingPackages = findAll( searchPath: "/content//${pckg}" )

			if ( existingPackages.size() == 1 ) {
				if ( ! script.parameters.dryRun ) {
					object.metadataModelPackage.value = existingPackages
					update( objects: [object])
				}
				log.info "invalid package for ${object.searchPath.value} was changed to ${existingPackages[ 0 ].searchPath.value}"
			} else {
				log.warn "There must one package matching ${pckg}. Object with missing package is ${object.searchPath.value}"
			}
		}
	}
}

return

private extractModelPath ( spec ) {
	def model
	def xml = new XmlSlurper().parseText( spec )
	def objectType = xml.name()
	switch ( objectType ) {
		case 'CRQReport' :
			model = xml.@srcModel.text()
			break

		case 'report' :
			model = xml.modelPath.text()
			break

		default :
			assert false, 'model path not found'
	}

	model
}
