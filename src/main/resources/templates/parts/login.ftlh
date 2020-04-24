<#macro login>

<form method="post" action="/login">
	<div class="form-group row">
		<label for="username" class="col-sm-2 col-form-label">Username</label>
		 <div class="col-sm-5">
			<input type="text" name="username" class="form-control"/>
		</div>
	</div>
	<div class="form-group row">
	<label for="password" class="col-sm-2 col-form-label">Password</label>
	 <div class="col-sm-5">
		<input type="password" name="password" class="form-control"/>
	</div>
	</div>
	<input type="hidden" name="_csrf" value="${_csrf.token}"/>
	 <div class="form-group row">
    <div class="col-sm-10">
      <button type="submit" class="btn btn-primary">Log in</button>
    </div>
  </div>
</form>

</#macro>