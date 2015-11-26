import com.cognos.developer.schemas.bibus._3.*
import sys.dsl.*

def scriptDescriptor = {
	description = 'Changes status of a connection object under dataSource object'
	parameters {
		connectionName { description = 'connection name as per keys in config.contentStore.dataSources (Brazil, Ecuador, etc.) to be disabled/enabled' }

		disabled { description = 'can take values: true, false' }
	}
}

cognos.play {
	def dataSourceConnectionsSearchPath = 'CAMID(":")' + "/dataSource/dataSourceConnection[@name='${params.connectionName}']"
	eachObject( searchPath: dataSourceConnectionsSearchPath, properties: [
		PropEnum.searchPath,
		PropEnum.disabled ]
	) { dataSourceConnection ->
		dataSourceConnection.disabled = new BooleanProp( value: params.disabled )
		update objects: [dataSourceConnection]
		log.info "${dataSourceConnection.searchPath.value} <disabled> property is now ${ dataSourceConnection.disabled.value }"
	}
}
