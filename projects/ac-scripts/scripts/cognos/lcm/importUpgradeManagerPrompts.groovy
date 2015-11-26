import util.*
import groovy.xml.*

assert params.projectFile, '<projectFile> is missing'
assert params.promptsFile, '<promptsFile> is missing'

def xml = new XmlParser().parse( params.projectFile )
def reports = xml.breadthFirst().findAll { node -> 	node.name() == 'report' }

new File( params.promptsFile ).eachLine { promptLine ->

	def promptLineParts = promptLine.split( ',' )
	if ( promptLineParts.size() <= 5 ) {
		log.warn "value is missing: ${promptLineParts}"
		return
	}
	
	def reportName = promptLineParts [ 0 ]
	def reportSearchPath = promptLineParts [ 1 ]
	def parameterName = promptLineParts [ 2 ]
	def parameterValue = promptLineParts [ 5 ]
	
	log.debug promptLine
	log.debug "${reportName}|${parameterName}|${parameterValue}"
	log.debug reportSearchPath
	
	def parameter = reports.find { report -> report.@searchPath == reportSearchPath }?.parameter.find { parameter -> parameter.@name == parameterName }

	if ( ! parameter ) {
		log.warn "parameter not found: ${reportName}/${parameterName}"
		return
	}
	
 	def simpleParameterValueNode = parameter.appendNode ( 'simpleParameterValue', [ inclusive: true ] )
 	simpleParameterValueNode.appendNode ( 'value', parameterValue )
}

reports.each { report ->
	if ( report.parameter.every { parameter -> parameter.children().size() == 1 } ) {
		report.taskData.each { taskData ->
			taskData.value[ 0 ].@status = '101'
		}
	}
}

def printer = new XmlNodePrinter( new PrintWriter( new FileWriter( params.projectFile + '.values' ) ) )
printer.preserveWhitespace = true
printer.print( xml )
