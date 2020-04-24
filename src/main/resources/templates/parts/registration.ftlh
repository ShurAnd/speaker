<#macro register_form>
<div class="container mt-5">
<h2 align="center">Registration Page</h2>
<br/><br/>
<form method="post" action="/register">
	
	<div class="form-group row">
		<label for="username" class="col-sm-2 col-form-label">Username</label>
		 <div class="col-sm-5">
			<input type="text"
					 name="username" 
					 class="form-control ${(usernameError??)?string('is-invalid', '')}"
					 value = "<#if user??>user.username</#if>"
					 placeholder="type username" />
					 
					 <#if usernameError??>
					     <div class="invalid-feedback">
	       						<span text="${usernameError}"></span>
	      				 </div>
      				 </#if>			 
		</div>
	</div>
	
	<div class="form-group row">
		<label for="password" class="col-sm-2 col-form-label">Password</label>
			 <div class="col-sm-5">
				<input type="password"
						 name="password" 
						class="form-control ${(passwordError??)?string('is-invalid', '')}"
						 placeholder="type password"/>
						 
							 <#if passwordError??>
							     <div class="invalid-feedback">
			       						<span text="${passwordError}"></span>
			      				 </div>
		      				 </#if>	
			</div>
	</div>
	
	<div class="form-group row">
		<label for="password2" class="col-sm-2 col-form-label">Confirm Password</label>
		 <div class="col-sm-5">
			<input type="password"
					 name="password2"
					 class="form-control ${(password2Error??)?string('is-invalid', '')}"
					 placeholder="retype password"/>
							
							 <#if password2Error??>
							     <div class="invalid-feedback">
			       						<span text="${password2Error}"></span>
			      				 </div>
		      				 </#if>	
		</div>
	</div>
	
	<div class="form-group row">
	<label for="mail" class="col-sm-2 col-form-label">Email</label>
		<div class="col-sm-5">
			<input type="email"
					 name="mail"
					 class="form-control ${(mailError??)?string('is-invalid', '')}"
					 value="<#if user??>user.mail</#if>"
					  placeholder="type email"/>
					  
							<#if mailError??>
								<div class="invalid-feedback">
									<span text="${mailError}"></span>
								</div>
							</#if>	
		</div>
	</div>
	
	<div class="col-sm-3">
	 <div class="g-recaptcha" data-sitekey="6Lc31OUUAAAAAD7ZWOsxeFvahNsUKDvsN06LmXN3"></div>
			
							<#if recaptchaError??>
								<div class="alert alert-danger" role="alert">
									<span text="${recaptchaError}"></span>
								</div>
							</#if>		
	</div>
	
	<input type="hidden" name="_csrf" value="${_csrf.token}"/>
	
    <div class="col-sm-10">
      <button type="submit" class="btn btn-primary">Register</button>
    </div>
    
</form>
</div>
</#macro>