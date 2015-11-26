import com.cognos.developer.schemas.bibus._3.*
import static com.xlson.groovycsv.CsvParser.parseCsv

def scriptDescriptor = {
	tags = ['groovycsv', 'csv', 'read csv']
	
	parameters {
		inputFile
	}
}

parseCsv( new File( script.parameters.inputFile ).newReader() )
