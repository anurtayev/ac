def scriptDescriptor = { discoverable }

dsl.cognos.play {
	gateways.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				failonerror: false,
				usepty: true,
				command: "sudo ${server.folders.apache.bin}/apachectl -k stop"
				)
		script.log.info "stopped gateway ${server.host}"
	}

	dispatchers.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				outputproperty: 'output',
				command: "source /etc/profile; ${server.folders.cognos.bin64}/cogconfig.sh -stop; cat ${server.folders.cognos.logs}/cogconfig_response.csv"
				)
		def output = ac.ant.project.properties.'output'
		script.log.info "stopped content manager ${server.host}, cogconfig_response.csv: ${output}"
	}

	contentManagers.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				outputproperty: 'output',
				command: "source /etc/profile; ${server.folders.cognos.bin64}/cogconfig.sh -stop; cat ${server.folders.cognos.logs}/cogconfig_response.csv"
				)
		def output = ac.ant.project.properties.'output'
		script.log.info "stopped content manager ${server.host}, cogconfig_response.csv: ${output}"
	}
}