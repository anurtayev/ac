import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	parameters {
		roles
		capabilitiesModel
	}
}

def capabilities = script.parameters.capabilitiesModel.findAll { capability ->
	capability.policies.value.any { policy ->
		policy.permissions.any { permission ->
			script.parameters.roles.any { role ->
				role.searchPath.value == policy.securityObject.searchPath.value
			} &&
			permission.name == 'execute' &&
			permission.access == AccessEnum.grant
		}
	}
}.collect {
	it.userCapability.value.value
}.unique()

// check for System Administrator
if ( script.parameters.roles.any{it.searchPath.value == 'CAMID("::System Administrators")'} ) {
	capabilities += 'canUseAdministrationPortal'
}

return capabilities