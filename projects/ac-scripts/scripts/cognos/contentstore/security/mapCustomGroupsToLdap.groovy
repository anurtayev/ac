import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	findAll( searchPath: ac.config.cognos.security.cognosCamid + "//group[starts-with(@name, 'BI.Role.')]" ).each { group ->

		def ldapGroups = findAll( searchPath: config.ldap.namespaceCamid + "//group[@name='${group.defaultName.value}']" )
		if (ldapGroups){
			group.members = new BaseClassArrayProp( value: ldapGroups[0] )
			update objects: [group]
			script.log.info "mapped ${group.searchPath.value} to LDAP"
		} else {
			script.log.warn "no LDAP group for ${group.searchPath.value}"
		}
	}
}
