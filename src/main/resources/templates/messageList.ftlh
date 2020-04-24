<#import "parts/common.ftlh" as cm>
<#import "parts/message_list.ftlh" as ml>
<#import "parts/paginator.ftlh" as p>
<#include "parts/security.ftlh">

<@cm.page>
<@p.paginator "/user/user-messages/${user.id}"/>
<div class="container mt-5">
	<h3>${user.username}</h3>
	
	<div class="container">
		<div class="row">
			<div class="col">
				<div class="card">
	  				<div class="card-body">	
		  				<div class="card-title">Subscribers</div>
		   				<h3 class="card-text"><a href="/user/subscribers/${user.id}">${subscribers}</a></h3> 
	  				</div>
				</div>		
			</div>
			<div class="col">
				<div class="card">
	  				<div class="card-body">	
		  				<div class="card-title">Subscriptions</div>
		   				<h3 class="card-text"><a href="/user/subscriptions/${user.id}">${subscriptions}</a></h3> 
	  				</div>
				</div>
			</div>
		</div>
	</div>
	
	<#assign
	
		isCurrentUser = currentUser.equals(user)
		isSubscriber = user.isSubscriber(currentUser)
	
	>
	   	
	   	<#if !isCurrentUser>
	   		<#if !isSubscriber>
			   	<div>
			   	    <form method="post" action="/user/subscribe/${user.id}">
			   	    	 <input type="hidden" name="_csrf" value="${_csrf.token}"/>
			   	    	 <button type="submit" class="btn btn-info mt-3">Subscribe</button>
			   	    </form>
		   	    </div>
		   	<#else>
		   	    <div>
			   	    <form method="post" action="/user/unscribe/${user.id}">
			   	   		 <input type="hidden" name="_csrf" value="${_csrf.token}"/>
			   	    	 <button type="submit" class="btn btn-info mt-3">Unscribe</button>
			   	    </form>
		   	    </div>
		   </#if>
   	    </#if>
	
	
	<@ml.message_list/>
	
</div>
<@p.paginator "/user/user-messages/${user.id}"/>
</@cm.page>