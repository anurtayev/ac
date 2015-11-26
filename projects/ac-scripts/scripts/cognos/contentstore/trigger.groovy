import com.cognos.developer.schemas.bibus._3.*
import util.*

def scriptDescriptor = {
	parameters {
		trigger
	}
}

Cognos.session( 
	contentManagerUrl: contentstore_GetActiveContentManagerUrl( 
		hosts: config.contentManager.hosts, 
		urlTemplate: config.contentManager.urlTemplate 
	), 
	namespace: config.domainCredentials.cognosNamespace,
	usr: config.domainCredentials.usr,
	pwd: config.domainCredentials.pwd,
) {
	runTrigger( trigger: parameters.trigger )
	log.info "PROCESS COMPLETED SUCCESSFULLY"	// magic words for ETL
}
