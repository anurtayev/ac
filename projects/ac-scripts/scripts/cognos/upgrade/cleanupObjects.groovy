import com.cognos.developer.schemas.bibus._3.*
import static com.xlson.groovycsv.CsvParser.parseCsv

def scriptDescriptor = {
	discoverable
	parameters {
		scopeFile
		folder
	}
}

def skipTypes = [
	'folder',
	'package',
	'model',
	'modelView',
	'packageConfiguration',
	'pagelet',
	'portletInstance',
	'jobDefinition',
	'jobStepDefinition'
]
def scopeObjects = parseCsv( new File( script.parameters.scopeFile ).newReader() ).findAll{!it.COGIPF_REPORTPATH.startsWith("CAMID")}

dsl.cognos.play {
	findAll(searchPath: "${script.parameters.folder}/descendant::*").findAll { object ->
		!scopeObjects.any {it.COGIPF_REPORTPATH == object.searchPath.value - "/folder[@name='cleanup']" } && skipTypes.every { object.searchPath.value.type != it }
	}.each{
		delete objects: [it.searchPath.value]
		script.log.debug "${it.searchPath.value} - deleted"
	}
}