<#import "parts/common.ftlh" as cm>

<@cm.page>
<div class="container mt-5">
<div>Profile page</div>
<form method="post" action="/user/profile">
	<table>
		<tr>
			<td><label for="username">${user.username}</label></td>
		</tr>
		<tr>
			<td><label for="password">Password</label></td><td><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td><label for="mail">Email</label></td><td><input type="email" name="mail"
			 			value="<#if user.mail??> ${user.mail} </#if>"/>
			 				 
			</td>
		</tr>
	</table>
	
	<input type="hidden" name="_csrf" value="_csrf.token"/>
	
	<button type="submit">Сохранить</button>
</form>
</div>

</@cm.page>