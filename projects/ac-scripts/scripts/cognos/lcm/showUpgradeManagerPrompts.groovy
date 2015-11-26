import util.*

assert parameters.projectPath, '<projectPath> is missing'

def xml = new XmlSlurper().parse( "${parameters.projectPath}/project.dat" )
println xml.name()
xml.depthFirst().findAll { node -> node.name() == 'report' }.each {
	println it.name()
	println it.@name
	
}
return 


new XmlSlurper()
	.parse( "${parameters.projectPath}/project.dat" )
	.breadthFirst()			// findAll { node -> node.name() == 'report' }
	.each { report ->
		report.parameter.each { parameter ->
			// writer.println "${report.@name},${ new SearchPath( report.@searchPath.text() ) },${parameter.@name},${parameter.@capabilities},${String.toBinaryString( parameter.@capabilities.toInteger() ) }"
			def capabilities = parameter.@capabilities.toInteger()
			def binaryString = Integer.toBinaryString( capabilities )
			def optionalFlag = capabilities & 32 ? 'optional' : 'mandatory'
			// println "${report.@name},${ report.@searchPath.text() },${parameter.@name},${ binaryString }, ${ optionalFlag } "
			println report.@name
		}
	}
