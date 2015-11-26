assert scriptArguments.size() == 1, 'mandatory CLI argument missing - <reqId>'
def reqId = scriptArguments[ 0 ]

// deployment request status validation
def req = config.deployment.db.firstRow ( "SELECT * FROM DR_DPLYREQ WHERE DPLYREQ_ID = ${ reqId }" )
assert req.DPLYREQ_STATUS == '0', 'deployment request must be at NEW status'

// extract parameters 
params.reqToolParameters = [ : ]
config.deployment.db.eachRow ( "SELECT * FROM DR_DPLYPARAM WHERE DPLYREQ_ID = ${ reqId }" ) { row ->
	params.reqToolParameters.put( row.DPLYPARAM_NAME, row.DPLYPARAM_VALUE )
}
