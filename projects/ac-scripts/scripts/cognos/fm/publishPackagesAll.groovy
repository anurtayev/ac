import groovy.xml.MarkupBuilder

def scriptDescriptor = {
	discoverable
	description = 'Calls CreateActionLogs for all CPF files found in subfolders in specified folder'
	tags = [
		'Framework Manager',
		'package'
	]

	parameters {
		folder
		target
	}
}

ac.ant.fileset( dir: script.parameters.folder ) { include( name: "**/*.cpf" ) }.iterator().toList().collect{ it.file }.each { cpfFile ->
	script.log.info "===> processing ${cpfFile.name}"
	ac.scripts.cognos.fm.publishPackages( [ model: cpfFile, target: script.parameters.target ] )
}
