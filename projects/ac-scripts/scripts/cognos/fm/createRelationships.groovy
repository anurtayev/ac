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

		actions.each { actionExpando ->
			transaction( seq: actionSequence++ ) {
				action( seq: actionSequence++, type: 'Create' ) {
					inputparams {
						param( seq: 1, type: 'integer' ) { value 3 }

						param( seq: 2, type: 'handle' ) {
							mappingpath 'project'
							value '[]'
						}
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/left/refobj'
							value "/O/left[0]/refobj[0]/O/[SNOW].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value "[${actionExpando.namespace}].[${actionExpando.querySubject}]" }
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/left/mincard'
							value "/O/left[0]/mincard[0]/O/[SNOW].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value 'one' }
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/left/maxcard'
							value "/O/left[0]/maxcard[0]/O/[SNOW].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value 'one' }
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/right/refobj'
							value "/O/right[0]/refobj[0]/O/[SNOW].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value "[${actionExpando.namespace}].[${actionExpando.shortcut}]" }
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/right/mincard'
							value "/O/right[0]/mincard[0]/O/[DATABASE].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value 'one' }
					}
				}

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/right/maxcard'
							value "/O/right[0]/maxcard[0]/O/[DATABASE].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value 'one' }
					}
				}

				def ralationshipName = "${actionExpando.querySubject} <--> ${actionExpando.shortcut}"

				action( seq: actionSequence++, type: 'Modify' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/name'
							value "/O/name[0]/O/[DATABASE].[New Relationship]"
						}
						param( seq: 2, type: 'i18nstring' ) { value ralationshipName }
					}
				}

				action( seq: actionSequence++, type: 'ModifyComplex' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship/expression'
							value "/O/expression[0]/O/[DATABASE].[${ralationshipName}]"
						}
						param( seq: 2, type: 'i18nstring' ) {  value "<refobj>[${actionExpando.namespace}].[${actionExpando.querySubject}].[${actionExpando.queryItem}]</refobj>=<refobjViaShortcut><refobj>[${actionExpando.namespace}].[${actionExpando.shortcut}]</refobj><refobj>[${actionExpando.namespace}].[${actionExpando.joinToQuerySubject}].[${actionExpando.joinToQueryItem}]</refobj></refobjViaShortcut>"  }
					}
				}
				
				action( seq: actionSequence++, type: 'EvaluateObject' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'relationship'
							value "[DATABASE].[${ralationshipName}]"
						}
					}
				}
			}
		}
	}
}

ac.scripts.cognos.fm.createScriptPlayerBatchFile ( [ cpf: script.parameters.cpf, actionLog: actionLog ] )
