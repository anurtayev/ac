package ac.processor

import groovy.transform.Canonical

/**
 * Models AC scripts
 * @author nurtai
 *
 */
@Canonical
class Script {

	boolean hidden = false

	String description = ''

	List<String> tags = []

	List<Parameter> parameters = []

	Closure execute = { }

}
