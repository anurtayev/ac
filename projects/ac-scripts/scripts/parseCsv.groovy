import static com.xlson.groovycsv.CsvParser.parseCsv

def scriptDescriptor = {
	tags = ['groovycsv', 'csv', 'read csv']
	
	parameters {
		file
	}
}

parseCsv( new File( script.parameters.inputFile ).newReader() ).toList()
