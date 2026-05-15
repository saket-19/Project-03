<%@page import="in.co.rays.project_3.controller.EnrollmentListCtl"%>
<%@page import="in.co.rays.project_3.dto.EnrollmentDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Enrollment List View</title>
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
		<form action="<%=ORSView.ENROLLMENT_LIST_CTL%>" method="post">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.EnrollmentDTO"
				scope="request"></jsp:useBean>

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;
				int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
				List list = ServletUtility.getList(request);
				Iterator<EnrollmentDTO> it = list.iterator();
			%>

			<%
				if (list.size() != 0) {
			%>

			<center>
				<h1 class="text-light font-weight-bold pt-2">
					<font color="white">Enrollment List</font>
				</h1>
			</center>

			<br>

			<div class="table-responsive">
				<table class="table table-dark table-bordered">
					<thead>
						<tr style="background-color: #8C8C8C;">
							<th width="10%"><input type="checkbox" id="select_all"
								name="Select" class="text"> Select All</th>
							<th class="text">S.NO</th>
							<th class="text">Enrollment No</th>
							<th class="text">Student Name</th>
							<th class="text">Course</th>
							<th class="text">Enrollment Date</th>
							<th class="text">Edit</th>
						</tr>
					</thead>

					<tbody>
						<%
							while (it.hasNext()) {
									dto = it.next();
						%>
						<tr>
							<td align="center"><input type="checkbox" class="checkbox"
								name="ids" value="<%=dto.getId()%>"></td>
							<td align="center"><%=index++%></td>
							<td align="center"><%=dto.getEnrollmentNo()%></td>
							<td align="center"><%=dto.getStudentName()%></td>
							<td align="center"><%=dto.getCourse()%></td>
							<td align="center"><%=dto.getEnrollmentDate()%></td>
							<td align="center"><a
								href="EnrollmentCtl?id=<%=dto.getId()%>">Edit</a></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>

			<table width="100%">
				<tr>
					<td><input type="submit" name="operation"
						class="btn btn-secondary"
						value="<%=EnrollmentListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td><input type="submit" name="operation"
						class="btn btn-primary" value="<%=EnrollmentListCtl.OP_NEW%>">
					</td>

					<td><input type="submit" name="operation"
						class="btn btn-danger" value="<%=EnrollmentListCtl.OP_DELETE%>">
					</td>

					<td align="right"><input type="submit" name="operation"
						class="btn btn-secondary" value="<%=EnrollmentListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
				} else {
			%>

			<center>
				<h1 class="text-primary font-weight-bold pt-3">Enrollment List</h1>
			</center>

			<br>

			<div style="padding-left: 48%;">
				<input type="submit" name="operation" class="btn btn-primary"
					value="<%=EnrollmentListCtl.OP_BACK%>">
			</div>

			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

		</form>
	</div>

</body>
<%@include file="FooterView.jsp"%>
</html>