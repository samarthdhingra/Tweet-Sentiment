<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html">
<html>
<head>
<meta charset="utf-8">
<title>JSP Ajax form</title>
<meta name="author" content="abhyuday" />
<script src="javascript/jquery-1.9.1.js"></script>
<script src="javascript/basic.js"></script>
<link rel="stylesheet" href="css/basic.css" type="text/css" media="screen" />
</head>
<body>
	<p class="large">How to make Ajax calls using Javascript and JQuery</p>
	<form id="updateUsername" method="post">
		<label for="username">What is your username?</label>
		<input type="text" id="username" name="username" />
		<input type="submit" />
	</form>
	<p id="displayName" />
	<hr />
	<p class="small">This is Another CSS class</p>
</body>
</html>