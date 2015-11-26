package ac.main

import groovy.util.logging.Slf4j

import com.vaadin.data.Container.Sortable
import com.vaadin.data.util.ItemSorter

/**
 * implements custom sorting logic for AC when folder are at the top of the list
 * @author nurtai
 *
 */
@Slf4j
class ScriptSorter implements ItemSorter, Serializable {
	
	private static final long serialVersionUID = 1L

	@Override
	void setSortProperties(Sortable container, Object[] propertyId,
			boolean[] ascending) {
		// TODO Auto-generated method stub

	}

	@Override
	int compare(Object itemId1, Object itemId2) {
		log.debug "itemId1: ${itemId1}, itemId2: ${itemId2}"

		RepoPath rp1= new RepoPath(itemId1)
		RepoPath rp2= new RepoPath(itemId2)

		rp1.root && rp2.root ? 0 : rp1.root ? -1 : rp2.root ? 1 : {

			assert !rp1.root && !rp2.root

			List<String> names1 = rp1.names
			List<String> names2 = rp2.names

			int index = 0
			while (index < rp1.namesCount - 1 && index < rp2.namesCount - 1 && names1[index] == names2[index]) { index++ }

			(isFolder(names1[index]) && isFolder(names2[index])) || \
			(!isFolder(names1[index]) && !isFolder(names2[index])) \
			? compareStrings(names1[index], names2[index]) : isFolder(names1[index]) ? -1 : 1
		}.call()
	}

	private int compareStrings(Object itemId1, Object itemId2) {
		itemId1 < itemId2 ? -1 : itemId1 == itemId2 ? 0 : 1
	}

	private boolean isFolder(String name) {
		!name.endsWith(RepoPath.SCRIPT_EXT)
	}
}
