<%@page import="in.co.rays.project_3.controller.SupportTicketCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Support Ticket View</title>
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
    <div>
        <main>
        <form action="<%=ORSView.SUPPORT_TICKET_CTL%>" method="post">

            <div class="row pt-3 pb-3">
                <div class="col-md-4 mb-4"></div>
                <div class="col-md-4 mb-4">
                    <jsp:useBean id="dto" class="in.co.rays.project_3.dto.SupportTicketDTO"
                        scope="request"></jsp:useBean>
                    <div class="card">
                        <div class="card-body">
                            <%
                                long id = DataUtility.getLong(request.getParameter("id"));
                                if (dto.getId() != null && dto.getId() > 0) {
                            %>
                            <h3 class="text-center text-primary">Update Support Ticket</h3>
                            <%
                                } else {
                            %>
                            <h3 class="text-center text-primary">Add Support Ticket</h3>
                            <%
                                }
                            %>

                            <div>
                                <H4 align="center">
                                    <% if (!ServletUtility.getSuccessMessage(request).equals("")) { %>
                                    <div class="alert alert-success alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <%=ServletUtility.getSuccessMessage(request)%>
                                    </div>
                                    <% } %>
                                </H4>
                                <H4 align="center">
                                    <% if (!ServletUtility.getErrorMessage(request).equals("")) { %>
                                    <div class="alert alert-danger alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <%=ServletUtility.getErrorMessage(request)%>
                                    </div>
                                    <% } %>
                                </H4>

                                <%
                                    HashMap statusL = (HashMap) request.getAttribute("statusL");
                                %>

                                <input type="hidden" name="id" value="<%=dto.getId()%>">
                                <input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
                                <input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
                                <input type="hidden" name="createdDatetime"
                                    value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
                                <input type="hidden" name="modifiedDatetime"
                                    value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">
                            </div>

                            <div class="md-form">

                                <!-- Ticket No -->
                                <span class="pl-sm-5"><b>Ticket No</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-ticket-alt grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="TicketNo"
                                            placeholder="Enter Ticket No"
                                            value="<%=DataUtility.getStringData(dto.getTicketNo())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("TicketNo", request)%></font></br>

                                <!-- Issue -->
                                <span class="pl-sm-5"><b>Issue</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-exclamation-circle grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="issue"
                                            placeholder="Enter Issue"
                                            value="<%=DataUtility.getStringData(dto.getIssue())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("issue", request)%></font></br>

                                <!-- Created Date -->
                                <span class="pl-sm-5"><b>Created Date</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control udate6" name="createdDate"
                                            placeholder="Enter Created Date" id="udate6" readonly="readonly"
                                            value="<%=DataUtility.getDateString(dto.getCreatedDate())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("createdDate", request)%></font></br>

                                <!-- Status -->
                                <span class="pl-sm-5"><b>Status</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-info-circle grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <%=HTMLUtility.getList("status", dto.getStatus(), statusL)%>
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("status", request)%></font></br>

                                <% if (id > 0) { %>
                                <div class="text-center">
                                    <input type="submit" name="operation"
                                        class="btn btn-success btn-md" style="font-size: 17px"
                                        value="<%=SupportTicketCtl.OP_UPDATE%>">
                                    <input type="submit" name="operation"
                                        class="btn btn-warning btn-md" style="font-size: 17px"
                                        value="<%=SupportTicketCtl.OP_CANCEL%>">
                                </div>
                                <% } else { %>
                                <div class="text-center">
                                    <input type="submit" name="operation"
                                        class="btn btn-success btn-md" style="font-size: 17px"
                                        value="<%=SupportTicketCtl.OP_SAVE%>">
                                    <input type="submit" name="operation"
                                        class="btn btn-warning btn-md" style="font-size: 17px"
                                        value="<%=SupportTicketCtl.OP_RESET%>">
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-4"></div>
                </div>
            </div>
        </form>
        </main>
    </div>
<%@ include file="calendar.jsp"%>
</body>
<%@include file="FooterView.jsp"%>
</html>