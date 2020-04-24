<#include "security.ftlh">

  <nav class="navbar navbar-expand-lg navbar-light bg-light">
	  <a class="navbar-brand" href="/">Speaker</a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	  <div class="collapse navbar-collapse" id="navbarNavDropdown">
	    <ul class="navbar-nav">
	      <li class="nav-item">
	        <a class="nav-link" href="/">Home</a>
	      </li>
	      
	      <li class="nav-item">
	        <a class="nav-link" href="/messages">Messages</a>
	      </li>
	      
	      
	      <#if isAdmin>
		      <li class="nav-item">
		        <a class="nav-link" href="/user">User list</a>
		      </li>
	      </#if>
	      
	      <#if currentUser??>
		      <li class="nav-item">
		        <a class="nav-link" href="/user/profile">Profile</a>
		      </li>
		  </#if>
	      
	      <#if currentUser??>
		      <li class="nav-item">
		        <a class="nav-link" href="/user/user-messages/${currentUser.id}">My Messages</a>
		      </li>
	      </#if>
	    	 
	    </ul>
	    
	    <div class="navbar-text ml-auto mr-3">
     	 	${currentUsername}
   	    </div>
   	    
   	   <#if !currentUser??>
	   	    <div class="nav-item ml-auto">
		   	    <form method="get" action="/login">
		   	    	 <button type="submit" class="btn btn-primary">Login</button>
		   	    </form>
	   	    </div>
   	   </#if>
   	   
   	   <#if currentUser??> 
	   	    <div sec:authorize="isAuthenticated()">
		   	    <form method="post" action="/logout">
		   	    	 <button type="submit" class="btn btn-primary">Logout</button>
		   	    </form>
	   	    </div>
   	    </#if>
   	    
	  </div>
	  </nav>