import util.*
import groovy.xml.*

assert params.projectFile, '<projectFile> is missing'
assert params.promptsFile, '<promptsFile> is missing'

def xml = new XmlSlurper().parse( params.projectFile )
def reports = xml.breadthFirst().findAll { node -> 	node.name() == 'report' }

new File( params.promptsFile ).eachLine { promptLine ->

	def promptLineParts = promptLine.split( ',' )
	if ( promptLineParts.size() <= 4 ) {
		return
	}
	
	def reportName = promptLineParts [ 0 ]
	def reportSearchPath = promptLineParts [ 1 ]
	def parameterName = promptLineParts [ 2 ]
	def parameterValue = promptLineParts [ 4 ]
	
	
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
		log.debug "${reportName}|${parameterName}|${parameterValue}"
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

new FileWriter( params.projectFile + '.values' ) << new StreamingMarkupBuilder().bind { 
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

