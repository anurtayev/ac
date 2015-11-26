package ac.main

import spock.lang.Specification
import spock.lang.Unroll

class RepoPathSpec extends Specification {

	@Unroll
	def "it returns correct value from getPath"() {
		expect:
		new RepoPath(path:path).path == result

		where:
		path                                           || result
		'/cognos/contentstore/deploymentScript.groovy' || '/cognos/contentstore/deploymentScript.groovy'
		'/cognos'                                      || '/cognos'
	}

	@Unroll
	def "it calculates isRoot correctly"() {
		expect:
		new RepoPath(path:path).root == result

		where:
		path                              || result
		'/'                               || true
		'/something.groovy'               || false
		'/cognos/fm/something.groovy'     || false
	}

	@Unroll
	def "it calculates isFolder correctly"() {
		expect:
		new RepoPath(path:path).folder == result

		where:
		path                              || result
		'/'                               || true
		'/something.groovy'               || false
		'/cognos/fm'                      || true
		'/cognos'                         || true
		'/cognos/f1/f2/file.groovy'       || false
	}

	@Unroll
	def "it calculates getParent correctly"() {
		expect:
		new RepoPath(path:path).parent == result

		where:
		path                              || result
		'/cognos/content/script4.groovy'  || '/cognos/content'
		'/cognos'                         || '/'
		'/cognos/s1/y2'                   || '/cognos/s1'
	}

	@Unroll
	def "it calculates getName correctly"() {
		expect:
		new RepoPath(path:path).name == result

		where:
		path                              || result
		'/cognos/content/script4.groovy'  || 'script4.groovy'
		'/cognos'                         || 'cognos'
		'/cognos/s1/y2'                   || 'y2'
	}

	@Unroll
	def "it calculates getNamesCount correctly"() {
		expect:
		new RepoPath(path:path).namesCount == result

		where:
		path                              || result
		'/cognos/content/script4.groovy'  || 3
		'/cognos'                         || 1
		'/'                               || 0
		'/cognos/s1/y2'                   || 3
	}

	@Unroll
	def "it calculates getNames correctly"() {
		expect:
		new RepoPath(path:path).names == result
	
		where:
		path                              || result
		'/cognos/content/script4.groovy'  || ['cognos', 'content', 'script4.groovy']
		'/cognos'                         || ['cognos']
		'/'                               || []
		'/cognos/s1/y2'                   || ['cognos', 's1', 'y2']
	}
}
