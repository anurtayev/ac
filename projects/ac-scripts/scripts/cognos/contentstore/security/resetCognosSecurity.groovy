import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Empties members lists of all Cognos roles.'
}

dsl.cognos.play {
	eachObject ( searchPath: 'CAMID(":")' + "/role[@name!='System Administrators']" ) {
		update ( objects: [ setMembers( object: it, members: [] ) ] )
		script.log.info "${it.searchPath.value} deleted all members" 
	}
}

return
