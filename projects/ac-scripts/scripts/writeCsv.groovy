
description 'writes CSV file'

tags { csv }

parameters {
	file
	reportData
}

run {
	
//	import au.com.bytecode.opencsv.*
	
	def keys = script.parameters.reportData[0].properties.keySet().toArray()

//	csvWriter = new CSVWriter( script.parameters.file.newPrintWriter() )
	csvWriter.writeNext( (String[]) keys )

	script.parameters.reportData.each { expando ->
		def reportLine = []
		keys.each { key->
			reportLine << expando.getProperty(key)
		}

		csvWriter.writeNext ((String[]) reportLine )
	}

	csvWriter.close()
}