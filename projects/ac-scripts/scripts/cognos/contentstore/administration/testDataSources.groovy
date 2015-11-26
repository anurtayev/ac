import com.cognos.developer.schemas.bibus._3.*
import java.rmi.RemoteException
import org.apache.axis.AxisFault
import groovy.xml.MarkupBuilder

def scriptDescriptor = { discoverable }

dsl.cognos.play {
	ac.config.dataSources.each { dataSourceName, dataSourceparams ->
		dataSourceparams.each { connectionName, connectionParams ->
			script.log.info "testing ${dataSourceName}/${connectionName}/(${connectionParams.usr}/${connectionParams.pwd})"
			try {
				script.log.debug getConnectionString( tnsEntry: connectionParams.tnsEntry )
				script.log.debug getDsCredentials( usr: connectionParams.usr, pwd: connectionParams.pwd )
				testDataSourceConnection connectionString: getConnectionString( tnsEntry: connectionParams.tnsEntry ), credentials: getDsCredentials( usr: connectionParams.usr, pwd: connectionParams.pwd )
				script.log.info 'data source test - ok'
			} catch ( RemoteException e ) {
				AxisFault f = (AxisFault) e
				String a1 = f.dumpToString()
				int start = a1.indexOf( "messageString>" ) + 13
				int end = a1.indexOf( "</", start )
				String message = a1.substring( start + 1, end )
				script.log.warn message
			}
		}
	}
}

return
 