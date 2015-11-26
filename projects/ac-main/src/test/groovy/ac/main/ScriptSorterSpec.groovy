package ac.main

import groovy.util.logging.Slf4j
import spock.lang.Specification
import spock.lang.Unroll

@Slf4j
class ScriptSorterSpec extends Specification {

	@Unroll
	def "it sorts two strings correctly"() {
		setup:
		def scriptSorter = new ScriptSorter()

		expect:
		scriptSorter.compare(id1, id2) == result
		
		where:
		id1                                             | id2                               || result
		'/'                                             | '/clearTempFolder.groovy'         || -1
		'/cognos/analyseLog.groovy'                     | '/'                               || 1
		'/cognos'                                       | '/cognos/analyseLog.groovy'       || 0
		'/cognos/clearLogs.groovy'                      | '/cognos'                         || 0
		'/cognos/content/administr/createSource.groovy' | '/cognos/clearLogs.groovy'        || -1
		'/cog/cont/admin'                               | '/cog/cont/admin/createSr.groovy' || 0
		'/cognos/contentstore'                          | '/cognos/contentstore/admin'      || 0
		'/cog/cont/admin/documentDataSources.groovy'    | '/cog/cont'                       || 0
		'/cog/cont/admin/docData.groovy'                | '/cog/cont/admin/docErr.groovy'   || -1
		'/cog/cont/admin/docErr.groovy'                 | '/cog/cont/admin/docData.groovy'  || 1
		'/'                                             | '/'                               || 0
		'/'                                             | '/f1'                             || -1
		'/f1'                                           | '/'                               || 1
	}
}
