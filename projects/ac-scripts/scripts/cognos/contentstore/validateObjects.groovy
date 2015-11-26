import com.cognos.developer.schemas.bibus._3.*
import util.*

Cognos.session( contentManager: config.contentManager, credentials: config.domainCredentials ) {
	eachObject( searchPath: "/content/folder[@name='Brazil']/folder[@name='Corporate']/folder[@name='Finance']/folder[@name='Treasury']//report" ) { object ->
		def sp = new SearchPath( object.searchPath.value )
		writer.print "${object.class.toString().tokenize('.').last()},${object.defaultName.value},${sp},${sp.canonical}"
		
		params.properties.each { property ->
			def fieldVal = object."${property}".value
			writer.print "\t${property}:\t${fieldVal}"
		}
	}
}
