import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	discoverable
	description = 'Deploys folders or packages from current into specified target environment. No non container objects allowed.'
	tags = [ 'administration', 'deployment' ]
	
	parameters {
		containersSearchPathArray
		targetEnvironment
		sourceEnvironment {
			description = 'current environment assumed as a source if this argument is missing' 
			optional 
		}
	}
}

def deploymentName = script.signature 

// ========= SOURCE - current environment
dsl.cognos.play { 
	assert script.parameters.containersSearchPathArray.every { object -> object.isContainer }, 'only folders or packages can be deployed using this script'

	// build a DepoloymentOptions
	def asynchReply = runExport deploymentName: deploymentName, searchPathArray: script.parameters.containersSearchPathArray
	script.log.debug asynchReply.status
	script.log.info "export finised, name: ${deploymentName}"
	
	downloadDeploymentArchive deploymentName: deploymentName, todir: ac.config.system.folders.temp
	script.log.info 'downloaded deployment archive from current environment'
}

//return

// ========= DESTINATION
def targetCognos = dsl.getCognosInstance.call( script.parameters.targetEnvironment )
targetCognos.play {
	
	uploadDeploymentArchive archiveFile: "${ac.config.system.folders.temp}/${deploymentName}.zip"
	script.log.info 'uploaded deployment archive into target environment'
	
	// backup affected folders
	def existingFodlersInTargetEnvironment = script.parameters.containersSearchPathArray.findAll { exists searchPath: it } 
	if ( existingFodlersInTargetEnvironment ) {
		runExport deploymentName: "${deploymentName}_backup", searchPathArray: existingFodlersInTargetEnvironment
		script.log.info "affected existing containers backup is finished"
	}

	// always delete a container in target environment
	delete objects: existingFodlersInTargetEnvironment
	script.log.info 'deleted folders and/or packages in target environment'

	// import objects
	runImport deploymentArchive: deploymentName
	script.log.info "import finished in target environment"

}
