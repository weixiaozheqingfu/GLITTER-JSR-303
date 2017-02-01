<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% 
	String path = request.getContextPath();
%>
<!doctype html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<c:if test="${!empty errorMsg}">
	${errorMsg}<br><br>
</c:if>

<form action="<%=path%>/Order">
	订单编号<input type="text" name="orderId"><br><br>
	客户<input type="text" name="customer"><br><br>
	电子信箱<input type="text" name="email"><br><br>
	地址<input type="text" name="address"><br><br>
	状态<input type="text" name="status"><br><br>
	产品名称<input type="text" name="productName"><br><br>
	产品价格<input type="text" name="price"><br><br>
	<button type="submit">提交</button><br>
</form>

</body>
</html>