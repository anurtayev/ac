import groovy.xml.MarkupBuilder

def scriptDescriptor = {
	discoverable
	description = 'Creates action log to publish with BMTScriptPlayer for all packages defined in FM model'
	tags = ['Framework Manager', 'package']

	parameters {
		cpf { description = 'File instance or absolute path to CPF file' }

		target { description = 'Path in Public Folders where to publish packages' }
	}
}

def project = ac.scripts.cognos.fm.getProject( [ model: script.parameters.project ] )
def actionLog = new File( "${ac.config.system.folders.temp}/${project.name}_${script.signature}_session-log.xml" )
def packages = project.packages.package.collect{ it.name.find{ it.@locale = project.defaultLocale } }

actionLog.withWriter { writer ->
	def xml = new MarkupBuilder( writer )
	xml.bmtactionlog() {
		transaction( seq: 1 ) {
			action( seq: 1, type: 'SetActiveLocale' ) {
				inputparams {
					param( seq: 1, type: 'i18nstring' ) { value project.defaultLocale }
				}
			}

			def seq = 2
			packages.each { packageName ->
				action( seq: seq++, type: 'Publish' ) {
					inputparams {
						param( seq: 1, type: 'handle' ) {
							mappingpath 'package'
							value "[].[packages].[${packageName}]"
						}

						param( seq: 2, type: 'integer' ) { value 2 }

						param( seq: 3, type: 'i18nstring' ) { value script.parameters.target }

						param( seq: 4, type: 'i18nstring' ) { value packageName }

						param( seq: 5, type: 'integer' ) { value 1 }

						param( seq: 6, type: 'integer' ) { value 1 }

						param( seq: 7, type: 'integer' ) { value '-1' }
					}
				}
			}
		}
	}
}

ac.scripts.cognos.fm.createScriptPlayerBatchFile ( [ project: project, actionLog: actionLog ] )
