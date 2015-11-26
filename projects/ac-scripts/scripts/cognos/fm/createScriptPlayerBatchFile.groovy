import groovy.xml.MarkupBuilder

def scriptDescriptor = {
	discoverable
	description = 'Creates a ScriptPlayer batch file to play action log'
	tags = ['Framework Manager']

	parameters {
		cpf { description = 'File instance or absolute path to CPF file' }

		actionLog { description = 'Action log file or absolute path string' }
	}
}

def cpf = script.parameters.cpf.getClass() == File.class ? script.parameters.cpf : new File( script.parameters.cpf )
script.log.info "cpf: ${cpf.absolutePath}"
def actionLog = script.parameters.actionLog.getClass() == File.class ? script.parameters.actionLog : new File( script.parameters.actionLog )
script.log.info "acitonLog: ${actionLog.absolutePath}"
def batchFile = new File( "${ac.config.system.folders.temp}/${actionLog.name - '.xml'}.bat" )
script.log.info "batchFile: ${batchFile.absolutePath}"

batchFile.withWriter { writer ->
	def printWriter = new PrintWriter( writer )
	printWriter.print '"'
	printWriter.print ac.config.cognos.scriptPlayerExecutable
	printWriter.print '" -m "'
	printWriter.print cpf.absolutePath
	printWriter.print '" -a "'
	printWriter.print actionLog.absolutePath
	printWriter.print '" -s '
	printWriter.print dsl.cognos.config.ldap.namespace
	printWriter.print ' -u '
	printWriter.print dsl.cognos.config.ldap.username
	printWriter.print ' -p '
	printWriter.print dsl.cognos.config.ldap.password
}
