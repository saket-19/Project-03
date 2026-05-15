<%@page import="in.co.rays.project_3.controller.LocationCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Event View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
.log1 {
	padding-top: 5%;
	/* padding-left: 30%; */
}

.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/wallp.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;
}

i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	padding-bottom: 7px;
	background-color: #ebebe0;
}
</style>
</head>
<body class="hm">
	<div class="header">
		<%@include file="Header.jsp"%>
	</div>
	<div>

		<main>
		<form action="<%=ORSView.LOCATION_CTL%>" method="post">

			<div class="row pt-3 pb-3">
				<!-- Grid column -->
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">
					<jsp:useBean id="dto" class="in.co.rays.project_3.dto.LocationDTO"
						scope="request"></jsp:useBean>
					<div class="card">
						<div class="card-body">
							<%
								long id = DataUtility.getLong(request.getParameter("id"));

								if (dto.getId() != null && dto.getId() > 0) {
							%>
							<h3 class="text-center  text-primary">Update Location</h3>
							<%
								} else {
							%>
							<h3 class="text-center  text-primary">Add location</h3>
							<%
								}
							%>
							<!--Body-->
							<div>


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
								<input type="hidden" name="createdBy"
									value="<%=dto.getCreatedBy()%>"> <input type="hidden"
									name="modifiedBy" value="<%=dto.getModifiedBy()%>"> <input
									type="hidden" name="createdDatetime"
									value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
								<input type="hidden" name="modifiedDatetime"
									value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">
							</div>
							<div class="md-form">
								<span class="pl-sm-5"><b>city</b><span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-book grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control" name="city"
											placeholder="Enter city Name"
											value="<%=DataUtility.getStringData(dto.getCity())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("city", request)%></font></br>

								<div class="md-form">
									<span class="pl-sm-5"><b>state</b><span
										style="color: red;">*</span></span></br>
									<div class="col-sm-12">
										<div class="input-group">
											<div class="input-group-prepend">
												<div class="input-group-text">
													<i class="fa fa-book grey-text" style="font-size: 1rem;"></i>
												</div>
											</div>
											<input type="text" class="form-control" name="state"
												placeholder="Enter state Name"
												value="<%=DataUtility.getStringData(dto.getState())%>">
										</div>
									</div>
									<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("state", request)%></font></br>


									<div class="md-form">
										<span class="pl-sm-5"><b>country</b><span
											style="color: red;">*</span></span></br>
										<div class="col-sm-12">
											<div class="input-group">
												<div class="input-group-prepend">
													<div class="input-group-text">
														<i class="fa fa-book grey-text" style="font-size: 1rem;"></i>
													</div>
												</div>
												<input type="text" class="form-control" name="country"
													placeholder="Enter country Name"
													value="<%=DataUtility.getStringData(dto.getCountry())%>">
											</div>
										</div>
										<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("country", request)%></font></br>

										<span class="pl-sm-5"><b>status</b><span
											style="color: red;">*</span></span> </br>
										<div class="col-sm-12">
											<div class="input-group">
												<div class="input-group-prepend">
													<div class="input-group-text">
														<i class="fa fa-user-alt grey-text"
															style="font-size: 1rem;"></i>
													</div>
												</div>
												<%
													HashMap status = new HashMap();
													status.put("active", "active");
													status.put("inactive", "inactive");
												%>
												<%=HTMLUtility.getList("status", dto.getStatus(), status)%>
											</div>
										</div>
										<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("status", request)%></font></br>
										</br>
										<%
											if (id > 0) {
										%>
										<div class="text-center">

											<input type="submit" name="operation"
												class="btn btn-success btn-md" style="font-size: 17px"
												value="<%=LocationCtl.OP_UPDATE%>"> <input
												type="submit" name="operation"
												class="btn btn-warning btn-md" style="font-size: 17px"
												value="<%=LocationCtl.OP_CANCEL%>">
										</div>
										<%
											} else {
										%>
										<div class="text-center">

											<input type="submit" name="operation"
												class="btn btn-success btn-md" style="font-size: 17px"
												value="<%=LocationCtl.OP_SAVE%>"> <input type="submit"
												name="operation" class="btn btn-warning btn-md"
												style="font-size: 17px" value="<%=LocationCtl.OP_RESET%>">
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


	</div>
	<%@ include file="calendar.jsp"%>
</body>
<%@include file="FooterView.jsp"%>

</html>