def scriptDescriptor = {
	discoverable
	description = ''
	returns = ''
	tags = [ 'Framework Manager', 'package' ]
	
	parameters {
		cpf {
			description = 'File instance or absolute path of CPF file'
		}
	}
}

def cpf = script.parameters.cpf.getClass() == File.class ? script.parameters.cpf : new File( script.parameters.cpf )
assert cpf.canRead(), 'model file is not accessible'

def model = new File( "${cpf.parent}/model.xml" )
if ( ! model.exists() ) {
	model = cpf
}

new XmlSlurper().parse( model )
