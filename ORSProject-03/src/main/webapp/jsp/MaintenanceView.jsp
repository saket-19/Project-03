<%@page import="in.co.rays.project_3.controller.MaintenanceCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Maintenance View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/wallp.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;
}
</style>
</head>

<body class="hm">

<div class="header">
	<%@include file="Header.jsp"%>
</div>

<main>
<form action="<%=ORSView.MAINTENANCE_CTL%>" method="post">

<div class="row pt-3 pb-3">
<div class="col-md-4 mb-4"></div>

<div class="col-md-4 mb-4">

<jsp:useBean id="dto"
	class="in.co.rays.project_3.dto.MaintenanceDTO"
	scope="request"></jsp:useBean>

<div class="card">
<div class="card-body">

<%
	long id = DataUtility.getLong(request.getParameter("id"));

	if (dto.getId() != null && dto.getId() > 0) {
%>
	<h3 class="text-center text-primary">Update Maintenance</h3>
<% } else { %>
	<h3 class="text-center text-primary">Add Maintenance</h3>
<% } %>

<!-- Success Message -->
<%
	if (!ServletUtility.getSuccessMessage(request).equals("")) {
%>
<div class="alert alert-success alert-dismissible">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<%=ServletUtility.getSuccessMessage(request)%>
</div>
<% } %>

<!-- Error Message -->
<%
	if (!ServletUtility.getErrorMessage(request).equals("")) {
%>
<div class="alert alert-danger alert-dismissible">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<%=ServletUtility.getErrorMessage(request)%>
</div>
<% } %>

<%
	HashMap statusList = (HashMap) request.getAttribute("statusList");
%>

<!-- Hidden Fields -->
<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime"
	value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
<input type="hidden" name="modifiedDatetime"
	value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

<!-- Code -->
<div class="form-group">
	<label><b>Code</b><span style="color:red;">*</span></label>
	<input type="text" class="form-control" name="code"
		placeholder="Enter Code"
		value="<%=DataUtility.getStringData(dto.getCode())%>">
	<font color="red">
	<%=ServletUtility.getErrorMessage("code", request)%>
	</font>
</div>

<!-- Equipment Name -->
<div class="form-group">
	<label><b>Equipment Name</b><span style="color:red;">*</span></label>
	<input type="text" class="form-control" name="equipmentName"
		placeholder="Enter Equipment Name"
		value="<%=DataUtility.getStringData(dto.getEquipmentName())%>">
	<font color="red">
	<%=ServletUtility.getErrorMessage("equipmentName", request)%>
	</font>
</div>

<!-- Maintenance Date -->
<div class="form-group">
	<label><b>Maintenance Date</b><span style="color:red;">*</span></label>
	<input type="text" class="form-control udate6"
		name="maintenanceDate" id="udate6"
		readonly="readonly"
		value="<%=DataUtility.getDateString(dto.getMaintenanceDate())%>">
	<font color="red">
	<%=ServletUtility.getErrorMessage("maintenanceDate", request)%>
	</font>
</div>

<!-- Technician Name -->
<div class="form-group">
	<label><b>Technician Name</b><span style="color:red;">*</span></label>
	<input type="text" class="form-control" name="technicianName"
		placeholder="Enter Technician Name"
		value="<%=DataUtility.getStringData(dto.getTechnicianName())%>">
	<font color="red">
	<%=ServletUtility.getErrorMessage("technicianName", request)%>
	</font>
</div>

<!-- Status -->
<div class="form-group">
	<label><b>Status</b><span style="color:red;">*</span></label>
	<%=HTMLUtility.getList("status",
		dto.getStatus(), statusList)%>
	<font color="red">
	<%=ServletUtility.getErrorMessage("status", request)%>
	</font>
</div>

<!-- Buttons -->
<div class="text-center mt-3">

<%
	if (id > 0) {
%>
	<input type="submit" name="operation"
		class="btn btn-success"
		value="<%=MaintenanceCtl.OP_UPDATE%>">

	<input type="submit" name="operation"
		class="btn btn-warning"
		value="<%=MaintenanceCtl.OP_CANCEL%>">
<%
	} else {
%>
	<input type="submit" name="operation"
		class="btn btn-success"
		value="<%=MaintenanceCtl.OP_SAVE%>">

	<input type="submit" name="operation"
		class="btn btn-warning"
		value="<%=MaintenanceCtl.OP_RESET%>">
<%
	}
%>

</div>

</div>
</div>

</div>
<div class="col-md-4 mb-4"></div>
</div>

</form>
</main>

<%@ include file="calendar.jsp" %>
<%@include file="FooterView.jsp"%>

</body>
</html>