import com.cognos.developer.schemas.bibus._3.*

def allAccounts
dsl.cognos.play{
	allAccounts = findAll( searchPath: dsl.cognos.config.ldap.namespaceCamid + "/descendant::account" )
}

def nonUniqueAccounts = []
allAccounts.groupBy{ it.defaultName.value }.each{ defaultName, pool ->
	if (pool.size()>1){
		script.log.warn "non unique default name: ${defaultName}"
		nonUniqueAccounts.addAll(pool)
	} 
}

def uniqueAccounts = allAccounts - nonUniqueAccounts

return uniqueAccounts