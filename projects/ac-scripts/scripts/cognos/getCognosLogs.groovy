def scriptDescriptor = { discoverable }

def dir = "${ac.config.system.folders.temp}/${script.signature}"
ac.ant.mkdir dir: dir

dsl.cognos.play {
	servers.each { server ->
		def serverLocalDir = "${dir}/${server.host}"
		ac.ant.mkdir dir: serverLocalDir

		ac.ant.scp (
				remoteFile: "${server.accounts.cognos.username}:${server.accounts.cognos.password}@${server.host}:${server.folders.cognos.logs}/*",
				localTodir: serverLocalDir,
				trust: true,
				failonerror: false,
				)
	}
}