<%@page import="in.co.rays.project_3.controller.EnrollmentCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Enrollment View</title>
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
	<form action="<%=ORSView.ENROLLMENT_CTL%>" method="post">

		<div class="row pt-3 pb-3">
			<div class="col-md-4 mb-4"></div>
			<div class="col-md-4 mb-4">

				<jsp:useBean id="dto" class="in.co.rays.project_3.dto.EnrollmentDTO"
					scope="request"></jsp:useBean>

				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Enrollment</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Enrollment</h3>
						<%
							}
						%>

						<!-- Success Message -->
						<%
							if (!ServletUtility.getSuccessMessage(request).equals("")) {
						%>
						<div class="alert alert-success alert-dismissible">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<%=ServletUtility.getSuccessMessage(request)%>
						</div>
						<%
							}
						%>

						<!-- Error Message -->
						<%
							if (!ServletUtility.getErrorMessage(request).equals("")) {
						%>
						<div class="alert alert-danger alert-dismissible">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<%=ServletUtility.getErrorMessage(request)%>
						</div>
						<%
							}
						%>

						<input type="hidden" name="id" value="<%=dto.getId()%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Enrollment No -->
						<span><b>Enrollment No</b><span style="color: red">*</span></span>
						<input type="text" class="form-control" name="enrollmentNo"
							placeholder="Enter Enrollment Number"
							value="<%=DataUtility.getStringData(dto.getEnrollmentNo())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("enrollmentNo", request)%>
						</font><br>

						<!-- Student Name -->
						<span><b>Student Name</b><span style="color: red">*</span></span>
						<input type="text" class="form-control" name="studentName"
							placeholder="Enter Student Name"
							value="<%=DataUtility.getStringData(dto.getStudentName())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("studentName", request)%>
						</font><br>

						<!-- Course -->
						<span><b>Course</b><span style="color: red">*</span></span> <input
							type="text" class="form-control" name="course"
							placeholder="Enter Course"
							value="<%=DataUtility.getStringData(dto.getCourse())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("course", request)%>
						</font><br>

						<!-- Enrollment Date -->
						<span><b>Enrollment Date</b><span style="color: red">*</span></span>
						<input type="text" class="form-control" id="udate6"
							name="enrollmentDate" readonly="readonly"
							placeholder="Select Enrollment Date"
							value="<%=DataUtility.getDateString(dto.getEnrollmentDate())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("enrollmentDate", request)%>
						</font><br> <br>

						<%
							if (id > 0) {
						%>
						<div class="text-center">
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=EnrollmentCtl.OP_UPDATE%>"> <input
								type="submit" name="operation" class="btn btn-warning"
								value="<%=EnrollmentCtl.OP_CANCEL%>">
						</div>
						<%
							} else {
						%>
						<div class="text-center">
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=EnrollmentCtl.OP_SAVE%>"> <input type="submit"
								name="operation" class="btn btn-warning"
								value="<%=EnrollmentCtl.OP_RESET%>">
						</div>
						<%
							}
						%>

					</div>
				</div>

			</div>
			<div class="col-md-4 mb-4"></div>
		</div>

	</form>
	</main>

	<%@ include file="calendar.jsp"%>
	<%@include file="FooterView.jsp"%>

</body>
</html>