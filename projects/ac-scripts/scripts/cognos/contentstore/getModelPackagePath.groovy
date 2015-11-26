import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Extracts model information from object specification. Can be used to figure out what the current model should be in case if package information is missing.'

	parameters {
		searchPath 
	}
}

def modelPackagePath 

dsl.cognos.play {
	def spec = find( searchPath: script.parameters.searchPath, properties: [PropEnum.specification]).specification.value
	modelPackagePath = extractModelPackagePath ( spec )

}

return modelPackagePath

private extractModelPackagePath ( spec ) {
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
			model = ''
	}

	model
}
