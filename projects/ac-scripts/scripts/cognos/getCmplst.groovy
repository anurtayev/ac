def scriptDescriptor = { discoverable }

def dir = "${tempFolder}/${scriptSignature}"
ac.ant.mkdir dir: dir

dsl.cognos.play {
	servers.each { server ->
		def serverLocalDir = "${dir}/${server.host}"
		ac.ant.mkdir dir: serverLocalDir

		ac.ant.scp (
				remoteFile: "${server.accounts.cognos.username}:${server.accounts.cognos.password}@${server.host}:${server.folders.cognos.cmplst}/cmplst.txt",
				localTodir: serverLocalDir,
				trust: true,
				failonerror: false,
				)
	}
}