<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.controller.InquiryCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inquiry View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	padding-bottom: 11px;
	background-color: #ebebe0;
}
.input-group-addon {
	box-shadow: 9px 8px 7px #001a33;
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

	<div>
		<main>

		<form action="<%=ORSView.INQUIRY_CTL%>" method="post">

			<jsp:useBean id="dto"
				class="in.co.rays.project_3.dto.InquiryDTO" scope="request"></jsp:useBean>

			<div class="row pt-3">
				<div class="col-md-4 mb-4"></div>

				<div class="col-md-4 mb-4">
					<div class="card input-group-addon">
						<div class="card-body">

							<%
								if (dto.getInquirerName() != null && dto.getId() > 0) {
							%>
							<h3 class="text-center default-text text-primary">UPDATE INQUIRY</h3>
							<%
								} else {
							%>
							<h3 class="text-center default-text text-primary">ADD INQUIRY</h3>
							<%
								}
							%>

							<!-- Messages -->
							<H4 align="center">
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
							</H4>

							<H4 align="center">
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
							</H4>

							<input type="hidden" name="id" value="<%=dto.getId()%>">
							<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
							<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
							<input type="hidden" name="createdDatetime"
								value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
							<input type="hidden" name="modifiedDatetime"
								value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

							<!-- Inquirer Name -->
							<span class="pl-sm-5"><b>Inquirer Name</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-user grey-text"></i>
										</div>
									</div>
									<input type="text" class="form-control"
										name="inquirerName" placeholder="Inquirer Name"
										value="<%=DataUtility.getStringData(dto.getInquirerName())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5">
								<%=ServletUtility.getErrorMessage("inquirerName", request)%>
							</font><br>

							<!-- Email -->
							<span class="pl-sm-5"><b>Email</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-envelope grey-text"></i>
										</div>
									</div>
									<input type="text" class="form-control"
										name="email" placeholder="Email"
										value="<%=DataUtility.getStringData(dto.getEmail())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5">
								<%=ServletUtility.getErrorMessage("email", request)%>
							</font><br>

							<!-- Subject -->
							<span class="pl-sm-5"><b>Inquiry Subject</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-book grey-text"></i>
										</div>
									</div>
									<input type="text" class="form-control"
										name="inquirySubject" placeholder="Subject"
										value="<%=DataUtility.getStringData(dto.getInquirySubject())%>">
								</div>
							</div>
							<font color="red" class="pl-sm-5">
								<%=ServletUtility.getErrorMessage("inquirySubject", request)%>
							</font><br>

							<!-- Status -->
							<span class="pl-sm-5"><b>Status</b><span
								style="color: red;">*</span></span><br>
							<div class="col-sm-12">
								<div class="input-group">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<i class="fa fa-info-circle grey-text"></i>
										</div>
									</div>
									<%
										HashMap map = new HashMap();
										map.put("New", "New");
										map.put("In Progress", "In Progress");
										map.put("Closed", "Closed");
									%>
									<%=HTMLUtility.getList("inquiryStatus",
											dto.getInquiryStatus(), map)%>
								</div>
							</div>
							<font color="red" class="pl-sm-5">
								<%=ServletUtility.getErrorMessage("inquiryStatus", request)%>
							</font><br>

							<!-- Buttons -->
							<div class="text-center">
								<%
									if (dto.getInquirerName() != null && dto.getId() > 0) {
								%>
								<input type="submit" name="operation"
									class="btn btn-success btn-md"
									value="<%=InquiryCtl.OP_UPDATE%>">
								<input type="submit" name="operation"
									class="btn btn-warning btn-md"
									value="<%=InquiryCtl.OP_CANCEL%>">
								<%
									} else {
								%>
								<input type="submit" name="operation"
									class="btn btn-success btn-md"
									value="<%=InquiryCtl.OP_SAVE%>">
								<input type="submit" name="operation"
									class="btn btn-warning btn-md"
									value="<%=InquiryCtl.OP_RESET%>">
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
	</div>

</body>

<%@include file="FooterView.jsp"%>

</html>
