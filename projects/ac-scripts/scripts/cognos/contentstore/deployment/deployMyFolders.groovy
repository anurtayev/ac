import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	parameters {
		deploymentRules { description = '[ MyFolderObjectSearchPath: PublicFoldersPath ]' }
	}
}

dsl.cognos.play {
	script.parameters.deploymentRules.each { objectSearchPath, targetPath ->
		copy objects: findAll( searchPath: objectSearchPath ), targetPath: targetPath
		log.info "Successfully copied ${objectSearchPath} to ${targetPath}"
	}
}
