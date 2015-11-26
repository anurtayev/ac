import util.*
import com.cognos.developer.schemas.bibus._3.*

new MarkupBuilder( getOutput( it.name, 'xml' ) ).security() {
	dsl.cognos.play {
		traverse( searchPath: "/content", properties: [ PropEnum.policies, PropEnum.defaultName, PropEnum.parent ] ) { objectVal ->
			log.debug searchPathToString( objectVal.searchPath.value )
			object(type: objectVal.class.name.tokenize('.').last().toLowerCase(), path: searchPathToString( objectVal.searchPath.value ), searchPath: objectVal.searchPath.value) {
				if (find(searchPath: objectVal.parent.value[0].searchPath.value, properties: [PropEnum.policies] )[ 0 ].policies != objectVal.policies) {
					objectVal.policies.value.each { policyVal ->
						policy( securityObject: policyVal.securityObject.searchPath.value ) {
							policyVal.permissions.each { permissionVal ->
								permission(name: permissionVal.name)
							}
						}
					}
				}
			}
		}
	}
}
