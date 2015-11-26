import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Matches all folders in Public Folders to respective group in AD if any. Assigns default access if none found.'
}

ac.scripts.cognos.contentstore.security.ResetContentSecurityPolicy()

dsl.cognos.play {
	def predefinedSecurityObjects = [ find( searchPath: 'CAMID(":Readers")' ) ]
	
	eachObject( searchPath: '/content//folder', properties: [ PropEnum.defaultName ] ) { folder ->
		
		def securityGroups = findAll( searchPath: ac.config.cognos.security.cognosCamid + "//group[starts-with(@name,'BI.Role.') and ends-with(@name,'.${folder.defaultName.value.replaceAll( ' ', { '_' } )}')]" )
		if ( securityGroups.size() == 1 ) {
			def policies = ( predefinedSecurityObjects + securityGroups[ 0 ] ).collect{ group ->
				newPolicy( securityObject: group, permissions: [ 'read', 'execute', 'traverse' ] )
			}
			
			setPolicies object: folder, policies: policies
			script.log.info "${folder.searchPath.value} group added: ${securityGroups[ 0 ].searchPath.value}"
			script.log.info "===> ${folder.defaultName.value}"
		} else {
			script.log.warn "===> ${folder.defaultName.value} was not secured. Number of matching groups is ${securityGroups.size()}"
		}
	}
}

return
