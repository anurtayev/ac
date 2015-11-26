import java.util.regex.Pattern.Script;

import com.cognos.developer.schemas.bibus._3.*

def allSearchPaths = [] as Set
def allAccounts
def allNamespaceAccounts

dsl.cognos.play{
	allNamespaceAccounts = findAll( searchPath: dsl.cognos.config.ldap.namespaceCamid + "/descendant::account" )
	script.log.debug "allNamespaceAccounts is done"
	
	def searchPath = dsl.cognos.config.ldap.namespaceCamid + "/descendant::*[starts-with(@name,'BI.Role')]"
	
//	findAll( searchPath: searchPath, properties: [PropEnum.members ]).each{
//		it.members.value.each{
//			allSearchPaths.add(it.searchPath.value)
//		}
//	}

//	findAll( searchPath: "expandSecurityMembers(${searchPath})").each{
//		allSearchPaths.add(it.searchPath.value)
//	}
	
	findAll( searchPath: searchPath).each{
		find(searchPath: it.searchPath.value,properties: [PropEnum.members ]).members.value.each{
			allSearchPaths.add(it.searchPath.value)
		}
	}
	
	allAccounts = allNamespaceAccounts.findAll{ allSearchPaths.contains(it.searchPath.value) }
}

return allAccounts
