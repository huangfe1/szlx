<%--
  Created by IntelliJ IDEA.
  User: huangfei
  Date: 16/5/9
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    if(request.getRequestURL().toString().contains("www.52zdt.com")){
        request.getRequestDispatcher("/zdtgw").forward(request, response);
    }else{
      //  request.getRequestDispatcher("http://www.zmz365.com:8081/").forward(request, response);
        response.sendRedirect("http://www.zmz365.com:8081/");
    }
%>

</body>
</html>
