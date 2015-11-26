package ac.processor

import groovy.transform.Immutable

/**
 * Models script parameter.
 * @author nurtai
 *
 */
@Immutable
class Parameter {
	
	String name
	
	String title = ''

	String description = ''
	
	boolean optional = false
	
	String defaultValue = ''
	
}
