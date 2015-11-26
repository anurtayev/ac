import com.cognos.developer.schemas.bibus._3.*
import sys.dsl.*

println 'hey ya!'
return

cognos.play { 
	runImport deploymentArchive: 'DeployObjectsBetweenEnv_20140226033905_backup', deploymentName: 'qqq333'
}
