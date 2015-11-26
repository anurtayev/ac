import com.cognos.developer.schemas.bibus._3.*

def searchPath = "/content/folder[@name='Ecuador']"
def objectTypes = ['report', 'query', 'analysis']
def numberOfObjectsProcessed = 0

dsl.cognos.play {
	findAll( searchPath: buildSearchPath( targetPath: searchPath, objectTypes: objectTypes ), properties: [
		PropEnum.metadataModelPackage,
		PropEnum.packageBase
	]).findAll { 
		it.searchPath.value.elements.every{it.type != 'package'}
	}.findAll { 
		it.metadataModelPackage.value[0].searchPath.value.elements[1].name != 'Ecuador'
	}.each{
		script.log.info '=====>'
		script.log.info it.searchPath.value
		script.log.info it.metadataModelPackage.value[0].searchPath.value
		
		def newPackage = it.metadataModelPackage.value[0].searchPath.value.replaceFirst( 'Americas', 'Ecuador' )
		def newPackageArray = findAll( searchPath: newPackage )
		assert newPackageArray.size() == 1
		
		it.metadataModelPackage.value =  newPackageArray
		update( objects: [ it ])
		script.log.info it.metadataModelPackage.value[0].searchPath.value
		numberOfObjectsProcessed ++
	}
}

script.log.info "number of objects processed: ${numberOfObjectsProcessed}"

return

