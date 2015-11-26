import com.cognos.developer.schemas.bibus._3.*
import static com.xlson.groovycsv.CsvParser.parseCsv

scopeFile='C:/Users/anurtay/Google Drive/work/C78Upgrade/ac.scripts.cognos.upgrade.listSecuredFoldersNA3_20150609135616.csv'

def reportData = parseCsv( new File( scopeFile ).newReader() ).collect{it.searchPath}.collect{folder->
	dsl.cognos.play {
		find(searchPath: folder, properties: [PropEnum.policies]).policies.value.collect{policy ->
			policy.permissions.collect {permission->
				new Expando(
					folder: folder,
					securityObject: policy.securityObject.searchPath.value,
					permission: permission.name
				)
			}
		}
	}
}.flatten()

ac.scripts.writeCsv(file: script.defaultTempFileCsv,reportData: reportData)