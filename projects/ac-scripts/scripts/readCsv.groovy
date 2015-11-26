description 'reads CSV'

parameters {
		file
}

run {
	
//	import static com.xlson.groovycsv.CsvParser.parseCsv
	parseCsv( new File( script.parameters.scopeFile ).newReader() )
}
