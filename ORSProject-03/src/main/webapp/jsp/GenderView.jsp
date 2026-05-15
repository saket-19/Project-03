<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.controller.GenderCtl"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gender view</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	 padding-bottom: 11px; 
	 background-color: #ebebe0;
}
.input-group-addon{
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
		<%@include file="calendar.jsp" %>
	</div>
	<div>
		<main>
		<form action="<%=ORSView.GENDER_CTL%>" method="post">
			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.GenderDTO" scope="request"></jsp:useBean>
			<div class="row pt-3">
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">
					<div class="card input-group-addon">
						<div class="card-body">

							<%
							  long id = DataUtility.getLong(request.getParameter("id"));
								if (dto.getGenderCode() != null && dto.getId() > 0) {
							%>
							<h3 class="text-center default-text text-primary">UPDATE GENDER</h3>
							<%
								} else {
							%>
							<h3 class="text-center default-text text-primary">ADD GENDER</h3>
							<%
								}
							%>

							<div>
								<%
									HashMap statusMap = (HashMap) request.getAttribute("statusList");
								%>
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
								<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
								<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">
							</div>

							<div class="md-form">

	<span class="pl-sm-5"><b>Gender Code</b>
	<span style="color: red;">*</span></span> </br>
	<div class="col-sm-12">
      <div class="input-group">
        <div class="input-group-prepend">
          <div class="input-group-text"><i class="fa fa-code grey-text" style="font-size: 1rem;"></i> </div>
        </div>
        <input type="text" class="form-control" name="genderCode" placeholder="Gender Code" value="<%=DataUtility.getStringData(dto.getGenderCode())%>">
      </div>
    </div>
	<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("genderCode", request)%></font></br>

	<span class="pl-sm-5"><b>Gender Type</b>
	<span style="color: red;">*</span></span> </br>
	<div class="col-sm-12">
      <div class="input-group">
        <div class="input-group-prepend">
          <div class="input-group-text"><i class="fa fa-venus-mars grey-text" style="font-size: 1rem;"></i> </div>
        </div>
        <input type="text" class="form-control" name="genderType" placeholder="Gender Type" value="<%=DataUtility.getStringData(dto.getGenderType())%>">
      </div>
    </div>
	<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("genderType", request)%></font></br>

	<span class="pl-sm-5"><b>Description</b>
	<span style="color: red;">*</span></span> </br>
	<div class="col-sm-12">
      <div class="input-group">
        <div class="input-group-prepend">
          <div class="input-group-text"><i class="fa fa-info grey-text" style="font-size: 1rem;"></i> </div>
        </div>
        <input type="text" class="form-control" name="description" placeholder="Description" value="<%=DataUtility.getStringData(dto.getDescription())%>">
      </div>
    </div>
	<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("description", request)%></font></br>

	<span class="pl-sm-5"><b>Status</b>
	<span style="color: red;">*</span></span> </br>
	<div class="col-sm-12">
      <div class="input-group">
        <div class="input-group-prepend">
          <div class="input-group-text"><i class="fa fa-info-circle grey-text" style="font-size: 1rem;"></i> </div>
        </div>
        <%=HTMLUtility.getList("status", dto.getStatus(), statusMap)%>
      </div>
    </div>
	<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("status", request)%></font></br>

							<%
								if (dto.getGenderCode() != null && dto.getId() > 0) {
							%>
							<div class="text-center">
								<input type="submit" name="operation" class="btn btn-success btn-md"
								       style="font-size: 17px"
								       value="<%=GenderCtl.OP_UPDATE%>">
								<input type="submit" name="operation" class="btn btn-warning btn-md"
									   style="font-size: 17px"
									   value="<%=GenderCtl.OP_CANCEL%>">
							</div>
							<%
								} else {
							%>
							<div class="text-center">
								<input type="submit" name="operation"
									   class="btn btn-success btn-md" style="font-size: 17px"
									   value="<%=GenderCtl.OP_SAVE%>">
								<input type="submit" name="operation"
								       class="btn btn-warning btn-md"
									   style="font-size: 17px" value="<%=GenderCtl.OP_RESET%>">
							</div>
							<%
								}
							%>

						</div>
					</div>
				</div>
		</form>
		</main>
		<div class="col-md-4 mb-4"></div>
	</div>

</body>
<%@include file="FooterView.jsp"%>
</body>
</html>