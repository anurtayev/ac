import com.cognos.developer.schemas.bibus._3.*

def scriptDescriptor = { parameters { capabilities
	} }

_scheduling=[
	'canUseScheduling',
	'canUseSchedulingByDay',
	'canUseSchedulingByHour',
	'canUseSchedulingByMinute',
	'canUseSchedulingByMonth',
	'canUseSchedulingByTrigger',
	'canUseSchedulingByWeek',
	'canUseSchedulingByYear',
	'canUseSchedulingPriority'
]
_reportStudio=['canUseReportStudio', 'canUseUserDefinedSQL', 'canUseHTML','canUseReportStudioFileManagement','canUseBursting', 'canUseExternalData']

_queryStudio=['canUseQueryStudio','canUseQueryStudioFileManagement', 'canUseQueryStudioAdvancedMode']

_metricStudio=['canUseMetricStudio','canUseMetricStudioEditView']

rolesRecipient = [
	'canUseCognosViewer',
	'canUseCognosViewerContextMenu',
	'canUseCognosViewerSelection',
	'canUseCognosViewerToolbar',
	'canReceiveDetailedErrors',
	'canUseShowHiddenObjectsPreference',
	'canUseRepository',
	'canUseLineage',
	'canUseGlossary',
	'canViewContentInRepository'
]
rolesConsumer =  rolesRecipient + _scheduling + 'canUseCognosViewerRunWithOptions' + 'canUseConditionalSubscriptions'
rolesEnhancedConsumer = rolesConsumer + 'canUseMobileService' + 'canUseCognosInsight' + 'canUseDashboardViewer'+ 'canUseIndexSearch' + 'canCollaborate'+'canUseCollaborationFeatures'+'canLaunchCollaborationTools'
rolesBusinessManager = rolesEnhancedConsumer + _metricStudio
rolesBusinessAnalyst = rolesEnhancedConsumer + 'canUseAnalysisStudio'
//rolesBusinessAuthor = rolesEnhancedConsumer + _queryStudio + 'canUseAdvancedDashboardFeatures'+'canUseInteractiveDashboardFeatures'
rolesBusinessAuthor = rolesEnhancedConsumer + _queryStudio + 'canUseAdvancedDashboardFeatures'+'canUseInteractiveDashboardFeatures'+'canUseMonitorActivityTool'+ 'canUseUserDefinedSQL'+ 'canUseHTML'
rolesAdvancedBusinessAuthor = rolesEnhancedConsumer + _queryStudio + 'canUseAnalysisStudio'
rolesProfessionalAuthor = rolesBusinessAuthor + _reportStudio
rolesProfessional = rolesProfessionalAuthor + _metricStudio + 'canUseEventStudio'+ 'canUseAnalysisStudio'+'canUsePowerPlay'

if ( isAdministrator() ) 'BI Administrator'
else if (isRecipient()) 'BI Recipient'
else if (isConsumer()) 'BI Consumer'
else if (isEnhancedConsumer()) 'BI Enhanced Consumer'
else if (isBusinessManager()) 'BI Business Manager'
else if (isBusinessAnalyst()) 'BI Business Analyst'
else if (isBusinessAuthor()) 'BI Business Author'
else if (isAdvancedBusinessAuthor()) 'Advanced BI Business Author'
else if (isProfessionalAuthor()) 'BI Professional Author'
else if (isProfessional()) 'BI Professional'
else {
	script.log.warn "can't calculate license for capabilities combination: ${script.parameters.capabilities}"
	'unidentified'
}

def isAdministrator() {
	script.parameters.capabilities.any{it == 'canUseAdministrationPortal'}
}

def isProfessional() {
	def extraRoles = script.parameters.capabilities - rolesProfessional
	script.log.debug "isProfessional: ${extraRoles}"
	!extraRoles
}

def isProfessionalAuthor() {
	def extraRoles = script.parameters.capabilities - rolesProfessionalAuthor
	script.log.debug "isProfessionalAuthor: ${extraRoles}"
	!extraRoles
}

def isAdvancedBusinessAuthor() {
	def extraRoles = script.parameters.capabilities - rolesAdvancedBusinessAuthor
	script.log.debug "isAdvancedBusinessAuthor: ${extraRoles}"
	!extraRoles
}

def isBusinessAuthor() {
	def extraRoles = script.parameters.capabilities - rolesBusinessAuthor
	script.log.debug "isBusinessAuthor: ${extraRoles}"
	!extraRoles
}

def isBusinessAnalyst() {
	def extraRoles = script.parameters.capabilities - rolesBusinessAnalyst
	script.log.debug "isBusinessAnalyst: ${extraRoles}"
	!extraRoles
}

def isBusinessManager() {
	def extraRoles = script.parameters.capabilities - rolesBusinessManager
	script.log.debug "isBusinessManager: ${extraRoles}"
	!extraRoles
}

def isEnhancedConsumer() {
	def extraRoles = script.parameters.capabilities - rolesEnhancedConsumer
	script.log.debug "isEnhancedConsumer: ${extraRoles}"
	!extraRoles
}

def isConsumer() {
	def extraRoles = script.parameters.capabilities - rolesConsumer
	script.log.debug "isConsumer: ${extraRoles}"
	!extraRoles
}

def isRecipient() {
	def extraRoles = script.parameters.capabilities - rolesRecipient
	script.log.debug "isRecipient: ${extraRoles}"
	!extraRoles
}
