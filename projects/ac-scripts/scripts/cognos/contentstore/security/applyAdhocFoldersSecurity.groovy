import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Applies security to Ad-Hoc Reports folders according to resources.cognos."adhocFoldersPoliciesConfig.groovy"'
}

dsl.cognos.play {
	def adhocFolderPolicies = ac.config.cognos.security.adhocFoldersPolicies.collect { group, permissions -> 
		newPolicy( securityObject: find( searchPath : group ), permissions: permissions ) 
	}
	
	eachObject( searchPath: "/content/descendant::folder[@name='Ad Hoc Reports']" ) { adhocFolder ->
		setPolicies object: adhocFolder, policies: adhocFolderPolicies
		script.log.info adhocFolder.searchPath.value
	}
}
