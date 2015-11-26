import com.cognos.developer.schemas.bibus._3.*

def capabilities
dsl.cognos.play {
	capabilities = findAll( searchPath: "/capability/descendant::*", properties: [
		PropEnum.policies,
		PropEnum.permissions,
		PropEnum.userCapability ]
	)
}

return capabilities