import groovy.xml.MarkupBuilder
import static com.xlson.groovycsv.CsvParser.parseCsv

def scriptDescriptor = {
	discoverable
	description = 'Creates action log to apply shortcust to FM model'
	tags = ['Framework Manager']

	parameters {
		cpf { description = 'File instance or absolute path of CPF file' }
		inputFile {  description = 'csv: namespace,querySubject,queryItem' }
	}
}

ac.ant.delete {
	fileset( dir: "c:/temp" ) { exclude( name: "ac.log" ) }
}

def project = ac.scripts.cognos.fm.getProject( [ cpf: script.parameters.cpf ] )
def actionLog = new File( "${ac.config.system.folders.temp}/${project.name}_${script.signature}_session-log.xml" )
def actions = parseCsv( new File( script.parameters.inputFile ).newReader() )

actionLog.withWriter { writer ->
	def actionSequence = 1
	def xml = new MarkupBuilder( writer )
	xml.bmtactionlog() {
		transaction( seq: 1 ) {
			action( seq: 1, type: 'SetActiveLocale' ) {
				inputparams {
					param( seq: 1, type: 'i18nstring' ) { value project.defaultLocale }
				}
			}
		}

		actions.each { actionEntry ->
			transaction( seq: actionSequence++ ) {
				action( seq: actionSequence++, type: 'Delete' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'queryItem'
							value "[${actionEntry.namespace}].[${actionEntry.querySubject}].[${actionEntry.queryItem}]"
						}
					}
				}
			}
		}
	}
}

ac.scripts.cognos.fm.createScriptPlayerBatchFile ( [ cpf: script.parameters.cpf, actionLog: actionLog ] )
