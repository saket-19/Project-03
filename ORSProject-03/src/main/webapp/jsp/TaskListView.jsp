<%@page import="in.co.rays.project_3.controller.TaskListCtl"%>
<%@page import="in.co.rays.project_3.dto.TaskDTO"%>
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
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Task List View</title>

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

<%@include file="Header.jsp"%>

<form action="<%=ORSView.TASK_LIST_CTL%>" method="post">

<jsp:useBean id="dto" class="in.co.rays.project_3.dto.TaskDTO" scope="request"></jsp:useBean>

<%
	HashMap statusList = (HashMap) request.getAttribute("statusList");

	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1) * pageSize) + 1;
	int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

	List list = ServletUtility.getList(request);
	Iterator<TaskDTO> it = list.iterator();
%>

<center>
<h1 class="text-light font-weight-bold pt-2">
<font color="white">Task List</font>
</h1>
</center>

<!-- Success Message -->
<% if (!ServletUtility.getSuccessMessage(request).equals("")) { %>
<div class="alert alert-success text-center">
	<%=ServletUtility.getSuccessMessage(request)%>
</div>
<% } %>

<!-- Error Message -->
<% if (!ServletUtility.getErrorMessage(request).equals("")) { %>
<div class="alert alert-danger text-center">
	<%=ServletUtility.getErrorMessage(request)%>
</div>
<% } %>

<!-- Search Section -->
<div class="row">

	<div class="col-sm-2"></div>

	<div class="col-sm-2">
		<input type="text" name="taskCode" placeholder="Task Code"
			class="form-control"
			value="<%=ServletUtility.getParameter("taskCode", request)%>">
	</div>

	<div class="col-sm-2">
		<input type="text" name="task" placeholder="Task"
			class="form-control"
			value="<%=ServletUtility.getParameter("task", request)%>">
	</div>

	<div class="col-sm-2">
		<input type="text" name="assignedTo" placeholder="Assigned To"
			class="form-control"
			value="<%=ServletUtility.getParameter("assignedTo", request)%>">
	</div>

	<div class="col-sm-2">
		<%=HTMLUtility.getList("taskStatus", dto.getTaskStatus(), statusList)%>
	</div>

	<div class="col-sm-2">
		<input type="submit" class="btn btn-primary"
			name="operation"
			value="<%=TaskListCtl.OP_SEARCH%>">

		<input type="submit" class="btn btn-dark"
			name="operation"
			value="<%=TaskListCtl.OP_RESET%>">
	</div>

</div>

<br>

<!-- Table -->
<div class="table-responsive">
<table class="table table-dark table-hover table-bordered">

<thead>
<tr style="background-color: #8C8C8C;">
	<th width="10%"><input type="checkbox" id="select_all"> Select All</th>
	<th class="text">S.NO</th>
	<th class="text">Task Code</th>
	<th class="text">Task</th>
	<th class="text">Assigned To</th>
	<th class="text">Due Date</th>
	<th class="text">Task Status</th>
	<th class="text">Edit</th>
</tr>
</thead>

<tbody>
<% while (it.hasNext()) {
	dto = it.next(); %>

<tr>
	<td align="center">
		<input type="checkbox" class="checkbox" name="ids"
			value="<%=dto.getId()%>">
	</td>
	<td align="center"><%=index++%></td>
	<td align="center"><%=dto.getTaskCode()%></td>
	<td align="center"><%=dto.getTask()%></td>
	<td align="center"><%=dto.getAssignedTo()%></td>
	<td align="center"><%=DataUtility.getDateString(dto.getDueDate())%></td>
	<td align="center"><%=dto.getTaskStatus()%></td>
	<td align="center">
		<a href="TaskCtl?id=<%=dto.getId()%>">Edit</a>
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
	class="btn btn-secondary"
	value="<%=TaskListCtl.OP_PREVIOUS%>"
	<%=pageNo > 1 ? "" : "disabled"%>>
</td>

<td>
<input type="submit" name="operation"
	class="btn btn-primary"
	value="<%=TaskListCtl.OP_NEW%>">
</td>

<td>
<input type="submit" name="operation"
	class="btn btn-danger"
	value="<%=TaskListCtl.OP_DELETE%>">
</td>

<td align="right">
<input type="submit" name="operation"
	class="btn btn-secondary"
	value="<%=TaskListCtl.OP_NEXT%>"
	<%=(nextPageSize != 0) ? "" : "disabled"%>>
</td>
</tr>
</table>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

<%@include file="FooterView.jsp"%>

</body>
</html>