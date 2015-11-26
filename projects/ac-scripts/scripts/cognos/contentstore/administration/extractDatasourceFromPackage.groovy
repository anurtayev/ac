import com.cognos.developer.schemas.bibus._3.*
import sys.cognos.*

def scriptDescriptor = {
	discoverable
	description = 'Extracts datasources from model inside package specified.'

	parameters { packageSearchPath }
}

cognos.play {
	assert exists( searchPath: parameters.packageSearchPath )
	def model = find( searchPath: "${parameters.packageSearchPath}/model", properties: [PropEnum.model ])

	new XmlSlurper().parseText( model.model.value ).dataSources.dataSource.each { dataSource ->
		log.debug dataSource.name.text()
		def dataSourceConnectionSearchPath = 'CAMID(":")' + "/dataSource[@name='" + dataSource.name.text() + "']/dataSourceConnection"
		eachObject( searchPath: dataSourceConnectionSearchPath, properties: [
			PropEnum.defaultName,
			PropEnum.connectionString ]
		) { dataSourceConnection ->
			println "${parameters.packageSearchPath},${dataSource.name.text()},${dataSourceConnection.defaultName.value},${dataSourceConnection.connectionString.value}"
		}
	}
}