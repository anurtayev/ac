import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = {
	description = 'Extracts model information from object specification. Can be used to figure out what the current model should be in case if package information is missing.'

	parameters { 
		packagePath 
		searchScopePath
	} 
}

def existingPackages 
dsl.cognos.play {
	def pckg = script.parameters.packagePath.elements.find{ it.type == 'package' }
	assert pckg, 'No package found in object model path'
	existingPackages = findAll( searchPath: "${script.parameters.searchScopePath}//${pckg}" )
}

existingPackages 