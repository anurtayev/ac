import groovy.xml.*

def scriptDescriptor = {
	description = 'imports prompts from CSV file into project.dat'

	parameters {
		projectFileFolder { description = 'path to folder with project.dat file' }
		promptsFile { description = 'CSV file that contains prompt values' }
	}
}

def projectFileName = parameters.projectFileFolder + '/project.dat'
def xml = new XmlParser().parse( projectFileName )

def reports = xml.breadthFirst().findAll { node -> 	node.name() == 'report' }

new File( parameters.promptsFile ).eachLine { promptLine ->

	def promptLineParts = promptLine.split( ',' )
	// if ( promptLineParts.size() <= 6 ) {
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
	
	// log.debug promptLine
	log.debug "${reportName}|${parameterName}|${parameterValue}"
	
	def report = reports.find { report -> report.'@searchPath' == reportSearchPath }
	
	if ( ! report ) {
		log.warn "LCM project doesn't contain ${reportSearchPath}"
		return
	}
	
	// populate value
	def parameter = report.parameter.find { it.'@name' == parameterName }
	if ( parameter ) { // parameter exist in project file
		def advanceParameterMap = getAdvanceParameterMap( report )
		
		def advanceParameterMapEntry = advanceParameterMap.parameter.find { it.'@name' == parameterName }
		
		if ( advanceParameterMapEntry ) { // replace existing value
			log.warn 'replacing existing parameter value'
			advanceParameterMapEntry.replaceNode { oldAdvanceParameterMapEntry ->
				parameter( 
					name: oldParameter.'@name', 
					capabilities: oldParameter.'@capabilities', 
					dataType: oldParameter.'@dataType', 
					modelFilterItem: oldParameter.'@modelFilterItem' 
				) {
					simpleParameterValue( inclusive: true ) {
						value( parameterValue )
					}
				}
			}
			
			return
		} else { // create a parameter-value entry
			def newParameter = advanceParameterMap.appendNode( 'parameter',  parameter.attributes() )
			def simpleParameterValueNode = newParameter.appendNode ( 'simpleParameterValue', [ inclusive: true ] )
			simpleParameterValueNode.appendNode ( 'value', parameterValue )
		}
	} else { // no such parameter
		log.warn "parameter defined in CSV doesn't exist in project file: ${parameterName}"
		return
	}
}

// populate missing parameters in advanceParametersMap
reports.each { report ->
	def presentParameters = report.advanceParameterMap.parameter
	def specParameters = report.parameter
	def advanceParameterMap = getAdvanceParameterMap( report )
	
	specParameters.findAll { specParameter ->
		! presentParameters.find {
			presentParameter -> presentParameter.'@name' == specParameter.'@name' 
		}
	}.each { parameterToCreate ->
		advanceParameterMap.appendNode( 'parameter',  parameterToCreate.attributes() )
	}
}

// advance reports statuses where all mandatory parameters are present
reports.each { report ->
	def presentParameters = report.advanceParameterMap.parameter.findAll { it.isMandatory() && it.children().size() == 1 }
	def specParameters = report.parameter.findAll { it.isMandatory() }
	
	if ( presentParameters.size() == specParameters.size() ) {
		report.breadthFirst().findAll{ node -> 
			( node.getClass().name == 'groovy.util.Node'		// bug? filter out garbage in list returned by breadthFirst
			&& node.name() == 'value' 
			&& !node.attributes().values().contains('reUpgradeTaskData') 
			&& node.attributes().size() == 4 ) 
		}.each { node ->
			def attributes = node.attributes()
			attributes.put( 'status', '101' )
			
			def newNode = new Node( node.parent(), node.name(), attributes )
			node.children().each { childNode ->
				newNode.append( childNode )
			}
			
			node.replaceNode( newNode )
		}
	}
}

def printer = new XmlNodePrinter( new PrintWriter( new FileWriter( projectFileName + '.values' ) ) )
printer.preserveWhitespace = true
printer.print( xml )


private getAdvanceParameterMap( report ) {
	def advanceParameterMap 
	def advanceParameterMapList = report.advanceParameterMap
	
	if ( advanceParameterMapList.size() == 0 ) { // create new
		advanceParameterMap = report.appendNode ( 'advanceParameterMap' )
		advanceParameterMap.appendNode ( 'name', 'en-us' )
	} else { // return existing
		advanceParameterMap = advanceParameterMapList[0]
	}
	
	return advanceParameterMap
}