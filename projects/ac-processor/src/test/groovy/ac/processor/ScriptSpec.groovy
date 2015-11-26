package ac.processor

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class ScriptSpec extends Specification {

	def "Script description initialized properly"() {
		given:
		def scriptText = """
		description 'describing script'
		"""
		
		expect:
		new ScriptProcessorService().parse(scriptText).description == 'describing script'
	}

	@SuppressWarnings('UnnecessaryGString')
	def "Script tags initialized properly"() {
		given:
		def scriptText = """
		tags {
			tag2
			tag3
		}
		"""

		expect:
		new ScriptProcessorService().parse(scriptText).tags == ['tag2', 'tag3']
	}

	def "Script parameters initialized properly"() {
		given:
		def scriptText = """
		parameters {
			firstName (optional: true)
			lastName (description : 'last name as per passport', title: 'Last Name',defaultValue : 'nurtai')
		}
		"""

		expect:
		new ScriptProcessorService().parse(scriptText).parameters == [
			new Parameter(name:'firstName', optional:true),
			new Parameter(name:'lastName', description:'last name as per passport', \
				title:'Last Name', defaultValue:'nurtai')
		]
	}
}
