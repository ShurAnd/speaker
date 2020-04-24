<#import "parts/common.ftlh" as cm>
<#import "parts/message_list.ftlh" as ml>
<#import "parts/paginator.ftlh" as p>

<#include "parts/security.ftlh">
<@cm.page>
<@p.paginator "/messages"/>
<div class="container mt-5">
	<div>Поиск по тегу</div>
	<div class="form-row">
		<div class="form-group col-md-6">
		
			<form method="get" action="/messages" class="form-inline">
			    
					<input type="text" name="filter" placeHolder="enter the tag" class="form-control" value="<#if filter??>${filter}</#if>"/>
					<button type="submit" class="btn btn-primary ml-3">Искать</button>
				
			</form>
			
		</div>
	</div>
	
	 <a class="btn btn-primary" data-toggle="collapse" href="#addMessage" role="button" aria-expanded="false" aria-controls="collapseExample">
    	Add message
 	 </a>
 	 
	<div class="${(message??)?string('collapse show', 'collapse')}" id="addMessage">
	
		<div class="form-group mt-3">
							<form method="POST" action="/messages" enctype="multipart/form-data">
			
				<div class="form-group">
					<input type="text"
							 name="text" id="text"
							 placeHolder="Enter the message"
							 class="${(textError??)?string('form-control is-invalid', 'form-control')}"
							 value="<#if message??> ${message.text}</#if>"/>
							 
					     <#if textError??>
					     	<div class="invalid-feedback">
	       						<span th:text="${textError}"></span>
	      				 	</div>
	      				 </#if> 
				</div>
				
				<div class="form-group">
					<input type="text"
							 id="tag"
							 name="tag" 
							 placeHolder="Enter the tag" 
							 class="${(tagError??)?string('form-control is-invalid', 'form-control')}"
							 value="<#if message??> ${message.tag}</#if>"/>
				</div>
				
				<div class="form-group">
					<div class="custom-file">
						<label class="custom-file-label" for="customFile">Choose File</label>
						<input type="file" 
								name="file" 
								id="customFile"/>
					</div>
				</div>
				
				<input type="hidden" name="_csrf" value="${_csrf.token}"/>
				
				<button type="submit" class="btn btn-primary">Отправить</button>
			</form>
		</div>
	</div>
	
<@ml.message_list/>
</div>
<@p.paginator "/messages"/>
</@>