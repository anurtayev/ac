import com.cognos.developer.schemas.bibus._3.*
import au.com.bytecode.opencsv.*

def scriptDescriptor = {
	description = 'licensed accounts = all namespace accounts - ldap disabled accounts'

	parameters {
		disabledAccounts { optional }
		visibleAccounts { optional }
	}
}

//def namespaceAccounts = ac.scripts.cognos.contentstore.security.licensing.getNamespaceAccounts()
def namespaceAccounts = ac.scripts.cognos.contentstore.security.licensing.getNamespaceAccountsFromCognosRoles()
script.log.debug "total number of accounts in Cognos groups: ${namespaceAccounts.size()}"

//**************** remove disabled accounts
def minusDisabled
if ( script.parameters.disabledAccounts ) {
	def disabledAccounts = ac.scripts.parseCsv( inputFile: script.parameters.disabledAccounts ).collect{ getCn(it) }

	minusDisabled = namespaceAccounts.findAll  { nsAccount ->
		disabledAccounts.every { da->
			da != nsAccount.defaultName.value
		}
	}
	script.log.debug "total minus disabled: ${minusDisabled.size()}"
} else {
	minusDisabled = namespaceAccounts
}

//**************** remove invisible accounts
def licensedAccounts
if ( script.parameters.visibleAccounts ) {
	def visibleAccounts = ac.scripts.parseCsv( inputFile: script.parameters.visibleAccounts )
	licensedAccounts= minusDisabled.findAll{ account ->
		visibleAccounts.any{ visibleAccount ->
			account.defaultName.value == visibleAccount.userName
		}
	}
} else {
	licensedAccounts = minusDisabled
}

script.log.debug "final number of licensed accounts: ${licensedAccounts.size()}"
return licensedAccounts

def getCn( entry ) {
	entry.dn.split(',').find{it.startsWith('CN=')}.split('=')[1]
}