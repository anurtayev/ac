package ac.main

import groovy.util.logging.Slf4j

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import com.vaadin.data.Container
import com.vaadin.data.Item
import com.vaadin.data.util.HierarchicalContainer

/**
 * Provides script services for AC GUI
 * @author nurtai
 *
 */
@Service
@Slf4j
@ConfigurationProperties
class ScriptsService implements Serializable {

	private static final long serialVersionUID = 1L
	private static final String STR_CAPTION = 'caption'
	public static final String STR_SCRIPT_ID = 'scriptId'
	private static final String STR_SCRIPTS_TITLE = 'scripts'

	String scriptsAddress
	String getScriptAddress

	private List<String> getAllScripts() {
		new RestTemplate().getForObject(scriptsAddress, List)
	}

	private String put(RepoPath repoPath, HierarchicalContainer container) {
		if (!container.getItem(repoPath.path)) {

			Item item = container.addItem(repoPath.path)
			item.getItemProperty(STR_CAPTION).value = repoPath.root ? STR_SCRIPTS_TITLE : repoPath.name
			item.getItemProperty(STR_SCRIPT_ID).value = repoPath.path

			container.setChildrenAllowed(repoPath.path, repoPath.root || repoPath.folder)
			container.setParent(repoPath.path, repoPath.parent ? put (new RepoPath(repoPath.parent), container) : null )
		}
		repoPath.path
	}

	String getScript(String id) {
		new RestTemplate().getForObject("${getScriptAddress}${id}", String)
	}
	
	/**
	 * Builds hierarchical data model for navigation tree in UI 
	 * @return
	 */
	Container getData() {
		HierarchicalContainer container = new HierarchicalContainer()
		container.addContainerProperty(STR_CAPTION, String, null)
		container.addContainerProperty(STR_SCRIPT_ID, String, null)

		allScripts.each {
			put new RepoPath(it), container
		}

		container.itemSorter = new ScriptSorter()
		container.doSort()
		container
	}
}
