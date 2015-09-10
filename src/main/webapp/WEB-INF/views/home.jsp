<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<html>
<head>
	<title>Home</title>
	<link href="resources/css/main.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>

	<header>
		<h1>Stock Tracking</h1>
	</header>

	<%@ include file="/WEB-INF/views/menu.jsp" %>

	<article>
		<div id="content">
			<br>
		    <p>
				This will be the home to Track My Stocks		  
			</p>
		</div>
		
	</article>

	<footer>
		<P class="serverTime">The time on the server is ${serverTime}.</P>

		<P class="textToBeReplaceByAjaxCall">
			menuState:
			<c:out value="${menuState}" />
		</P>
	</footer>

</body>
</html>

