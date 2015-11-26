import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = { parameters { namespace } }

dsl.cognos.play {
	findAll( searchPath: script.parameters.namespace + "/descendant::account", properties: [PropEnum.modificationTime]).findAll{ account ->
		account.modificationTime.value
	}
}
