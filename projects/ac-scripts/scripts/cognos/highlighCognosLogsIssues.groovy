def scriptDescriptor = {
	discoverable
	parameters {
		logsFolder
		numberOfLastLines {
			optional
			description = 'limites number of lines in output'
		}
	}	
}

ac.ant.fileset( dir: script.parameters.logsFolder ) { include( name: "**/cogserver.log" ) }.iterator().toList().collect{ it.file }.each { logFile ->
	logFile.readLines()[ -script.parameters.numberOfLastLines .. -1 ].findAll{ it.contains( 'Failure' ) }.each {
		script.log.info "${logFile.absolutePath} ==> ${it}"
	}
}
