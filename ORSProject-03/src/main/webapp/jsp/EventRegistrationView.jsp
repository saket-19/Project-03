<%@page import="in.co.rays.project_3.controller.EventRegistrationCtl"%>
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
<title>Event Registration View</title>
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
        <form action="<%=ORSView.EVENT_REGISTRATION_CTL%>" method="post">

            <div class="row pt-3 pb-3">
                <div class="col-md-4 mb-4"></div>
                <div class="col-md-4 mb-4">
                    <jsp:useBean id="dto" class="in.co.rays.project_3.dto.EventRegistrationDTO"
                        scope="request"></jsp:useBean>
                    <div class="card">
                        <div class="card-body">
                            <%
                                long id = DataUtility.getLong(request.getParameter("id"));

                                if (dto.getId() != null && dto.getId() > 0) {
                            %>
                            <h3 class="text-center text-primary">Update Event Registration</h3>
                            <%
                                } else {
                            %>
                            <h3 class="text-center text-primary">Add Event Registration</h3>
                            <%
                                }
                            %>

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

                                <%
                                    HashMap ticketType = (HashMap) request.getAttribute("ticketTypeL");
                                    HashMap paymentMode = (HashMap) request.getAttribute("paymentModeL");
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

                                <!-- Participant -->
                                <span class="pl-sm-5"><b>Participant</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-user grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="participant"
                                            placeholder="Enter Participant Name"
                                            value="<%=DataUtility.getStringData(dto.getParticipant())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("participant", request)%></font></br>

                                <!-- Event Name -->
                                <span class="pl-sm-5"><b>Event Name</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-book grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="eventName"
                                            placeholder="Enter Event Name"
                                            value="<%=DataUtility.getStringData(dto.getEventName())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("eventName", request)%></font></br>

                                <!-- Registration Date -->
                                <span class="pl-sm-5"><b>Registration Date</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control udate6" name="registrationDate"
                                            placeholder="Enter Registration Date" id="udate6" readonly="readonly"
                                            value="<%=DataUtility.getDateString(dto.getRegistrationDate())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("registrationDate", request)%></font></br>

                                <!-- Ticket Type -->
                                <span class="pl-sm-5"><b>Ticket Type</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-ticket-alt grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <%=HTMLUtility.getList("ticketType", dto.getTicketType(), ticketType)%>
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("ticketType", request)%></font></br>

                                <!-- Payment Mode -->
                                <span class="pl-sm-5"><b>Payment Mode</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-credit-card grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <%=HTMLUtility.getList("paymentMode", dto.getPaymentMode(), paymentMode)%>
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("paymentMode", request)%></font></br>

                                <!-- Seat Number -->
                                <span class="pl-sm-5"><b>Seat Number</b><span style="color: red;">*</span></span></br>
                                <div class="col-sm-12">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fa fa-chair grey-text" style="font-size: 1rem;"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="seatNumber"
                                            placeholder="Enter Seat Number"
                                            value="<%=DataUtility.getStringData(dto.getSeatNumber())%>">
                                    </div>
                                </div>
                                <font color="red" class="pl-sm-5"><%=ServletUtility.getErrorMessage("seatNumber", request)%></font></br>

                                <%
                                    if (id > 0) {
                                %>
                                <div class="text-center">
                                    <input type="submit" name="operation"
                                        class="btn btn-success btn-md" style="font-size: 17px"
                                        value="<%=EventRegistrationCtl.OP_UPDATE%>">
                                    <input type="submit" name="operation"
                                        class="btn btn-warning btn-md" style="font-size: 17px"
                                        value="<%=EventRegistrationCtl.OP_CANCEL%>">
                                </div>
                                <%
                                    } else {
                                %>
                                <div class="text-center">
                                    <input type="submit" name="operation"
                                        class="btn btn-success btn-md" style="font-size: 17px"
                                        value="<%=EventRegistrationCtl.OP_SAVE%>">
                                    <input type="submit" name="operation"
                                        class="btn btn-warning btn-md" style="font-size: 17px"
                                        value="<%=EventRegistrationCtl.OP_RESET%>">
                                </div>
                                <%
                                    }
                                %>
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