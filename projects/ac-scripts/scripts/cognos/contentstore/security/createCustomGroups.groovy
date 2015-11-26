import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	
	def numberOfDeletedObjects = delete objects: findAll( searchPath: 'CAMID(":")' + "/namespaceFolder[@name='${ac.config.cognos.security.namespaceFolder}']" )
	script.log.info numberOfDeletedObjects ? "deleted existing '${ac.config.cognos.security.namespaceFolder}' namespace folder" : "namespace folder '${ac.config.cognos.security.namespaceFolder}' didn't exist"
	
	def securityFolder = add( objects: [ new NamespaceFolder( defaultName: new TokenProp( value: ac.config.cognos.security.namespaceFolder ) ) ], parentPath: 'CAMID(":")' ) [0]
	script.log.info "created namespace folder: '${securityFolder.searchPath.value}'"
	
	findAll( searchPath: config.ldap.namespaceCamid + "//group[starts-with(@name, 'BI.Role.')]" ).collect { it.defaultName.value }.each { groupName ->
		add objects: [ new Group( defaultName: new TokenProp( value: groupName ) ) ], parentPath: securityFolder.searchPath.value
		script.log.info "added group: ${groupName}"
	}
}
