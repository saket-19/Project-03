<%@page import="in.co.rays.project_3.controller.ClientCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Client View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
.log1 {
	padding-top: 5%;
}

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
	<form action="<%=ORSView.CLIENT_CTL%>" method="post">

		<div class="row pt-3 pb-3">
			<div class="col-md-4 mb-4"></div>

			<div class="col-md-4 mb-4">

				<jsp:useBean id="dto"
					class="in.co.rays.project_3.dto.ClientDTO"
					scope="request"></jsp:useBean>

				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Client</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Client</h3>
						<%
							}
						%>

						<H4 align="center">
							<%
								if (!ServletUtility.getSuccessMessage(request).equals("")) {
							%>
							<div class="alert alert-success alert-dismissible">
								<button type="button" class="close"
									data-dismiss="alert">&times;</button>
								<%=ServletUtility.getSuccessMessage(request)%>
							</div>
							<%
								}
							%>
						</H4>

						<H4 align="center">
							<%
								if (!ServletUtility.getErrorMessage(request).equals("")) {
							%>
							<div class="alert alert-danger alert-dismissible">
								<button type="button" class="close"
									data-dismiss="alert">&times;</button>
								<%=ServletUtility.getErrorMessage(request)%>
							</div>
							<%
								}
							%>
						</H4>

						<input type="hidden" name="id" value="<%=dto.getId()%>">
						<input type="hidden" name="createdBy"
							value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>">
						<input type="hidden" name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Code -->
						<span><b>Client Code</b><span style="color:red;">*</span></span>
						<div class="col-sm-12">
							<input type="text" class="form-control" name="code"
								placeholder="Enter Code"
								value="<%=DataUtility.getStringData(dto.getCode())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("code", request)%>
						</font><br>

						<!-- Name -->
						<span><b>Client Name</b><span style="color:red;">*</span></span>
						<div class="col-sm-12">
							<input type="text" class="form-control" name="name"
								placeholder="Enter Name"
								value="<%=DataUtility.getStringData(dto.getName())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("name", request)%>
						</font><br>

						<!-- Company -->
						<span><b>Company</b><span style="color:red;">*</span></span>
						<div class="col-sm-12">
							<input type="text" class="form-control" name="company"
								placeholder="Enter Company"
								value="<%=DataUtility.getStringData(dto.getCompany())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("company", request)%>
						</font><br>

						<!-- Email -->
						<span><b>Email</b><span style="color:red;">*</span></span>
						<div class="col-sm-12">
							<input type="text" class="form-control" name="email"
								placeholder="Enter Email"
								value="<%=DataUtility.getStringData(dto.getEmail())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("email", request)%>
						</font><br>

						<!-- Contact -->
						<span><b>Contact Number</b><span style="color:red;">*</span></span>
						<div class="col-sm-12">
							<input type="text" class="form-control" name="contact"
								placeholder="Enter Contact"
								value="<%=DataUtility.getStringData(dto.getContact())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("contact", request)%>
						</font><br><br>

						<div class="text-center">
						<%
							if (id > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=ClientCtl.OP_UPDATE%>">
							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=ClientCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=ClientCtl.OP_SAVE%>">
							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=ClientCtl.OP_RESET%>">
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

</body>
<%@include file="FooterView.jsp"%>
</html>