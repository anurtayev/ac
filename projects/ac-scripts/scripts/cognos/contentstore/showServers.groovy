import com.cognos.developer.schemas.bibus._3. *
import util. *

log.info 'Content managers:'
config.contentManager.hosts.each {
	def urlStr = String.format( config.contentManager.urlTemplate, it )
	log.debug urlStr
	log.info "${urlStr} is in ${urlStr.toURL().text.indexOf('standby') < 0 ? 'Active' : 'Standby'} mode"
}

log.info 'Dispatchers:'
config.dispatcher.hosts.each {
	def urlStr = String.format( config.dispatcher.urlTemplate, it )
	log.debug urlStr
	log.info "${urlStr} is ${urlStr.toURL().text.indexOf('Log on') > 0 ? 'Running' : 'Unexpected result'}"
}
