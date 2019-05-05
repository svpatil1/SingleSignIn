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

<body >
	<sql:setDataSource
        var="myDB"
        driver="com.mysql.cj.jdbc.Driver"
        url="jdbc:mysql://localhost:3306/Microservice"
        user="root" password="svds2019"
    />
     
    <sql:query var="listApp"   dataSource="${myDB}">
        SELECT * FROM application;
    </sql:query>
    
<%
String username=(String)session.getAttribute("loginName");
int userID=0;		        
int sessionID=0;
if(username != null){
userID=(Integer)session.getAttribute("userId");				        
sessionID=(Integer)session.getAttribute("sessionID");
}
%>

 <div class="toptab" style="overflow-x:auto:">
 	
 	<a class="active" href="index">DashBoard</a>
 	<%
	String loginName=(String)session.getAttribute("loginName");
	String role=(String)session.getAttribute("role");
	Integer userId=(Integer)session.getAttribute("userId");
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
  	<a href="http://143.167.9.201:8080/PaymentMicroservice/getViewAccount?userID=<%=userID %>&sessionID=<%=sessionID%>">Peanut Account</a>
  	<a href="logout">Logout</a>
  	<a href="about">About Us</a>
							 
	<%
	}
	else if(role.equals("ROLE_ISV")){
	%>
  	<a href="http://143.167.9.201:8080/PaymentMicroservice/getViewAccount?userID=<%=userID %>&sessionID=<%=sessionID%>">Peanut Account</a>
	<a href="upload">Upload</a>
	<a href="logout">Logout</a>
	<a href="about">About Us</a>
							 
	<%
	}
	else if(role.equals("ROLE_ADMIN")){
	%>
  	<a href="http://143.167.9.201:8080/PaymentMicroservice/getViewAccount?userID=<%=userID %>&sessionID=<%=sessionID%>">Peanut Account</a>
  	<a href="upload">Upload</a>
  	<a href="logout">Logout</a>
  	<a href="about">About Us</a>
  	
  	<%
	}
}
%>	
</div>	

<div style="overflow-x:auto:">
	<img class="one" src="resources/img/academia.png" height=400" width="100">
</div>

<div style="overflow-x:auto:" class="heading" align="center">
	<h1>WELCOME TO SHEFFIELD CLOUDBASE</h1>
	<p>The platform is all about Academia theme. The applications hosted on the platform will help students with there academic life.</p>
	<p>It facilitates ISVs to upload their applications centered around academic theme. Before registering, you can hover over the </p>
	<p>application icon to know more about it. After successful register, you will be initially credited with 1000 peanuts.</p>
	<p>For each application usage, the user will be charged 5 peanuts.</p>
</div>

<div style="overflow-x:auto;">
<table id="table2" align="center" style="overflow-x:auto;">

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
	  			<a href="http://143.167.9.201:8080/PaymentMicroservice/getCheckout?userID=<%=userID %>&sessionID=<%=sessionID%>&appName=${app.name}">
	  			<button name="${app.name}" class="btnn" title="${app.appDescription}" ><img src="${app.imageLocation}" border = "1" width="100px" height="100px">${app.name}</button>
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