def scriptDescriptor = {
	discoverable
}

cognos.currentEnvironmentConfig.servers.each { server ->
	ac.ant.sshexec (
		host: server.host, 
		username: server.accounts.root.username, 
		password: server.accounts.root.password, 
		trust: true,
		failonerror: false,
		command: 'shutdown -r now'
	)
	script.log.info "sent shutdown command to ${server.host}"
}

return