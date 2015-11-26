package ac.processor

import groovy.util.logging.Slf4j

import org.codehaus.groovy.control.CompilerConfiguration
import org.springframework.stereotype.Service

/**
 * AC script processor. Yields a Script instance with properties populated 
 * according to script definition.
 * @author nurtai
 *
 */
@Slf4j
@Service
class ScriptProcessorService implements Serializable {
	
	private static final long serialVersionUID = 1L

	private Map<String, Object> getDelegate() {
		Map<String, Object> map = [:]
		map
	}

	private getShell() {
		new GroovyShell(this.class.classLoader, new Binding(), \
			new CompilerConfiguration(scriptBaseClass:DelegatingScript.name))
	}

	private renderScript(text) {
		ScriptDsl scriptDsl = new ScriptDsl()
		DelegatingScript scriptSpec = shell.parse(text)
		scriptSpec.setDelegate(scriptDsl)
		scriptSpec.run()
		scriptDsl.script
	}

	Script parse(String text) {
		Script script = renderScript(text)
		script.execute.delegate = delegate
		script.execute.resolveStrategy = Closure.DELEGATE_ONLY
		script
	}
}
