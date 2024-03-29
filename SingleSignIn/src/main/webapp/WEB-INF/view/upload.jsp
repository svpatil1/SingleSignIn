<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %> 

<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.7.0/css/all.css'>
<style>

form{
  max-width:600px;
  margin: 0 auto;
  margin-top: 100px;
  height: 450px;
}

div.note{
  max-width:500px; 
  max-width:510px;
  margin: 0 auto;
  margin-top: 10px;
  height: 120px;
  margin-bottom: 20px;
}

div{
  max-width:500px; 
}
input{
  margin-top: 10px;  
}
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333;
}

li {
  float: left;
}

li a {
  display: block;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
  font-style: normal;
  font-family: "Times New Roman", Times, serif;
}

li a:hover {
  background-color: #111;
}

</style>
<title>Uploading Files</title>
</head>

<body>

<ul>
  <li><a class="active" href="index">Back</a></li>
</ul>

<div class="note">
<h2 align="center">NOTE</h2>
<p>Kindly make a note before uploading your application, that the application name and its icon name(.png) must be same. Also, you need to include some files in your application. </p>
<p>You can download that files from the below link.</p>
<a align="center" href="instructions">Click Here</a>
</div>


<f:form action="uploadFiles" modelAttribute="command" class="w3-container w3-card-4 w3-light-grey w3-text-pink" enctype="multipart/form-data" method="POST">

	<div class="w3-center">
  		<h2>Upload File</h2>
  		<c:if test = "${err!=null}">
			<p class="error" style="color:blue;"> ${err}</p>
		</c:if>
	</div>
	<div class="w3-row w3-section">		
       	<div class="w3-rest">
      		Application Name: <f:input path="name" class="w3-input w3-border" name="name" type="text" placeholder="Enter your Application Name" />
    	</div>
	</div>
	<div class="w3-row w3-section">		
       	<div class="w3-rest">
      		Application Description: <f:input path="appDescription" class="w3-input w3-border" name="description" type="text" placeholder="Enter Description for your Application" />
    	</div>
	</div>
    <div class="w3-rest" >	
      	WAR file to upload: <input type="file" name="file" />
      	SQL file to upload: <input type="file" name="file" />
      	Icon Image to upload: <input type="file" name="file" />
    </div>


    <div class="w3-rest">
      	<input type="submit" value="Upload" />
    </div>
    	
</f:form>
</body>
</html>