def scriptDescriptor = { discoverable }

dsl.cognos.play {
	contentManagers.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				outputproperty: 'output',
				command: "source /etc/profile; ${server.folders.cognos.bin64}/cogconfig.sh -s; cat ${server.folders.cognos.logs}/cogconfig_response.csv"
				)
		def output = ac.ant.project.properties.'output'
		script.log.info "started content manager ${server.host}, cogconfig_response.csv: ${output}"
	}

	dispatchers.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				outputproperty: 'output',
				command: "source /etc/profile; ${server.folders.cognos.bin64}/cogconfig.sh -s; cat ${server.folders.cognos.logs}/cogconfig_response.csv"
				)
		def output = ac.ant.project.properties.'output'
		script.log.info "started content manager ${server.host}, cogconfig_response.csv: ${output}"
	}

	gateways.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				failonerror: false,
				usepty: true,
				command: "sudo ${server.folders.apache.bin}/apachectl -k start"
				)
		script.log.info "started gateway ${server.host}"
	}
}