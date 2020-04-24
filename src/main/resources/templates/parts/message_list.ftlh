<#macro message_list>
<#include "security.ftlh">
<div class="card-columns" id="message-list">
		<#list messages as message>
			<div class="card mt-3" style="width: 22rem;" data-id="<#if message??>message.id</#if>">
			
			<#if message.filename??>
				<img class="card-img-top" src="/img/${message.filename}"/>
			</#if>
			
			<div class="m-2">			
				<span>${message.text}</span>
				<i>${message.tag}</i>
			</div>
			
				<div class="card-footer text-muted container">
					<div class="row">
						
						<#if currentUsername == message.author.username>
							<form class="col align-self-center" method="get" action="/messages/edit/${message.id}">
					   	    	 <button type="submit" class="btn btn-primary">Edit</button>
					   	    </form>
						</#if>
						
						<a class="col align-self-center" href="/messages/like/${message.id}">			
							<#if message.meLiked>
								<i class="fas fa-heart"></i>
							<#else>
								<i class="far fa-heart"></i>
							</#if>	
							${message.likes}
						</a>
						
						<#if currentUsername == message.author.username || isAdmin>
								<form class="col align-self-center" method="get" action="/messages/delete/${message.id}">
									<button type="submit" class="btn btn-primary">Delete</button>
								</form>
						</#if>
					</div>
					<div class="row">
						<div  class="col align-self-center"></div>
						<a class="col align-self-center" href="/user/user-messages/${message.author.id}">${message.author}</a>
						<div  class="col align-self-center"></div>
					</div>
				</div>
				
			</div>
		</#list>
	</div>
	</#macro>