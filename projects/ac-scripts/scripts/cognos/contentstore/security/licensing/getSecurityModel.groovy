import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	discoverable
	description = 'retrieves all groups and roles from Cognos and AD namespaces along with members'
	longDescription = 'first get containers. Then get members individually. Takes longer, but it is the only way. AD is returning less containers when they being searched along with members due to limitation of 1000 entries per query.'
}

def containersWithMembers = []
dsl.cognos.play {
	def getContainerMembers = { find( searchPath: it.searchPath.value, properties: [PropEnum.members ]) }

	// Cognos namespace
	def cognosNamespaceSearchPath = "${ac.config.cognos.security.cognosCamid}/descendant::group | ${ac.config.cognos.security.cognosCamid}/descendant::role"
//	containersWithMembers.addAll( findAll( searchPath: cognosNamespaceSearchPath, properties: [PropEnum.members ] ) )
	containersWithMembers.addAll( findAll( searchPath: cognosNamespaceSearchPath ).collect( getContainerMembers ) )
//	containersWithMembers.addAll( findAll( searchPath: "expandSecurityMembers(${cognosNamespaceSearchPath})" ))

	// AD. not require if userSearchPath == 'CAMID("::Anonymous")'. currently not checked as it is very rare case
	def ldapNamespaceSearchPath = "${config.ldap.namespaceCamid}/descendant::group[starts-with(@name,'BI.Role.')]"
//	containersWithMembers.addAll( findAll( searchPath: ldapNamespaceSearchPath, properties: [PropEnum.members ] ) )
	containersWithMembers.addAll( findAll( searchPath: ldapNamespaceSearchPath ).collect( getContainerMembers ) )
//	containersWithMembers.addAll( findAll( searchPath: "expandSecurityMembers(${ldapNamespaceSearchPath})" ))
}

return containersWithMembers
