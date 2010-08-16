<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
   <head>
      <base href="<%= basePath %>">      
      <!-- title>Initial Redirect Page</title -->
   </head>
   <body>
      <%
		response.sendRedirect(basePath+"index.faces");
	  %>
   </body>
</html>

