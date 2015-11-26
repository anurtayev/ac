import com.cognos.developer.schemas.bibus._3.*
import sys.cognos.*

def scriptDescriptor = {
	discoverable
	description = 'Recovers FM project file based package content.'

	parameters { packageSearchPath }
}

dsl.cognos.play {
	assert exists( searchPath: script.parameters.packageSearchPath )
	def model = find( searchPath: "${script.parameters.packageSearchPath}/model[@name='model']", properties: [PropEnum.model ])
	
	script.tempFile.call('xml', '').text = model.model.value
}

return