import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	def templatePolicies = ac.config.cognos.security.datasourcesPolicies.collect { group, permissions ->
		newPolicy( securityObject: find( searchPath : group ), permissions: permissions )
	}
	
	eachObject( searchPath: 'CAMID(":")/dataSource/dataSourceConnection' ) { dataSourceConnection ->
		script.log.debug "=============> ${dataSourceConnection.defaultName.value}"
		def groupSearchPath = ac.config.cognos.security.cognosCamid + "//group[starts-with(@name,'BI.Role.') and ends-with(@name, '${normalize(dataSourceConnection.defaultName.value)}')]" 
		script.log.debug groupSearchPath
		def objs = findAll( searchPath : groupSearchPath )
		script.log.debug objs.size()
		objs.each {
			script.log.debug it.searchPath.value
		}
		 
		try {
			setPolicies ( 
				object: dataSourceConnection, 
				policies: templatePolicies + newPolicy( 
					securityObject: find( searchPath : groupSearchPath ), 
					permissions: [ 'read', 'execute', 'traverse' ]
				)
			)
			
			script.log.info "secured ${dataSourceConnection.searchPath.value}"
		} catch (java.lang.AssertionError e) {
			script.log.warn "couldn't find a security group for ${dataSourceConnection.searchPath.value}"
		}
	}
}

return

def normalize( value ) {
	return value.replaceAll( ' ', { '_' } )
}