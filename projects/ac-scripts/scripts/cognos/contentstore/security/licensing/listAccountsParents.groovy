import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	findAll( searchPath: dsl.cognos.config.ldap.namespaceCamid + "/descendant::account", properties: [PropEnum.parent]).findAll{ account ->
		println find(searchPath: account.parent.value[0].searchPath.value).defaultName.value
	}
}
