import com.cognos.developer.schemas.bibus._3.*

cognos.obj.play {
	def dataSourceSearchPath = 'CAMID(":")' + '/dataSource'
	eachObject( searchPath: dataSourceSearchPath ) { dataSource ->
		script.log.debug "datasource ${dataSource.defaultName.value}"
		def dataSourceConnectionSearchPath = dataSource.searchPath.value + '/dataSourceConnection'
		eachObject( searchPath: dataSourceConnectionSearchPath, properties: [ PropEnum.connectionString ] ) { dataSourceConnection ->
			script.log.debug dataSourceConnection.connectionString.value
			println "${dataSource.defaultName.value},${dataSourceConnection.defaultName.value}"
		}
	}
}

return