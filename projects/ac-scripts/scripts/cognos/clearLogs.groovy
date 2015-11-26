def scriptDescriptor = {
	discoverable
	description = 'Deletes all files and subfolders from <cognos_install>/logs folder from all Cognos servers.'
}

dsl.cognos.play {
	servers.each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				failonerror: false,
				command: "rm -r ${server.folders.cognos.logs}/*"
				)
		log.info "deleted log files on ${server.host}"
	}
}

return