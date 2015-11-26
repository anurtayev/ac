import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	discoverable

	parameters {
		cognosInstance { optional }
		searchPathArray
	}
}

def getCognosInstance() {
	script.parameters.cognosInstance ?: dsl.cognos
}

cognosInstance.play {
	def adHocFolders = script.parameters.searchPathArray.collect { folder ->
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
