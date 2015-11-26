runScript( 'contentstore_deployment_requestTool_GetReqToolParameters' )

params.searchPaths = [ ]
params.reqToolParameters.findAll { key, value -> key.startsWith( 'SEARCHPATH' ) }.each { id, searchPath ->
	params.searchPaths << searchPath
}

// call deployment routine
if ( params.searchPaths ) {
	runScript( 'contentstore_deployment_DeployBetweenEnv' )
}
