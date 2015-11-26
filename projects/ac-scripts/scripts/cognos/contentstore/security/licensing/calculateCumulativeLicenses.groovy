import com.cognos.developer.schemas.bibus._3.*
import static com.xlson.groovycsv.CsvParser.parseCsv
import au.com.bytecode.opencsv.*

def scriptDescriptor = {
	discoverable
	tags = ['licensing', 'audit']

	parameters { licenseFiles }
}

ac.scripts.clearTempFolder()

licenseScale = [
	'BI Recipient',
	'BI Consumer',
	'BI Enhanced Consumer',
	'BI Business Manager',
	'BI Business Analyst',
	'BI Business Author',
	'Advanced BI Business Author',
	'BI Professional Author',
	'BI Professional',
	'BI Administrator',
	'unidentified'
]

def allLicenses = [:]
script.parameters.licenseFiles.each{file->
	def licenses = parseCsv( new File( file).newReader() ).findAll{it.entryType=='license'}
	licenses.each {
		allLicenses.put(it.user,allLicenses.containsKey(it.user)?allLicenses.get(it.user) << it.value:[it.value])
	}
}

def highestLicenses=[]
script.tempFileCsv.call( '' ).withPrintWriter { writer ->
	def csvWriter = new CSVWriter( writer )
	csvWriter.writeNext( (String[]) ['user', 'license', 'value'])

	allLicenses.entrySet().each{entry->
		entry.value.each{specificLicense->
			csvWriter.writeNext( (String[]) [entry.key, 'specific', specificLicense])
		}
		def highestLicense = getHighestLicense(entry.value)
		csvWriter.writeNext( (String[]) [entry.key, 'final', highestLicense])
		highestLicenses<<highestLicense
	}
}

script.tempFile.call('txt', 'summary').withPrintWriter { writer ->
	highestLicenses.groupBy{ it }.each { type, pool ->
		writer.println "${type}:\t${pool.size()}"
	}
}

return

def getHighestLicense(licenses) {
	def highestLicense
	licenses.each{license->
		highestLicense = licenseScale.indexOf(highestLicense)>licenseScale.indexOf(license)?highestLicense:license
	}
	return highestLicense
}

