package ac.main

import groovy.transform.Immutable

import org.gcontracts.annotations.Invariant

/**
 * Models script path inside repository. Provides utility methods for path analysis.
 * @author anurtay
 *
 */
@Immutable
@Invariant({
	path != null && path[0] == FORWARD_SLASH
})
class RepoPath implements Serializable {

	private static final long serialVersionUID = 1L

	final static String SCRIPT_EXT = '.groovy'
	final static String FORWARD_SLASH= '/'

	String path

	boolean isRoot() {
		path == FORWARD_SLASH
	}

	boolean isFolder() {
		!path.endsWith(SCRIPT_EXT)
	}

	String getParent() {
		root ? null : namesCount == 1 ? FORWARD_SLASH : path - ( FORWARD_SLASH + name )
	}

	String getName() {
		root ? path : path.split(FORWARD_SLASH)[-1]
	}

	int getNamesCount() {
		root ? 0 : path.split(FORWARD_SLASH).size() - 1
	}

	List<String> getNames() {
		root ? [] : path.split(FORWARD_SLASH)[1..-1]
	}
}
