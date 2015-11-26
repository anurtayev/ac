package ac.scriptrepository

import groovy.io.FileType
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Provides low level access to file system. 
 * Returns repository content.
 * @author nurtai
 *
 */
@Component
@ConfigurationProperties
@Slf4j
@SuppressWarnings('JavaIoPackageAccess')
class FileScriptRepository {

	private final static SCRIPT_EXT = '.groovy'

	String repositoryPath

	File getRepoDir() {
		new File(repositoryPath)
	}

	String getScript(String id) {
		log.debug "requested script id: ${id}"
		"${new File(repoDir, id).text}"
	}

	List<String> list() {
		List<String> files=[]
		repoDir.eachFileRecurse(FileType.FILES) {
			if (it.canonicalPath.endsWith(SCRIPT_EXT)) {
				files << (it.canonicalPath - repoDir.canonicalPath).replaceAll('\\\\', '/')
			}
		}
		files
	}

	@PostConstruct
	void handleContextRefresh() {
		log.info "repositoryPath: ${repositoryPath}, canonical path: ${repoDir.canonicalPath}"
	}
}
