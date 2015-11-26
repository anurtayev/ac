import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	parameters {
		searchPath { description = 'Limits the scope of query' }
		objectTypes
	}
}

dsl.cognos.play {
	script.defaultTempFileCsv.withPrintWriter { writer ->
		writer.println "account,type,value"

		eachObject( searchPath: buildSearchPath( targetPath: script.parameters.searchPath, objectTypes: script.parameters.objectTypes ), properties: [
			PropEnum.metadataModelPackage,
			PropEnum.packageBase
		]) {  writer.println "${it.searchPath.value},${it.metadataModelPackage.value[0].searchPath.value}"  }
	}
}

return
