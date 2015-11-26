import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	findAll( searchPath: "/content/folder[@name='cleanup']/descendant::folder" ).findAll {
		findAll( 
			searchPath: "${it.searchPath.value}/child::report | ${it.searchPath.value}/child::query | ${it.searchPath.value}/child::analysis | ${it.searchPath.value}/child::folder"
		).size() == 0
	}.each{
		delete objects: [it.searchPath.value]
		script.log.debug "${it.searchPath.value} - deleted"
	}
}