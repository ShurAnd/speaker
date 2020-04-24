<#import "parts/common.ftlh" as cm>

<@cm.page>
<div class="container mt-5">
	
		<div class="form-group mt-3">
			<form method="POST" action="/messages/edit/${message.id}" enctype="multipart/form-data">
			
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
				
				<button type="submit" class="btn btn-primary">Сохранить</button>
			</form>
		</div>
	
		<div class="card mt-3" style="width: 15rem;">
			<#if message.filename??>
				<img class="card-img-top" src="/img/${message.filename}"/>
			</#if>
			
			<div class="m-2">			
				<span>${message.text}</span>
				<i>${message.tag}</i>
			</div>
			<div class="card-footer text-muted container">
					<div class="row">
						<div  class="col align-self-center"></div>
						<a class="col align-self-center" href="/user/user-messages/${message.author.id}">${message.author}</a>
						<div  class="col align-self-center"></div>
					</div>
			</div>
		</div>
</div>
</@cm.page>