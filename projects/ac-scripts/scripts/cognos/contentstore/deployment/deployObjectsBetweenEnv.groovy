import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	discoverable
	description = 'Deploys objects from current into specified target environment. No folders or packages allowed.'

	parameters {
		searchPathArray { description = 'Array of SearchPaths of objects to be deployed' }
		targetEnvironment
		sourceEnvironment {
			description = 'current environment assumed as a source if this argument is missing'
			optional
		}
	}
}

def getDeploymentName() {
	script.signature
}
def getDeploymentTempFolderSearchPath() {
	"/content/folder[@name='${deploymentName}']"
}

// ========= SOURCE - current environment
dsl.cognos.play {
	assert script.parameters.searchPathArray.every { object -> ! object.isContainer }, 'no folders or packages can be deployed using this script'

	def deploymentTempFolder = createPath  path: deploymentTempFolderSearchPath

	// copy deployment objects to temporary folder
	script.parameters.searchPathArray.each { searchPath ->
		copyRename(
				objects: [find( searchPath: searchPath )],
				targetPath: deploymentTempFolderSearchPath,
				newNames: [searchPath.canonical.replaceAll( '/', '_' ) ])
	}

	// build a DepoloymentOptions
	runExport deploymentName: deploymentName, searchPathArray: [deploymentTempFolderSearchPath]
	script.log.info "export finised, name: ${deploymentName}"

	delete  objects: [deploymentTempFolder]

	downloadDeploymentArchive deploymentName: deploymentName, todir: ac.config.system.folders.temp
	script.log.info 'downloaded deployment archive from current environment'
}

// ========= DESTINATION
def targetCognos = dsl.getCognosInstance.call( script.parameters.targetEnvironment )
targetCognos.play {
	uploadDeploymentArchive archiveFile: "${ac.config.system.folders.temp}/${deploymentName}.zip"
	script.log.info 'uploaded deployment archive into target environment'

	// calculate folders affected by new import
	def affectedFolders = []
	script.parameters.searchPathArray.each { path ->
		if ( affectedFolders.every { backupFolder -> ! path.isDescendantOf( backupFolder ) } ) {
			affectedFolders += path.parent
		}
	}

	// backup affected folders
	runExport deploymentName: "${deploymentName}_backup", searchPathArray: affectedFolders
	script.log.info "affected folders backup is finished"

	// calculate Ad Hoc folders in destination environment
	def adHocFolders = affectedFolders.collect { folder ->
		findAll( searchPath: "${ folder }/descendant::folder[@name='Ad Hoc Reports']" ).collect { adHocFolder ->
			adHocFolder.searchPath.value
		}
	}.flatten()

	def adHocFoldersArchiveName = "${deploymentName}_adHocFolders_archive"

	// backup Ad Hoc folders
	if ( adHocFolders ) {
		runExport deploymentName: "${deploymentName}_adHocFoldersbackup", searchPathArray: adHocFolders, deploymentArchive: adHocFoldersArchiveName
		log.info "ad hoc folders backup is finished"
	}

	// import objects
	runImport deploymentArchive: deploymentName
	script.log.info "import finished in target environment"

	// move objects to their destinations
	script.parameters.searchPathArray.each { searchPath ->
		copyRename objects: [
			find( searchPath: "${ deploymentTempFolderSearchPath }/${ searchPath.type }[@name='${ searchPath.canonical.replaceAll( '/', '_' ) }']" )
		],
		targetPath: searchPath.parent,
		newNames: [searchPath.name]
	}

	// delete temporary folder
	delete  objects: [find( searchPath: deploymentTempFolderSearchPath )]

	// restore Ad Hoc folders
	if ( adHocFolders ) {
		runImport deploymentName: "${deploymentName}_adHocFoldersrestore", deploymentArchive: adHocFoldersArchiveName
		script.log.info "ad hoc folders restore is finished"
	}
}
