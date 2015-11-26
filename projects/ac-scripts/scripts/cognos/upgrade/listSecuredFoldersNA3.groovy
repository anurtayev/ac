import com.cognos.developer.schemas.bibus._3.*
import static com.xlson.groovycsv.CsvParser.parseCsv

scopeFile='C:/Users/anurtay/Google Drive/work/C78Upgrade/20150515. objects extract final.csv'

def reportData = parseCsv( new File( scopeFile ).newReader() ).findAll{
	!it.COGIPF_REPORTPATH.startsWith("CAMID")
}.collect {
	assert it.COGIPF_REPORTPATH.parent.type == 'folder'
	it.COGIPF_REPORTPATH.parent
}.toSet().collect{ extractAllParents(it) }.flatten().toSet().collect{searchPath->
	dsl.cognos.play {
		find(searchPath: searchPath, properties: [PropEnum.policies], options: new QueryOptions(schemaInfo:true))
	}
}.findAll{ !it.policies.schemaInfo.acquired }.collect{it.searchPath.value}.sort().collect { new Expando(searchPath:it)}

ac.scripts.writeCsv(file: script.defaultTempFileCsv, reportData: reportData)

return

def extractAllParents(folderSearchPath) {
	def result = [folderSearchPath]
	def mf = folderSearchPath
	while ( mf != '/content' ) {
		mf = mf.parent
		result << mf
	}
	return result
}