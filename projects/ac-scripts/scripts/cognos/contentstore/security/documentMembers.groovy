import com.cognos.developer.schemas.bibus._3.*

dsl.cognos.play {
	eachObject(
		searchPath: 'CAMID(":")' + "/descendant::role" + '|' + 'CAMID(":")' + "/descendant::group", 
		properties: [ PropEnum.members ] ) 
	{ securityContainer ->
		securityContainer.members.value.each { member ->
			def memberWithData = find( searchPath: member.searchPath.value, properties: [ PropEnum.defaultName, PropEnum.members ] ) 
			memberWithData.members.value.each { member2 ->
				def member2WithData = find( searchPath: member2.searchPath.value, properties: [ PropEnum.defaultName ] ) 
				println "${securityContainer.searchPath.value},${memberWithData.defaultName.value},${member2WithData.defaultName.value}"
			}
		}
	}
}
