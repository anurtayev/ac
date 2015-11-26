import com.cognos.developer.schemas.bibus._3.*
import au.com.bytecode.opencsv.*

def scriptDescriptor = {
	discoverable
	tags = ['licensing', 'audit']

	parameters {
		disabledAccounts
		dumpToFile {
			optional
			description = 'boolean. saves model to CSV file'
		}
	}
}

ac.scripts.clearTempFolder()

def licenses = []
def capabilitiesModel = ac.scripts.cognos.contentstore.security.licensing.getCapabilitiesModel(dumpToFile:script.parameters.dumpToFile)
def activeAccounts = ac.scripts.cognos.contentstore.security.licensing.getLicensedAccounts(disabledAccounts: script.parameters.disabledAccounts)

//**************************************
csvWriter = new CSVWriter( script.defaultTempFileCsv.newPrintWriter() )
csvWriter.writeNext( (String[]) ['user','entryType','value'] )

def numberOfAccounts =activeAccounts.size() 
activeAccounts.eachWithIndex { user, index ->
	script.log.info "processing account ${++index}/${numberOfAccounts}"

	def membership = dsl.cognos.play{findAll( searchPath: "membership(${user.searchPath.value})" )}
	membership.each{
		println it.searchPath.value
	}
	
	def userCapabilities = ac.scripts.cognos.contentstore.security.licensing.getUserCapabilities( roles: membership, capabilitiesModel: capabilitiesModel )
	script.log.debug "capabilities for ${user.defaultName.value}: ${userCapabilities}"
	userCapabilities.each { capability->
		csvWriter.writeNext ((String[]) [ user.defaultName.value, 'capability', capability ] )
	}
	
	def license = ac.scripts.cognos.contentstore.security.licensing.getLicenseTypeC8( capabilities: userCapabilities )
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