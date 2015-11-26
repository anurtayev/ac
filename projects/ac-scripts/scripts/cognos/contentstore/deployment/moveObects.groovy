import com.cognos.developer.schemas.bibus._3.*
import ac.sys.cognos.*

def scriptDescriptor = {
	discoverable
	description = ''

	parameters {
		source { description = "/content/folder[@name='_v143']" }
		target { description = "/content/folder[@name!='_v143']" }
		objectTypes
	}
}

def sourceSearchPath = SearchPathUtils.buildSearchPath( script.parameters.source, script.parameters.objectTypes )

dsl.cognos.play {
	def objectsMap = [ : ]
	eachObject( searchPath: sourceSearchPath, properties: [PropEnum.modificationTime]) { object ->
		def existingObject = objectsMap.get( object.defaultName.value )

		if ( existingObject ) {
			if ( object.searchPath.value.type == existingObject.searchPath.value.type ) {

				if ( object.modificationTime.value > existingObject.modificationTime.value ) {
					objectsMap.put( object.defaultName.value, object )
					delete objects: [existingObject]
					script.log.info "deleted ${existingObject.searchPath.value} with timestamp ${existingObject.modificationTime.value.time} because it is older than ${object.searchPath.value} with timestamp ${object.modificationTime.value.time}"
				} else {
					delete objects: [object]
					script.log.info "deleted ${object.searchPath.value} with timestamp ${object.modificationTime.value.time} because it is older than ${existingObject.searchPath.value} with timestamp ${existingObject.modificationTime.value.time}"
				}
			}
		} else {
			objectsMap.put( object.defaultName.value, object )
		}
	}

	script.log.info "${objectsMap.keySet().size()} unique objects in ${sourceSearchPath}"

	objectsMap.values().each { object ->
		def existingObjects = findAll( searchPath: "${script.parameters.target}//${object.searchPath.value.elements.last()}", properties: [PropEnum.parent])
		if ( existingObjects.size() == 1 ) {
			def targetPath = existingObjects[ 0 ].parent.value[ 0 ].searchPath.value
			move objects: [object], targetPath: targetPath
			script.log.info "moved ${object.searchPath.value} into ${targetPath}"
		} else {
			script.log.warn "can't move ${object.searchPath.value} - number of objects with same name is ${existingObjects.size()}"
		}
	}
}
