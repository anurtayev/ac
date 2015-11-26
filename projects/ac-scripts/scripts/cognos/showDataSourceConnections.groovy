import com.cognos.developer.schemas.bibus._3.*
import sys.dsl.*

cognos.play { 
	def dataSourceConnectionsSearchPath = 'CAMID(":")' + "/dataSource/dataSourceConnection"
//			def dataSourceConnectionsSearchPath = 'CAMID(":")' + "/dataSource/dataSourceConnection[@name='Brazil']"
	log.info "DISABLED property status:"
	eachObject( searchPath: dataSourceConnectionsSearchPath, properties: [ PropEnum.searchPath, PropEnum.disabled ] ) { dataSourceConnection ->
		log.info "${dataSourceConnection.searchPath.value} is ${ dataSourceConnection.disabled.value }"
	}
}
