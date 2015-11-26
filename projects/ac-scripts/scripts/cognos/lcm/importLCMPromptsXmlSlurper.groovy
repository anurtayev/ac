import groovy.xml.*

def scriptDescriptor = {
	description = 'imports prompts from CSV file into project.dat'

	parameters {
		projectFileFolder { description = 'path to folder with project.dat file' }
		promptsFile { description = 'CSV file that contains prompt values' }
	}
}

def xml = new XmlSlurper().parse( parameters.projectFileFolder + '/project.dat' )
def reports = xml.breadthFirst().findAll { node -> 	node.name() == 'report' }

new File( parameters.promptsFile ).eachLine { promptLine ->

	def promptLineParts = promptLine.split( ',' )
	if ( promptLineParts.size() <= 5 ) {
		log.warn "value is missing: ${promptLineParts}"
		return
	}
	
	log.debug promptLine
	promptLineParts.each {
		log.debug it
	}
	
	def reportName = promptLineParts [ 0 ]
	def reportSearchPath = promptLineParts [ 1 ]
	def parameterName = promptLineParts [ 2 ]
	def parameterValue = promptLineParts [ 5 ]
	
	def parameter = reports.find { report -> report.@searchPath == reportSearchPath }?.parameter.find { parameter -> parameter.@name == parameterName }

	if ( ! parameter ) {
		log.warn "parameter not found: ${reportName}/${parameterName}"
		return
	}
	
/* 	parameter.replaceNode { oldParameter ->
		parameter( 
			name: oldParameter.@name, 
			capabilities: oldParameter.@capabilities, 
			dataType: oldParameter.@dataType, 
			modelFilterItem: oldParameter.@modelFilterItem 
		) {
			simpleParameterValue( inclusive: true ) {
				value( parameterValue )
			}
		}
	}
 */	
 	parameter.appendNode {
		delegate = parameter
		resolveStrategy = Closure.DELEGATE_ONLY
		
//		log.debug "${reportName}|${parameterName}|${parameterValue}"
		simpleParameterValue( inclusive: true ) {
			value( parameterValue )
		}
	}
}

reports.each { report ->
	if ( 
		report.parameter.every { parameter ->
			parameter.childNodes().toList()
		} 
	) {
		println '-' * 10
		println report.@name
		report.parameter.each { parameter -> 
			println parameter.@name
			println parameter.text()
			println parameter.simpleParameterValue.value.text()
		}
		println '-' * 10
	}
}

new FileWriter( parameters.projectFileFolder.toString() + '/project.dat.values' ) << new StreamingMarkupBuilder().bind { 
	mkp.yield xml
}

reports.each { report ->
	if ( 
		report.parameter.every { parameter ->
			parameter.childNodes().toList()
		} 
	) {
		println '-' * 10
		println report.@name
		report.parameter.each { parameter -> 
			println parameter.@name
			println parameter.text()
			println parameter.simpleParameterValue.value.text()
		}
		println '-' * 10
	}
}

