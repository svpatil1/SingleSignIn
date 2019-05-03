<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8"  name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/styles2.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<title>Sheffield Cloudbase Platform</title>
<style>
.w3-black{
    color: #fff !important;
    background-color: #000 !important;
}
.w3-container {
    padding: 0.01em 16px;
    padding-top: 0.01em;
    padding-right: 16px;
    padding-bottom: 0.01em;
    padding-left: 16px;
}
.footer {
    display: block;
}

img.one {
  	width: 100%;
  }
  
html, body {
  height: 100%;
  margin: 0;
  
}  

* {
    
   box-sizing: border-box;
}
</style>
</head>

<body>
	<sql:setDataSource
        var="myDB"
        driver="com.mysql.cj.jdbc.Driver"
        url="jdbc:mysql://localhost:3306/Login and Payment"
        user="root" password="sqlpass1"
    />
     
    <sql:query var="listApp"   dataSource="${myDB}">
        SELECT * FROM application;
    </sql:query>

 <div class="toptab">
 	
 	<a class="active" href="index">DashBoard</a>
 	<%
	String username=(String)session.getAttribute("loginName");
	String role=(String)session.getAttribute("role");
	System.out.println("role is:" +role);
	if(role==null){
	%>
	<a href="reg_form">Registration</a>
	<a href="get_login">Login</a>
	<a href="about">About Us</a>
	<% 
	}
	else{			        
	if(role.equals("ROLE_USER")){
	%> 
  	<a href="/PaymentMicroservice/getViewAccount">Peanut Account</a>
  	<a href="logout">Logout</a>
  	<a href="about">About Us</a>
							 
	<%
	}
	else if(role.equals("ROLE_ISV")){
	%>
  	<a href="/PaymentMicroservice/getViewAccount">Peanut Account</a>
	<a href="upload">Upload</a>
	<a href="logout">Logout</a>
	<a href="about">About Us</a>
							 
	<%
	}
	else if(role.equals("ROLE_ADMIN")){
	%>
  	<a href="/PaymentMicroservice/getViewAccount">Peanut Account</a>
  	<a href="upload">Upload</a>
  	<a href="logout">Logout</a>
  	<a href="about">About Us</a>
  	
  	<%
	}
}
%>	
</div>	

<div>
	<img class="one" src="resources/img/academia.png" height=400" width="100">
</div>

<div class="heading" align="center">
	<h1>WELCOME TO SHEFFIELD CLOUDBASE</h1>
	<p>The platform is all about Academia theme. The applications hosted on the platform will help students with there academic life.</p>
	<p>It facilitates ISVs to upload their applications centered around academic theme. Before registering, you can hover over the </p>
	<p>application icon to know more about it. After successful register, you will be initially credited with 1000 peanuts.</p>
	<p>For each application usage, the user will be charged 5 peanuts.</p>
</div>

<div class= "appTray" style="overflow-x:auto;" align="center">
<table id="table2" align="center">

	<c:if test="${sessionScope.userId == null}"> 
	<tr>
		<c:forEach var="app" items="${listApp.rows}">
  			
  			<th>
	  			<a href="get_login">
	  			<button class="btnn" title="${app.appDescription}" ><img src="${app.imageLocation}" border = "1" width="100px" height="100px">${app.name}</button>
				</a>
			</th>
    		
    			
		</c:forEach>
		</tr>		                	  
	</c:if>
	
	<c:if test="${sessionScope.userId != null}"> 
		<tr>
		<c:forEach var="app" items="${listApp.rows}">
  			
  			<th>
	  			<a href="/PaymentMicroservice/getCheckout?/${app.name}">
	  			<button class="btnn" title="${app.appDescription}" ><img src="${app.imageLocation}" border = "1" width="100px" height="100px">${app.name}</button>
				</a>
			</th>
    		
    			
		</c:forEach>
		</tr>	                	  
	</c:if>
		
</table>
</div>

<footer class="w3-container w3-black">
    <div>© 
    <script type="text/javascript">
      var d = new Date();
      document.write(d.getFullYear());
    </script>
    Team 05, Department of Computer Science, University of Sheffield</div>
    <div>Regent Court, 211 Portobello, Sheffield S1 4DP, United Kingdom</div>
  </footer>
</body>
</html> 