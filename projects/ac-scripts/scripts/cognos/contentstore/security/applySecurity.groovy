def scriptDescriptor = {
	description = 'Main script to deploy LAFAM security'
}

ac.scripts.cognos.contentstore.security.resetCognosSecurity()

ac.scripts.cognos.contentstore.security.createCustomGroups()

ac.scripts.cognos.contentstore.security.mapCustomGroupsToLdap()

ac.scripts.cognos.contentstore.security.mapCustomRules()

ac.scripts.cognos.contentstore.security.applyFoldersSecurity()

ac.scripts.cognos.contentstore.security.applyAdhocFoldersSecurity()

ac.scripts.cognos.contentstore.security.applyDataSourcesSecurity()
