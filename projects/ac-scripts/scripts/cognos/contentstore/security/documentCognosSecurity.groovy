import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = ''
}

dsl.cognos.play {
	eachObject ( searchPath: 'CAMID(":")' + "/role", properties: [PropEnum.members ] ) {
		println it.searchPath.value
		it.members.value.each {  
			println "\t${find ( searchPath: it.searchPath.value ).defaultName.value}"
		}
	}
}

return
