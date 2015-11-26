package ac.processor

import groovy.util.logging.Slf4j

/**
 * DSL support for AC scripts
 * @author nurtai
 *
 */
@Slf4j
class ScriptDsl {
	
	Script script = new Script()

	void description(desc) {
		script.description = desc
	}

	void tags(Closure c) {
		c.delegate = new TagsBuilder()
		c.resolveStrategy = Closure.DELEGATE_ONLY
		c()
		script.tags = c.delegate.tags
	}

	void parameters(Closure c) {
		c.delegate = new ParametersBuilder()
		c.resolveStrategy = Closure.DELEGATE_ONLY
		c()
		script.parameters = c.delegate.parameters
	}

	void run(Closure runClosure) {
		script.execute = runClosure
	}
}
