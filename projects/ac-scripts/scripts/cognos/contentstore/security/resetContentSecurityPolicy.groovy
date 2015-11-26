import sys.dsl.*
import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Sets default security policy for entire content. Default policy is <read>, <execute>, <traverse> for All Authenticated Users. It is a good idea to reset permissions manually on /content with "Delete the access permissions of all child entries" flag set.'
}

dsl.cognos.play {
	setPolicies (
		object: find( searchPath: '/content' ), 
		policies: [ 
			newPolicy( 
				securityObject: find( searchPath : 'CAMID(":All Authenticated Users")' ), 
				permissions: [
					'read',
					'execute',
					'traverse'
				]
			) 
		]
	)
	script.log.info "/content security set to read, execute and traverse for All Authenticated Users"
	
	eachObject( searchPath: '/content/descendant::folder' ) { object ->
		setPolicies object: object, policies: []
	}
	script.log.info "deleted security policies for all descendant folders of /content"
}

return
