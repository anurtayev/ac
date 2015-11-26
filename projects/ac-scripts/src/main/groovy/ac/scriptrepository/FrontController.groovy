package ac.scriptrepository

import groovy.util.logging.Slf4j

import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*
import org.springframework.context.annotation.*
import org.springframework.stereotype.*
import org.springframework.util.AntPathMatcher
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping

/**
 * Scripts repository micro service Rest controller. 
 * @author nurtai
 *
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Slf4j
@SuppressWarnings('NoWildcardImports')
class FrontController {

	@Autowired
	FileScriptRepository scriptRepository

	@SuppressWarnings('NoDef')
	@RequestMapping('/getScript/**')
	String getScript(HttpServletRequest request) {
		def id = new AntPathMatcher().extractPathWithinPattern(\
			request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE), \
			request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
		scriptRepository.getScript(id)
	}

	@RequestMapping('/list')
	List<String> list() {
		scriptRepository.list()
	}

	static void main(args) {
		SpringApplication.run(FrontController, args)
	}
}
