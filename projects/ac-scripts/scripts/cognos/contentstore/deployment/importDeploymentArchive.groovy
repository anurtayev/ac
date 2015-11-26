import com.cognos.developer.schemas.bibus._3.*
import ac.sys.cognos.*

def scriptDescriptor = {
	description = 'Deploys deployment archive from file system into Cognos instance provided'
	tags = [
		'administration',
		'deployment'
	]

	parameters {
		cognosInstance
		deploymentArchive { description = 'path to deployment archive zip file on file system' }
		targetFolder {
			description = 'folder where content will be deployed'
			optional
		}
	}
}

script.parameters.cognosInstance.play {

	uploadDeploymentArchive archiveFile: script.parameters.deploymentArchive
	script.log.info "uploaded ${script.parameters.deploymentArchive} into ${script.parameters.cognosInstance.activeCmUrl.host}"

	def cognosDeploymentName = new File( script.parameters.deploymentArchive ).name - '.zip'

	// change target folder
	def deploymentOptionsValues
	if ( script.parameters.targetFolder ) {
		deploymentOptionsValues = getDeploymentOptionsValuesFromDeploymentArchive( deploymentArchive: cognosDeploymentName )
		script.log.debug "deploymentOptionsValues before - ${deploymentOptionsValues}"
		deploymentOptionsValues.values().each {
			it.parent = script.parameters.targetFolder
		}
		script.log.debug "deploymentOptionsValues after - ${deploymentOptionsValues}"

		// create target folder
		createPath path: script.parameters.targetFolder
	}

	runImport deploymentArchive: cognosDeploymentName, deploymentOptionsValues: deploymentOptionsValues
}
