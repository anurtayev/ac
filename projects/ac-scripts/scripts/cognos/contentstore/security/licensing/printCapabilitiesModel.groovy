import com.cognos.developer.schemas.bibus._3.*
import au.com.bytecode.opencsv.*

def capabilitiesModel = ac.scripts.cognos.contentstore.security.licensing.getCapabilitiesModel()

csvWriter = new CSVWriter( script.defaultTempFileCsv.newPrintWriter() )
csvWriter.writeNext( (String[]) ['capability','capabilitySearchPath','description','member','permission','access'] )

capabilities.each { capability ->
	capability.policies.value.each{ policy ->
		policy.permissions.each { permission ->
			csvWriter.writeNext ((String[]) [ capability.defaultName.value,capability.searchPath.value,capability.userCapability.value.value,policy.securityObject.searchPath.value,permission.name,permission.access ] )
		}
	}
}

csvWriter.close()

return