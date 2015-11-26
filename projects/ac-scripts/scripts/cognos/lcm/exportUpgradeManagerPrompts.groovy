import util.*

assert params.projectPath, '<projectPath> is missing'

new XmlSlurper()
	.parse( "${params.projectPath}/project.dat" )
	.breadthFirst().findAll { node -> node.name() == 'report' }
	.each { report ->
		report.parameter.each { parameter ->
			println "${report.@name},${report.@searchPath},${parameter.@name},${parameter.@dataType},${ parameter.isMandatory() ? 'mandatory' : 'optional' }"
			writer.println "${report.@name},${report.@searchPath},${parameter.@name},${parameter.@dataType},${ parameter.isMandatory() ? 'mandatory' : 'optional' }"
		}
	}
