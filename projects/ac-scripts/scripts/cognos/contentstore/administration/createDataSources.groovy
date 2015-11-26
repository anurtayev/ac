import com.cognos.developer.schemas.bibus._3.*
import java.rmi.RemoteException
import org.apache.axis.AxisFault
import groovy.xml.MarkupBuilder

def scriptDescriptor = { discoverable }

dsl.cognos.play {
	ac.config.dataSources.each { dataSourceName, dataSourceparams ->
		DataSource dataSource = add( objects: [new DataSource( defaultName: new TokenProp( value: dataSourceName ) )], parentPath: 'CAMID(":")' ) [0]
		script.log.info "==> ${dataSource.searchPath.value} created"

		dataSourceparams.each { connectionName, connectionParams ->

			def name = new TokenProp( value: connectionName )
			def connectionString = getConnectionString( tnsEntry: connectionParams.tnsEntry )
			script.log.info "${connectionParams.tnsEntry} connection string created"

			DataSourceConnection connection = add(
					objects: [
						new DataSourceConnection (
						defaultName: name,
						connectionString: new StringProp( value: connectionString )
						)
					],
					parentPath: dataSource.searchPath.value
					) [0]
			script.log.info "${connection.searchPath.value} created"

			def credentials = getDsCredentials( usr: connectionParams.usr, pwd: connectionParams.pwd )
			script.log.info "${connectionParams.usr}/${connectionParams.pwd} credentials created"
			DataSourceSignon signon = add(
					objects: [
						new DataSourceSignon (
						defaultName: name,
						credentials: new AnyTypeProp( value: credentials ),
						consumers: new BaseClassArrayProp( value: find( searchPath: 'CAMID(":")' + "/group[@name='Everyone']" ) )
						)
					],
					parentPath: connection.searchPath.value
					) [0]
			script.log.info "${signon.searchPath.value} created"
		}
	}
}

ac.scripts.cognos.contentstore.administration.testDataSources()

ac.scripts.cognos.contentstore.security.applyDataSourcesSecurity()

return
