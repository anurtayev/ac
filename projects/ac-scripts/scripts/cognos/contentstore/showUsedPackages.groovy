import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	parameters {
		searchPath { description = 'Limits the scope of query' }
		objectTypes
	}
}

dsl.cognos.play {
	findAll( searchPath: buildSearchPath( targetPath: script.parameters.searchPath, objectTypes: script.parameters.objectTypes ), properties: [PropEnum.metadataModelPackage, PropEnum.packageBase]).collect {
		println it.searchPath.value
		println it.metadataModelPackage.value[0].searchPath.value
		println '-------------->'
		
		it.metadataModelPackage.value[0].searchPath.value
	}.unique().sort().each {
		println it
	}
}

return
