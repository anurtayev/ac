package ac.processor

import groovy.util.logging.Slf4j

/**
 * Builds list of Parameters defined by script. Being called by Script.
 * @author nurtai
 *
 */
@Slf4j
@SuppressWarnings('NoDef')
class ParametersBuilder  {

	List<Parameter> parameters = []

	def invokeMethod(String name, Object args) {
		log.debug name
		log.debug args[0]
		Parameter p=new Parameter(name:name, *:args[0])
		log.debug p.toString()
		parameters << p
	}
	
	def propertyMissing(String name) {
		parameters << new Parameter(name:name)
	}
}
