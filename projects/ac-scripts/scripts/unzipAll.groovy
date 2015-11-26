description 'Extracts *.zip from source into destination folder'

tags { upgrade }

parameters {
	source
	target
}

run {
	for ( f in ac.ant.fileScanner { fileset( dir: script.parameters.source ) { include(name: "**/*.zip")
		} } ) {
		script.log.info "unzipping ${f.absolutePath} into ${script.parameters.target}"
		ac.ant.unzip src: f.absolutePath, dest: "${script.parameters.target}/${f.name}"
	}
}