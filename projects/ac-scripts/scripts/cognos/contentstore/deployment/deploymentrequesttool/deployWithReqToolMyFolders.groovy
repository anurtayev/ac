runScript( 'contentstore_deployment_requestTool_GetReqToolParameters' )

params.deploymentRules = [ : ]
params.reqToolParameters.findAll { key, value -> key.startsWith( 'OBJECT' ) }.each { id, searchPath ->
	def pathId = "PATH_${ id.split( '_' )[ -1 ] }"
	def path = params.reqToolParameters[ pathId ]
	assert path, "${ pathId } is missing"
	params.deploymentRules.put ( searchPath, path )
}

// call deployment routine
if ( params.deploymentRules ) {
	runScript( 'contentstore_deployment_DeployMyFolders' )
}
