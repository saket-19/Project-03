<%@page import="in.co.rays.project_3.controller.ShiftListCtl"%>
<%@page import="in.co.rays.project_3.dto.ShiftDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Shift List View</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.text {
	text-align: center;
}

.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/wallp.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}
</style>
</head>

<body class="p4">

<div>
	<%@include file="Header.jsp"%>
</div>

<div>
<form action="<%=ORSView.SHIFT_LIST_CTL%>" method="post">

<jsp:useBean id="dto"
	class="in.co.rays.project_3.dto.ShiftDTO"
	scope="request"></jsp:useBean>

<%
	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1) * pageSize) + 1;
	int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
	List list = ServletUtility.getList(request);
	Iterator<ShiftDTO> it = list.iterator();
%>

<% if (list.size() != 0) { %>

<center>
<h1 class="text-light font-weight-bold pt-2">
<font color="white"> Shift List </font>
</h1>
</center>

<!-- Success Message -->
<div class="row">
	<div class="col-md-4"></div>
	<% if (!ServletUtility.getSuccessMessage(request).equals("")) { %>
	<div class="col-md-4 alert alert-success alert-dismissible"
		style="background-color: #80ff80">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<h4>
			<font color="#008000">
				<%=ServletUtility.getSuccessMessage(request)%>
			</font>
		</h4>
	</div>
	<% } %>
	<div class="col-md-4"></div>
</div>

<!-- Error Message -->
<div class="row">
	<div class="col-md-4"></div>
	<% if (!ServletUtility.getErrorMessage(request).equals("")) { %>
	<div class="col-md-4 alert alert-danger alert-dismissible">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<h4>
			<font color="red">
				<%=ServletUtility.getErrorMessage(request)%>
			</font>
		</h4>
	</div>
	<% } %>
	<div class="col-md-4"></div>
</div>

<!-- Search Section -->
<div class="row">
	<div class="col-sm-3"></div>

	<div class="col-sm-3">
		<input type="text" name="code"
			placeholder="Enter Shift code"
			class="form-control"
			value="<%=ServletUtility.getParameter("code", request)%>">
	</div>
	<div class="col-sm-3">
		<input type="text" name="name"
			placeholder="Enter Shift Name"
			class="form-control"
			value="<%=ServletUtility.getParameter("name", request)%>">
	</div>

	<div class="col-sm-3">
		<input type="submit" class="btn btn-primary btn-md"
			style="font-size: 17px" name="operation"
			value="<%=ShiftListCtl.OP_SEARCH%>">
		&nbsp;
		<input type="submit" class="btn btn-dark btn-md"
			style="font-size: 17px" name="operation"
			value="<%=ShiftListCtl.OP_RESET%>">
	</div>

	<div class="col-sm-3"></div>
</div>

<br>

<!-- Table -->
<div style="margin-bottom: 20px;" class="table-responsive">
<table class="table table-dark table-bordered">
	<thead>
	<tr style="background-color: #8C8C8C;">
		<th width="10%">
			<input type="checkbox" id="select_all" name="Select"> Select All
		</th>
		<th class="text">S.NO</th>
		<th class="text">Code</th>
		<th class="text">Name</th>
		<th class="text">Start Time</th>
		<th class="text">End Time</th>
		<th class="text">Edit</th>
	</tr>
	</thead>

	<tbody>
	<% while (it.hasNext()) {
		dto = it.next(); %>

	<tr>
		<td align="center">
			<input type="checkbox" class="checkbox"
				name="ids" value="<%=dto.getId()%>">
		</td>
		<td align="center"><%=index++%></td>
		<td align="center"><%=dto.getCode()%></td>
		<td align="center"><%=dto.getName()%></td>
		<td align="center"><%=dto.getStartTime()%></td>
		<td align="center"><%=dto.getEndTime()%></td>
		<td align="center">
			<a href="ShiftCtl?id=<%=dto.getId()%>">Edit</a>
		</td>
	</tr>

	<% } %>
	</tbody>
</table>
</div>

<!-- Pagination Buttons -->
<table width="100%">
<tr>
<td>
	<input type="submit" name="operation"
		class="btn btn-secondary btn-md"
		style="font-size: 17px"
		value="<%=ShiftListCtl.OP_PREVIOUS%>"
		<%=pageNo > 1 ? "" : "disabled"%>>
</td>

<td>
	<input type="submit" name="operation"
		class="btn btn-primary btn-md"
		style="font-size: 17px"
		value="<%=ShiftListCtl.OP_NEW%>">
</td>

<td>
	<input type="submit" name="operation"
		class="btn btn-danger btn-md"
		style="font-size: 17px"
		value="<%=ShiftListCtl.OP_DELETE%>">
</td>

<td align="right">
	<input type="submit" name="operation"
		class="btn btn-secondary btn-md"
		style="font-size: 17px"
		value="<%=ShiftListCtl.OP_NEXT%>"
		<%=(nextPageSize != 0) ? "" : "disabled"%>>
</td>
</tr>
</table>

<br>

<% } else { %>

<center>
<h1 class="text-primary font-weight-bold pt-3">Shift List</h1>
</center>

<br>

<div style="padding-left: 48%;">
	<input type="submit" name="operation"
		class="btn btn-primary btn-md"
		style="font-size: 17px"
		value="<%=ShiftListCtl.OP_BACK%>">
</div>

<% } %>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>
</div>

</body>
<%@include file="FooterView.jsp"%>
</html>