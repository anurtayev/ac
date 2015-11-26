import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	discoverable

	parameters {
		searchPath
		additionalProperties { optional }
	}
}

dsl.cognos.play {
	eachObject ( searchPath : script.parameters.searchPath, properties: additionalProperties ) { object ->
		script.log.info '=========================================='
		script.log.info "defaultName: ${object.defaultName.value}"
		script.log.info "type: ${object.searchPath.value.type}"
		script.log.info "searchPath: ${object.searchPath.value}"
		script.parameters.additionalProperties.each { property ->
			script.log.info "${property}: ${propertyToString( property )}"
		}
	}
}

return

def getAdditionalProperties() {
	def lst = []
	script.parameters.additionalProperties.each { lst << PropEnum.fromString( it ) }

	lst
}

def propertyToString( property ) {
	property.value
}