import com.cognos.developer.schemas.bibus._3.*
import ac.sys.cognos.*

def scriptDescriptor = {
	discoverable
	description = 'Deploys deployment archives from file system folder into Cognos instance provided'
	tags = [ 'administration', 'deployment' ]
	
	parameters {
		deploymentArchivesFolder {
			description = 'path to folder on file system that contains one or more deployment archives'
		}
		targetFolder {
			description = 'folder where content will be deployed'
			optional
		}
	}
}

ac.ant.fileset( dir: script.parameters.deploymentArchivesFolder ) { include( name: "**/*.zip" ) }.iterator().toList().collect{ it.file }.each { archiveFile ->
	script.log.info "processing: ${archiveFile.name}"
	ac.scripts.cognos.contentstore.deployment.ImportDeploymentArchive ( [
		cognosInstance: dsl.cognos,
		deploymentArchive: archiveFile.absolutePath,
		targetFolder: script.parameters.targetFolder
	] )
}
