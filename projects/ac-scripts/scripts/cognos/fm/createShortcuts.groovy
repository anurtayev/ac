import groovy.xml.MarkupBuilder

def scriptDescriptor = {
	discoverable
	description = 'Creates action log to apply shortcust to FM model'
	tags = ['Framework Manager']

	parameters {
		cpf { description = 'File instance or absolute path of CPF file' }

		inputFile {  description = 
"""
CSV file formatted like this:

namespace,querySubject,queryItem,joinToQuerySubject,joinToQueryItem,shortcut
DATABASE,CHANGE_REQUEST,ASSIGNED_TO,SYS_USER,SYS_ID,SYS_USER.CHANGE_REQUEST.ASSIGNED_TO
DATABASE,CHANGE_REQUEST,ASSIGNMENT_GROUP,SYS_USER_GROUP,SYS_ID,SYS_USER_GROUP.CHANGE_REQUEST.ASSIGNMENT_GROUP
DATABASE,CHANGE_REQUEST,CLOSED_BY,SYS_USER,SYS_ID,SYS_USER.CHANGE_REQUEST.CLOSED_BY
DATABASE,CHANGE_REQUEST,CMDB_CI,CMDB_CI,SYS_ID,CMDB_CI.CHANGE_REQUEST.CMDB_CI

"""
		}
	}
}

ac.ant.delete {
	fileset( dir: "c:/temp" ) { exclude( name: "ac.log" ) }
}

def project = ac.scripts.cognos.fm.getProject( [ cpf: script.parameters.cpf ] )
def actionLog = new File( "${ac.config.system.folders.temp}/${project.name}_${script.signature}_session-log.xml" )
def actions = ac.scripts.cognos.fm.getActions( [ inputFile: script.parameters.inputFile ] )
def actionSequence = 1

actionLog.withWriter { writer ->
	def xml = new MarkupBuilder( writer )
	xml.bmtactionlog() {
		transaction( seq: 1 ) {
			action( seq: actionSequence++, type: 'SetActiveLocale' ) {
				inputparams {
					param( seq: 1, type: 'i18nstring' ) { value project.defaultLocale }
				}
			}

			actions.each { actionExpando ->
				action( seq: actionSequence++, type: 'CreateShortcut' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) { value 'NULL' }

						param( seq: 2, type: 'handle' ) {
							mappingpath 'querySubject'
							value "[${actionExpando.namespace}].[${actionExpando.joinToQuerySubject}]"
						}
					}
				}

				def shortcutDefaultName = "[${actionExpando.namespace}].[Shortcut to ${actionExpando.joinToQuerySubject}]"

				action( seq: actionSequence++, type: 'AddProperty' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'shortcut'
							value shortcutDefaultName
						}
						param( seq: 2, type: 'i18nstring' ) { value 'treatAs' }
						param( seq: 3, type: 'i18nstring' ) { value 'alias' }
						param( seq: 4, type: 'integer' ) { value 1 }
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'shortcut/name'
							value "/O/name[0]/O/${shortcutDefaultName}"
						}
						param( seq: 2, type: 'i18nstring' ) { value actionExpando.shortcut }
					}
				}
			}
		}
	}
}

ac.scripts.cognos.fm.createScriptPlayerBatchFile ( [ cpf: script.parameters.cpf, actionLog: actionLog ] )
