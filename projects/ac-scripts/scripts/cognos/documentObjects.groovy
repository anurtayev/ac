import com.cognos.developer.schemas.bibus._3.*
import util.*

def path = "/content/folder[@name='Ecuador']//folder[not(contains(@name,'Ad Hoc'))]"

cognos.play {
	println "Object"
	eachObject( searchPath: path + '/descendant::report' , properties: [ PropEnum.metadataModelPackage, PropEnum.defaultName ] ) { object ->
		println "${object.defaultName.value},${object.searchPath.value},${object.metadataModelPackage.value[0].searchPath.value}"
	}
}
