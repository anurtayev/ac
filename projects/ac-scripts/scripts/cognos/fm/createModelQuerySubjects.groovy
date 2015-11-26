import com.opencsv.CSVReader

def scriptDescriptor = {
	discoverable
	description = 'Creates model query subjects'
	tags = ['Framework Manager']

	parameters { inputFile }
}

List csvEntries = new CSVReader(new FileReader(script.parameters.inputFile)).readAll().findAll{ it[3] && it[4] }.collect{ it[0] }.unique().each{
	println it
}
