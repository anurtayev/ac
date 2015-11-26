import com.cognos.developer.schemas.bibus._3.*

def objectTypes = ['report', 'query', 'analysis']
def numberOfObjectsProcessed = 0

dsl.cognos.play {
	findAll( searchPath: buildSearchPath( targetPath: config.ldap.namespaceCamid, objectTypes: ['account']), properties: [PropEnum.creationTime, PropEnum.modificationTime]).findAll{ it.creationTime.value }.each{ user ->

		script.log.debug user.defaultName.value
		
		def userObjectsWithPackageUnavailable = findAll( searchPath: buildSearchPath( targetPath: user.searchPath.value, objectTypes: objectTypes), properties: [PropEnum.metadataModelPackage]).findAll{ ! it.metadataModelPackage.value }
		
		if (!userObjectsWithPackageUnavailable) return;

		script.log.info '=====>'
		script.log.info user.defaultName.value

		userObjectsWithPackageUnavailable.each { object ->
			script.log.info '---->'
			script.log.info object.searchPath.value

			def modelPackagePath = ac.scripts.cognos.contentstore.getModelPackagePath( searchPath: object.searchPath.value )
			if (!modelPackagePath) {
				script.log.warn "model package is missing. object: ${object.searchPath.value}, user: ${user.defaultName.value}, user searchPath: ${user.searchPath.value}"
				return
			}

			script.log.info "model package: ${modelPackagePath}"

			def existingPackages = ac.scripts.cognos.contentstore.findAllPackagesWithSameName( packagePath: modelPackagePath, searchScopePath: "/content/folder[@name='Americas']" )
			existingPackages.each {
				script.log.info "found package: ${object.searchPath.value}"
			}
			
			if ( existingPackages.size() == 1 ) {
				object.metadataModelPackage.value = existingPackages
				//update( objects: [object])
				script.log.info "invalid package for ${object.searchPath.value} was changed to ${existingPackages[ 0 ].searchPath.value}"
			} else {
				script.log.warn "packages found: ${existingPackages.size()}. object: ${object.searchPath.value}, model package: ${modelPackagePath}"
			}

			numberOfObjectsProcessed ++
		}
	}
}

script.log.info "number of objects processed: ${numberOfObjectsProcessed}"

return

