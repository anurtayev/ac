import com.cognos.developer.schemas.bibus._3.*
import au.com.bytecode.opencsv.*

def scriptDescriptor = {
	discoverable
	tags = ['licensing', 'audit']

	parameters {
		disabledAccounts
	}
}

ac.scripts.clearTempFolder()

def licenses = []
def sortedSecurityModel = ac.scripts.cognos.contentstore.security.licensing.getSecurityModel().sort{ it.searchPath.value }
def capabilitiesModel = ac.scripts.cognos.contentstore.security.licensing.getCapabilitiesModel()
def activeAccounts = ac.scripts.cognos.contentstore.security.licensing.getLicensedAccounts(disabledAccounts: script.parameters.disabledAccounts, visibleAccounts: script.parameters.visibleAccounts)

//**************************************
csvWriter = new CSVWriter( script.defaultTempFileCsv.newPrintWriter() )
csvWriter.writeNext( (String[]) ['user','entryType','value'] )

def numberOfAccounts =activeAccounts.size() 
activeAccounts.eachWithIndex { user, index ->
	script.log.info "processing account ${++index}/${numberOfAccounts}"

	def userMemberships = ac.scripts.cognos.contentstore.security.licensing.getUserMemberships( userSearchPath: user.searchPath.value, sortedSecurityModel: sortedSecurityModel )
	script.log.debug "memberships for ${user.defaultName.value}: ${userMemberships.collect{it.defaultName.value}}"
	userMemberships.each { membership->
		csvWriter.writeNext ((String[]) [ user.defaultName.value, 'membership', membership.defaultName.value ] )
	}
	
	def userCapabilities = ac.scripts.cognos.contentstore.security.licensing.getUserCapabilities( roles: userMemberships, capabilitiesModel: capabilitiesModel )
	script.log.debug "capabilities for ${user.defaultName.value}: ${userCapabilities}"
	userCapabilities.each { capability->
		csvWriter.writeNext ((String[]) [ user.defaultName.value, 'capability', capability ] )
	}
	
	def license = ac.scripts.cognos.contentstore.security.licensing.getLicenseTypeC10( capabilities: userCapabilities )
	script.log.debug "license for ${user.defaultName.value}: ${license}"
	csvWriter.writeNext ((String[]) [ user.defaultName.value, 'license', license ] )
	
	licenses << license
}

script.tempFile.call('txt', 'summary').withPrintWriter { writer ->
	licenses.groupBy{ it }.each { type, pool ->
		writer.println "${type}:\t${pool.size()}"
	}
}

csvWriter.close()

return