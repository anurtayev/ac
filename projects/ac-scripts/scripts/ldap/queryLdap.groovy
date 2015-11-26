import ac.dsl.ldap.LDAP

def scriptDescriptor = {
	discoverable
	tags = ['ldap', 'audit', 'clean up']
}

def allLdapAccounts = new File( 'C://Users//anurtay//Google Drive//work//IBM Audit//Cognos 7 review//usernames_normalized_DEV.csv' ).readLines()

ldap = LDAP.newInstance( "ldap://${ac.config.ldap.host}:${ac.config.ldap.port}", ac.config.ldap.usr, ac.config.ldap.pwd )

println "=============>"
script.defaultTempFileCsv.withPrintWriter { writer ->
	allLdapAccounts.each { cn ->
		def foundAccounts = ldap.findAll( base: ac.config.ldap.baseDn, filter: "(cn=${cn})")
		writer.println "${cn},${foundAccounts.size()}"
	}
}
