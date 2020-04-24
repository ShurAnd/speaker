<#import "parts/common.ftlh" as cm>

<@cm.page>
<div class="container mt-5">
	<h3>${user.username}</h3>
	
	<div class="card-columns">
		<#list users as user>
			<div class="card mt-3" style="width: 15rem;">
				<div class="m-2">
					<a href="/user/user-messages/${user.id}">${user.username}</a>
				</div>
			</div>
		</#list>
	</div>
	
</div>

</@cm.page>