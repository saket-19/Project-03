<%@page import="in.co.rays.project_3.controller.MaintenanceListCtl"%>
<%@page import="in.co.rays.project_3.dto.MaintenanceDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
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
<title>Maintenance List View</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/wallp.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}
.text {
	text-align: center;
}
</style>
</head>

<body class="p4">

<%@include file="Header.jsp"%>

<form action="<%=ORSView.MAINTENANCE_LIST_CTL%>" method="post">

<jsp:useBean id="dto"
	class="in.co.rays.project_3.dto.MaintenanceDTO"
	scope="request"></jsp:useBean>

<%
	HashMap statusList = (HashMap) request.getAttribute("statusList");

	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1) * pageSize) + 1;
	int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

	List list = ServletUtility.getList(request);
	Iterator<MaintenanceDTO> it = list.iterator();
%>

<center>
<h1 class="text-light font-weight-bold pt-2">
	Maintenance List
</h1>
</center>

<!-- Search Row -->
<div class="row mt-3">
	<div class="col-sm-2"></div>

	<div class="col-sm-2">
		<input type="text" name="code"
			placeholder="Enter Code"
			class="form-control"
			value="<%=ServletUtility.getParameter("code", request)%>">
	</div>

	<div class="col-sm-3">
		<%=HTMLUtility.getList("status",
				dto.getStatus(), statusList)%>
	</div>

	<div class="col-sm-3">
		<input type="submit" class="btn btn-primary"
			name="operation"
			value="<%=MaintenanceListCtl.OP_SEARCH%>">

		<input type="submit" class="btn btn-dark"
			name="operation"
			value="<%=MaintenanceListCtl.OP_RESET%>">
	</div>
</div>

<br>

<%
if (list.size() > 0) {
%>

<div class="table-responsive">
<table class="table table-dark table-bordered">

<thead>
<tr>
	<th width="10%">
		<input type="checkbox" id="select_all"> Select All
	</th>
	<th class="text">S.NO</th>
	<th class="text">Code</th>
	<th class="text">Equipment Name</th>
	<th class="text">Maintenance Date</th>
	<th class="text">Technician</th>
	<th class="text">Status</th>
	<th class="text">Edit</th>
</tr>
</thead>

<tbody>
<%
while (it.hasNext()) {
	dto = it.next();
%>

<tr>
	<td align="center">
		<input type="checkbox" class="checkbox" name="ids" 
			value="<%=dto.getId()%>">
	</td>

	<td align="center"><%=index++%></td>
	<td align="center"><%=dto.getCode()%></td>
	<td align="center"><%=dto.getEquipmentName()%></td>
	<td align="center"><%=dto.getMaintenanceDate()%></td>
	<td align="center"><%=dto.getTechnicianName()%></td>
	<td align="center"><%=dto.getStatus()%></td>

	<td align="center">
		<a href="MaintenanceCtl?id=<%=dto.getId()%>">Edit</a>
	</td>
</tr>

<%
}
%>
</tbody>
</table>
</div>

<!-- Pagination -->
<table width="100%">
<tr>
<td>
<input type="submit" name="operation"
	class="btn btn-secondary"
	value="<%=MaintenanceListCtl.OP_PREVIOUS%>"
	<%=pageNo > 1 ? "" : "disabled"%>>
</td>

<td>
<input type="submit" name="operation"
	class="btn btn-primary"
	value="<%=MaintenanceListCtl.OP_NEW%>">
</td>

<td>
<input type="submit" name="operation"
	class="btn btn-danger"
	value="<%=MaintenanceListCtl.OP_DELETE%>">
</td>

<td align="right">
<input type="submit" name="operation"
	class="btn btn-secondary"
	value="<%=MaintenanceListCtl.OP_NEXT%>"
	<%=(nextPageSize != 0) ? "" : "disabled"%>>
</td>
</tr>
</table>

<%
} else {
%>

<br>
<center>
<h3 class="text-danger">No record found</h3>
<input type="submit" name="operation"
	class="btn btn-primary"
	value="<%=MaintenanceListCtl.OP_BACK%>">
</center>

<%
}
%>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

<%@include file="FooterView.jsp"%>

</body>
</html>