import com.cognos.developer.schemas.bibus._3.*

group='CAMID(":BI.Role.Cement_Functional")'

dsl.cognos.play {
	find( searchPath: group, properties: [PropEnum.members]).members.value.each{
		println it.searchPath.value
		println it.getClass()
	}
}

//ac.scripts.writeCsv(file: script.defaultTempFileCsv,reportData: reportData)