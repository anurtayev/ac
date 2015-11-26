import com.cognos.developer.schemas.bibus._3.*
import au.com.bytecode.opencsv.*

script.tempFileCsv.call( '' ).withPrintWriter { writer ->

	def csvWriter = new CSVWriter( writer )
	csvWriter.writeNext( (String[]) [
		'defaultName',
		'searchPath',
		'membersDefaultName',
		'membersSearchPath'
	])

	ac.scripts.cognos.contentstore.security.licensing.getSecurityModel().each { securityContainer ->
		securityContainer.members.value.each { member ->
			def accounts = dsl.cognos.play{findAll( searchPath: member.searchPath.value )}
			if (accounts.size()==1) {
				csvWriter.writeNext( (String[]) [
					securityContainer.defaultName.value,
					securityContainer.searchPath.value,
					accounts[0].defaultName.value,
					member.searchPath.value
				])
			} else {
				script.log.warn "non existent group member ${member.searchPath.value}"
			}
		}
	}
}

return 