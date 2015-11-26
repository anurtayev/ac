def scriptDescriptor = {
	parameters {
		deploymentArchive
		deploymentOptionsValues {
			optional
		}
	}
}

def checkStatus = true

dsl.cognos.play {
	def deploymentOptionsValues = parameters.deploymentOptionsValues ?: getDeploymentOptionsValuesFromDeploymentArchive( deploymentArchive: parameters.deploymentArchive )
	findAll( searchPath: '/content/folder' ).collect{ it.searchPath.value }.each { topLevelFolder ->
		deploymentOptionsValues.keySet().each { archiveItem ->
			if ( deploymentOptionsValues."${archiveItem}".parent.topLevelFolder == topLevelFolder ) {
				checkStatus = false
			}
		}
	}
}

checkStatus