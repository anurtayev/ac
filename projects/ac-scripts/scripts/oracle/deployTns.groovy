def scriptDescriptor = {
	discoverable
	description = 'Wipes out all files/folders in network/admin and copies tnsnames.ora file.'
}

def tnsFile = ac.resources.oracle.'tnsnames.ora'

dsl.cognos.play {
	( contentManagers + dispatchers ).each { server ->
		ac.ant.sshexec (
				host: server.host,
				username: server.accounts.cognos.username,
				password: server.accounts.cognos.password,
				trust: true,
				failonerror: false,
				command: "rm -r ${server.folders.oracle.networkAdmin}/*"
				)
		script.log.info "deleted all files/folders in ${server.host}:${server.folders.oracle.networkAdmin}"

		ac.ant.scp (
				localFile: tnsFile.absolutePath,
				remoteTodir: "${server.accounts.cognos.username}:${server.accounts.cognos.password}@${server.host}:${server.folders.oracle.networkAdmin}",
				trust: true
				)
		script.log.info "copied ${tnsFile.absolutePath} into ${server.host}:${server.folders.oracle.networkAdmin}"
	}
}

return