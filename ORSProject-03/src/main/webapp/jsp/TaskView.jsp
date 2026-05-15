<%@page import="in.co.rays.project_3.controller.TaskCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Task View</title>
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
		<form action="<%=ORSView.TASK_CTL%>" method="post">

			<div class="row pt-3 pb-3">
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">

					<jsp:useBean id="dto"
						class="in.co.rays.project_3.dto.TaskDTO"
						scope="request"></jsp:useBean>

					<div class="card">
						<div class="card-body">

							<%
								long id = DataUtility.getLong(request.getParameter("id"));

								if (dto.getId() != null && dto.getId() > 0) {
							%>
							<h3 class="text-center text-primary">Update Task</h3>
							<%
								} else {
							%>
							<h3 class="text-center text-primary">Add Task</h3>
							<%
								}
							%>

							<!-- Success Message -->
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

							<!-- Error Message -->
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

							<%
								HashMap statusList = (HashMap) request.getAttribute("statusList");
							%>

							<input type="hidden" name="id"
								value="<%=dto.getId()%>">
							<input type="hidden" name="createdBy"
								value="<%=dto.getCreatedBy()%>">
							<input type="hidden" name="modifiedBy"
								value="<%=dto.getModifiedBy()%>">
							<input type="hidden" name="createdDatetime"
								value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
							<input type="hidden" name="modifiedDatetime"
								value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

							<div class="md-form">

								<!-- Task Code -->
								<span class="pl-sm-5"><b>Task Code</b><span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-book grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control"
											name="taskCode"
											placeholder="Enter Task Code"
											value="<%=DataUtility.getStringData(dto.getTaskCode())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5">
									<%=ServletUtility.getErrorMessage("taskCode", request)%>
								</font></br>

								<!-- Task -->
								<span class="pl-sm-5"><b>Task</b><span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-tasks grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control"
											name="task"
											placeholder="Enter Task"
											value="<%=DataUtility.getStringData(dto.getTask())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5">
									<%=ServletUtility.getErrorMessage("task", request)%>
								</font></br>

								<!-- Assigned To -->
								<span class="pl-sm-5"><b>Assigned To</b><span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control"
											name="assignedTo"
											placeholder="Enter Assigned To"
											value="<%=DataUtility.getStringData(dto.getAssignedTo())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5">
									<%=ServletUtility.getErrorMessage("assignedTo", request)%>
								</font></br>

								
<span class="pl-sm-5"><b>Due Date</b><span style="color: red;">*</span></span></br>
<div class="col-sm-12">
    <div class="input-group">
        <div class="input-group-prepend">
            <div class="input-group-text">
                <i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
            </div>
        </div>

        <input type="text"
               class="form-control"
               id="udate6"
               name="dueDate"
               value="<%=DataUtility.getDateString(dto.getDueDate())%>">
    </div>
</div>

<font color="red" class="pl-sm-5">
    <%=ServletUtility.getErrorMessage("dueDate", request)%>
</font></br>

								<!-- Task Status -->
								<span class="pl-sm-5"><b>Task Status</b><span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-info-circle grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>
										<%=HTMLUtility.getList("taskStatus",
												dto.getTaskStatus(),
												statusList)%>
									</div>
								</div>
								<font color="red" class="pl-sm-5">
									<%=ServletUtility.getErrorMessage("taskStatus", request)%>
								</font></br></br>

								<%
									if (id > 0) {
								%>
								<div class="text-center">
									<input type="submit" name="operation"
										class="btn btn-success btn-md"
										style="font-size: 17px"
										value="<%=TaskCtl.OP_UPDATE%>">

									<input type="submit" name="operation"
										class="btn btn-warning btn-md"
										style="font-size: 17px"
										value="<%=TaskCtl.OP_CANCEL%>">
								</div>
								<%
									} else {
								%>
								<div class="text-center">
									<input type="submit" name="operation"
										class="btn btn-success btn-md"
										style="font-size: 17px"
										value="<%=TaskCtl.OP_SAVE%>">

									<input type="submit" name="operation"
										class="btn btn-warning btn-md"
										style="font-size: 17px"
										value="<%=TaskCtl.OP_RESET%>">
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

<%@ include file="calendar.jsp" %>
</body>
<%@include file="FooterView.jsp"%>
</html>