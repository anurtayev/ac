import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	script.log.info "===> mapping Cognos groups"
	ac.config.cognos.security.systemGroupsMembers.each { cognosGroup, members ->
		setMembers object: find( searchPath: cognosGroup ), members: members.collect { find( searchPath: it ) }
		script.log.info "${cognosGroup} members set to ${members}"
	}
	
	script.log.info "===> mapping Cognos capabilities"
	ac.config.cognos.security.capabilities.each { securedFunction, policies ->
		setPolicies object: find( searchPath: securedFunction ), policies: policies.collect{ group, permissions ->
			newPolicy( securityObject: find( searchPath : group ), permissions: permissions )
		}
		script.log.info "${policies} added to ${securedFunction}"
	}
}

return