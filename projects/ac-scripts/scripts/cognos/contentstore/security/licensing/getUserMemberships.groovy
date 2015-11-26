import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	parameters {
		userSearchPath
		sortedSecurityModel
	}
}

userMemberships = [
	new Group( searchPath: new StringProp( value: 'CAMID("::All Authenticated Users")' ), defaultName: new TokenProp( value: 'All Authenticated Users' ) ),
	new Group( searchPath: new StringProp( value: 'CAMID("::Everyone")' ), defaultName: new TokenProp( value: 'Everyone' ) ),
]

// ===== all containers user is member of
checkUserMembershipInContainer()

// ===== all containers users containers are members of
script.parameters.sortedSecurityModel.each { checkIfContainerShouldBeIncluded( it ) }
script.parameters.sortedSecurityModel.reverseEach { checkIfContainerShouldBeIncluded( it ) }

return userMemberships

def checkUserMembershipInContainer() {
	script.parameters.sortedSecurityModel.each { securityContainer ->
		if ( securityContainer.members.value.any { it.searchPath.value == script.parameters.userSearchPath } )
			userMemberships << securityContainer
	}
}

def checkIfContainerShouldBeIncluded( securityContainer ) {
	if ( securityContainer.members.value.any { member -> userMemberships.any { membership -> membership.searchPath.value == member.searchPath.value } } ) {
		if ( userMemberships.every { membership -> securityContainer.searchPath.value != membership.searchPath.value } ) {
			userMemberships << securityContainer
		}
	}
}
