import com.cognos.developer.schemas.bibus._3.*
import util.*

assert params.packagesPaths, '<packagesPaths> is missing'	// mandatory parameter, only individual package can processed. SearchPath mask will cause system to fail

Cognos.session( contentManager: config.contentManager, credentials: config.domainCredentials ) {
	params.packagesPaths.each { packagePath ->
		log.debug packagePath
		def model = find( searchPath: "${packagePath}/model", properties: [ PropEnum.model ] )
		new XmlSlurper().parseText( model.model.value ).dataSources.dataSource.each { dataSource ->
			log.debug dataSource.name.text()
			def dataSourceConnectionSearchPath = 'CAMID(":")' + "/dataSource[@name='" + dataSource.name.text() + "']/dataSourceConnection"
			eachObject( searchPath: dataSourceConnectionSearchPath, properties: [ PropEnum.defaultName, PropEnum.connectionString ] ) { dataSourceConnection ->
				writer.println "${packagePath},${dataSource.name.text()},${dataSourceConnection.defaultName.value},${dataSourceConnection.connectionString.value}"
			}
		}
	}
}
