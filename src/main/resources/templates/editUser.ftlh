<import "parts/common" as cm>
<@cm.page>
<div class="container mt-5" >
	${user.username}</br>
	<form method="POST" action="/user/edit/${user.id}">
		<table>
			<tbody>
				<tr>
					<td><span text="Username"></span></td>
					<td><input type="text" name="username" value="${user.username}"/></td>
				</tr>
				<tr>
					<td>Roles:</td>
				</tr>
				<#list roles as role>
				<tr>
					<td><input type="checkbox" value=${role} name="roles"/></td>
					<td>${role}</td>
				</tr>
				</#list>
			</tbody>
		</table>
		<br/>
		<button type="submit"> Save</button>
	</form>
</div>
</@cm.page>
