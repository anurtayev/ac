import sys.dsl.*
import com.cognos.developer.schemas.bibus._3.*

cognos.play {
	def adhocFolder = new Folder( 
		name: new MultilingualTokenProp( 
			value : [ 'en': 'Ad Hoc Reports', 'es': 'Reportes Ad Hoc' ].collect { locale, name ->
				new MultilingualToken( locale : locale, value : name )
			}.toArray() 
		)
	)

	eachObject( searchPath: "/content/folder[@name!='Americas']/folder/folder/folder", properties: [ PropEnum.parent ] ) { applicationFolder ->
		def objects = findAll( searchPath: applicationFolder.searchPath.value + "/folder[@name='Ad Hoc Reports']" )
		if ( objects.size() == 0 ) {
			add parentPath: applicationFolder.searchPath.value, objects: [ adhocFolder ]
			log.info "added adhoc folder to ${applicationFolder.searchPath.value}"
		}
	}
}

contentstore_security_ApplyAdhocFoldersSecurity()