import com.cognos.developer.schemas.bibus._3.*
import util.*

def objectsSearchPath = "/content/folder[@name='Brazil']/folder[@name='Corporate']/folder[@name='Finance']/folder[@name='Management Reports']/report[@name='BIR57GLODSBR02_Management Query by Type of Operation']"

Cognos.session( contentManager: config.contentManager, credentials: config.domainCredentials ) {

	def object = find( searchPath: objectsSearchPath, properties: [ PropEnum.metadataModelPackage ] )
	println object.metadataModelPackage.value
	println object.metadataModelPackage.packageBase.value
	return
	
	if ( !object.metadataModelPackage.value ) {
		def spec = find( searchPath: object.searchPath.value, properties: [ PropEnum.specification ] ).specification.value
		def modelPath = extractModelPath ( spec )
		
		assert modelPath, 'model path not found'
		
		// def modelPathSearchPath = new SearchPath( modelPath )
		def modelPathSearchPath = modelPath
		def pckg = modelPathSearchPath.elements.find{ it.type == 'package' }

		assert pckg, 'No package found in object model path'

		def existingPackages = findAll( searchPath: "/content//${pckg}" )
		
		assert existingPackages.size() == 1, "There must one package matching ${pckg}. Object with missing package is ${object.searchPath.value}"
		
		object.metadataModelPackage.value = existingPackages
		update( objects: [ object ] )
		log.info "invalid package for ${object.searchPath.value} was changed to ${existingPackages[ 0 ].searchPath.value}"
	}
}

private extractModelPath ( spec ) {
	def model
	def xml = new XmlSlurper().parseText( spec )
	def objectType = xml.name()
	switch ( objectType ) {
		case 'CRQReport' :
			model = xml.@srcModel.text()
			break
			
		case 'report' :
			model = xml.modelPath.text()
			break
		
		default :
			assert false, 'model path not found'
	}
	
	model
}
