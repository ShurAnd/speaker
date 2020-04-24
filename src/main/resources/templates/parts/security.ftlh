<#assign
	known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
	<#assign
		currentUser = Session.SPRING_SECURITY_CONTEXT.authentication.principal
		currentUsername = currentUser.getUsername()
		isAdmin = currentUser.isAdmin()
	>
	
<#else>
	<#assign
		currentUsername = "unknown"
		isAdmin = false
	>
</#if>