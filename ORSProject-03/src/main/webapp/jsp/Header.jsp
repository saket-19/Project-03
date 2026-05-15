\<%@page import="in.co.rays.project_3.controller.LoginCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="in.co.rays.project_3.dto.RoleDTO"%>
<%@page import="in.co.rays.project_3.dto.UserDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Header</title>

<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.6.3/css/all.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

<style>
.aj {
	background-image: linear-gradient(to bottom right, white, #4f879a);
}

.student-nav-fix .navbar-nav {
	align-items: center !important;
}

.student-nav-fix .dropdown-menu a {
	color: #000 !important;
}

.student-nav-fix .nav-link {
	color: #fff !important;
}
</style>
</head>

<body>

	<%
	UserDTO userDto = (UserDTO) session.getAttribute("user");
	boolean userLoggedIn = userDto != null;

	String welcomeMsg = "Hi, ";
	if (userLoggedIn) {
		String role = (String) session.getAttribute("role");
		welcomeMsg += userDto.getFirstName() + " (" + role + ")";
	} else {
		welcomeMsg += "Guest";
	}
	%>

	<nav
		class="navbar navbar-expand-lg fixed-top aj 
    <%if (userLoggedIn && userDto.getRoleId() == RoleDTO.STUDENT) {%>
        student-nav-fix
    <%}%>">

		<a class="navbar-brand" href="<%=ORSView.WELCOME_CTL%>"> <img
			src="<%=ORSView.APP_CONTEXT%>/img/custom.png" height="50px">
		</a>

		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"> <i class="fa fa-bars"
				style="color: #fff; font-size: 28px;"></i>
			</span>
		</button>

		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ml-auto">

				<%
				if (userLoggedIn) {
				%>

				<%
				if (userDto.getRoleId() == RoleDTO.STUDENT) {
				%>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">
						<span style="color: white;">Marksheet</span>
				</a>
					<div class="dropdown-menu">
						<a class="dropdown-item"
							href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>"> <i
							class="fa fa-file-alt"></i> Marksheet Merit List
						</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">
						<span style="color: white;">User</span>
				</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.MY_PROFILE_CTL%>"> <i
							class="fa fa-user-tie"></i> My Profile
						</a> <a class="dropdown-item" href="<%=ORSView.CHANGE_PASSWORD_CTL%>">
							<i class="fa fa-edit"></i> Change Password
						</a>
					</div></li>

				<%
				} else if (userDto.getRoleId() == RoleDTO.ADMIN) {
				%>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">User</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.USER_CTL%>"><i
							class="fa fa-user-circle"></i>Add User</a> <a class="dropdown-item"
							href="<%=ORSView.USER_LIST_CTL%>"><i
							class="fa fa-user-friends"></i>User List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Marksheet</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.MARKSHEET_CTL%>"><i
							class="fa fa-file"></i>Add Marksheet</a> <a class="dropdown-item"
							href="<%=ORSView.MARKSHEET_LIST_CTL%>"><i class="fa fa-paste"></i>Marksheet
							List</a> <a class="dropdown-item"
							href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>"><i
							class="fa fa-file-alt"></i>Marksheet Merit List</a> <a
							class="dropdown-item" href="<%=ORSView.GET_MARKSHEET_CTL%>"><i
							class="fa fa-copy"></i>Get Marksheet</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Role</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.ROLE_CTL%>"><i
							class="fa fa-user-tie"></i>Add Role</a> <a class="dropdown-item"
							href="<%=ORSView.ROLE_LIST_CTL%>"><i
							class="fa fa-user-friends"></i>Role List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">College</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.COLLEGE_CTL%>"><i
							class="fa fa-university"></i>Add College</a> <a class="dropdown-item"
							href="<%=ORSView.COLLEGE_LIST_CTL%>"><i
							class="fa fa-building"></i>College List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Course</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.COURSE_CTL%>"><i
							class="fa fa-book-open"></i>Add Course</a> <a class="dropdown-item"
							href="<%=ORSView.COURSE_LIST_CTL%>"><i
							class="fa fa-sort-amount-down"></i>Course List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Student</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.STUDENT_CTL%>"><i
							class="fa fa-user-circle"></i>Add Student</a> <a
							class="dropdown-item" href="<%=ORSView.STUDENT_LIST_CTL%>"><i
							class="fa fa-users"></i>Student List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Faculty</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.FACULTY_CTL%>"><i
							class="fa fa-user-tie"></i>Add Faculty</a> <a class="dropdown-item"
							href="<%=ORSView.FACULTY_LIST_CTL%>"><i class="fa fa-users"></i>Faculty
							List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Time Table</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.TIMETABLE_CTL%>"><i
							class="fa fa-clock"></i>Add TimeTable</a> <a class="dropdown-item"
							href="<%=ORSView.TIMETABLE_LIST_CTL%>"><i class="fa fa-clock"></i>TimeTable
							List</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Subject</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.SUBJECT_CTL%>"><i
							class="fa fa-calculator"></i>Add Subject</a> <a class="dropdown-item"
							href="<%=ORSView.SUBJECT_LIST_CTL%>"><i
							class="fa fa-sort-amount-down"></i>Subject List</a>
					</div></li>
				<!-- usercase -->
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">USECASES</a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.BATCH_CTL%>"><i
							class="fa fa-plus-circle"></i>Add Batch</a> <a class="dropdown-item"
							href="<%=ORSView.BATCH_LIST_CTL%>"><i class="fa fa-list"></i>Batch
							List</a><a class="dropdown-item" href="<%=ORSView.USER_PROFILE_CTL%>">
							<i class="fa fa-user"></i> Add UserProfile
						</a> <a class="dropdown-item"
							href="<%=ORSView.USER_PROFILE_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> User Profile List
						</a><a class="dropdown-item"
							href="<%=ORSView.MOBILE_VERIFICATION_CTL%>"> <i
							class="fa fa-mobile"></i> Add Mobile Verification
						</a> <a class="dropdown-item"
							href="<%=ORSView.MOBILE_VERIFICATION_LIST_CTL%>"> <i
							class="fa fa-list"></i> Mobile Verification List
						</a><a class="dropdown-item" href="<%=ORSView.GENDER_CTL%>"> <i
							class="fa fa-venus-mars"></i> Add Gender
						</a> <a class="dropdown-item" href="<%=ORSView.GENDER_LIST_CTL%>">
							<i class="fa fa-list"></i> Gender List
						</a><a class="dropdown-item" href="<%=ORSView.VOICE_COMMAND_CTL%>">
							<i class="fa fa-microphone"></i> Add Voice Command
						</a> <a class="dropdown-item"
							href="<%=ORSView.VOICE_COMMAND_LIST_CTL%>"> <i
							class="fa fa-list"></i> Voice Command List
						</a><a class="dropdown-item" href="<%=ORSView.CHAT_ROOM_CTL%>"> <i
							class="fa fa-comments"></i> Add Chat Room
						</a> <a class="dropdown-item" href="<%=ORSView.CHAT_ROOM_LIST_CTL%>">
							<i class="fa fa-list"></i> Chat Room List
						</a>
					</div></li>


				<%-- <li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">USECASES 2</a>
					<div class="dropdown-menu">

						<a class="dropdown-item" href="<%=ORSView.TASK_CTL%>"> <i
							class="fa fa-id-card"></i> Add Task
						</a> <a class="dropdown-item" href="<%=ORSView.TASK_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> Task List
						</a> <a class="dropdown-item" href="<%=ORSView.INQUIRY_CTL%>"> <i
							class="fa fa-id-card"></i> Add Inquiry
						</a> <a class="dropdown-item" href="<%=ORSView.INQUIRY_LIST_CTL%>">
							<i class="fa fa-sort-amount-down"></i> Inquiry List
						</a> <a class="dropdown-item"
							href="<%=ORSView.EVENT_REGISTRATION_CTL%>"> <i
							class="fa fa-id-card"></i> Event Registration
						</a> <a class="dropdown-item"
							href="<%=ORSView.EVENT_REGISTRATION_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> Event Registration List
						</a> <a class="dropdown-item" href="<%=ORSView.SUPPORT_TICKET_CTL%>">
							<i class="fa fa-id-card"></i> Add Support Ticket
						</a> <a class="dropdown-item"
							href="<%=ORSView.SUPPORT_TICKET_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> Support Ticket List
						</a>
						<a class="dropdown-item" href="<%=ORSView.EMI_CTL%>">
							<i class="fa fa-id-card"></i> Add EMI
						</a> <a class="dropdown-item"
							href="<%=ORSView.EMI_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> EMI List
						</a>
						<a class="dropdown-item" href="<%=ORSView.PARKING_CTL%>">
							<i class="fa fa-id-card"></i> Add Parking
						</a> <a class="dropdown-item"
							href="<%=ORSView.PARKING_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> Parking List
						</a>
							<a class="dropdown-item" href="<%=ORSView.ASSET_CTL%>">
							<i class="fa fa-id-card"></i> Add Asset
						</a> <a class="dropdown-item"
							href="<%=ORSView.ASSET_LIST_CTL%>"> <i
							class="fa fa-sort-amount-down"></i> Asset List
						</a>

					</div></li> --%>



				<%-- <li class="nav-item dropdown"><a class="nav-link dropdown-toggle"
                        href="#" data-toggle="dropdown" style="color:white;">Watchlist</a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="<%=ORSView.WATCHLIST_CTL%>"><i class="fa fa-calculator"></i>Watchlist</a>
                            <a class="dropdown-item" href="<%=ORSView.WATCHLIST_LIST_CTL%>"><i class="fa fa-sort-amount-down"></i>Watchlist List</a>
                        </div></li>
                        
                        <li class="nav-item dropdown"><a class="nav-link dropdown-toggle"
                        href="#" data-toggle="dropdown" style="color:white;">Event</a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="<%=ORSView.EVENT_CTL%>"><i class="fa fa-calculator"></i>Event</a>
                            <a class="dropdown-item" href="<%=ORSView.EVENT_LIST_CTL%>"><i class="fa fa-sort-amount-down"></i>Event List</a>
                        </div></li>
                        
                         <li class="nav-item dropdown"><a class="nav-link dropdown-toggle"
                        href="#" data-toggle="dropdown" style="color:white;">Payment</a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="<%=ORSView.PAYMENT_CTL%>"><i class="fa fa-calculator"></i>Add payment</a>
                            <a class="dropdown-item" href="<%=ORSView.PAYMENT_LIST_CTL%>"><i class="fa fa-sort-amount-down"></i>Payment List</a>
                        </div></li>
                        
                        <li class="nav-item dropdown"><a class="nav-link dropdown-toggle"
                        href="#" data-toggle="dropdown" style="color:white;">Location</a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="<%=ORSView.LOCATION_CTL%>"><i class="fa fa-calculator"></i>Add location</a>
                            <a class="dropdown-item" href="<%=ORSView.LOCATION_LIST_CTL%>"><i class="fa fa-sort-amount-down"></i>location List</a>
                        </div></li>
                          
                        <li class="nav-item dropdown"><a class="nav-link dropdown-toggle"
                        href="#" data-toggle="dropdown" style="color:white;">Profile</a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="<%=ORSView.PROFILE_CTL%>"><i class="fa fa-calculator"></i>Add profile</a>
                            <a class="dropdown-item" href="<%=ORSView.PROFILE_LIST_CTL%>"><i class="fa fa-sort-amount-down"></i>profile List</a>
                        </div></li> --%>

				<%-- <li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Maintenance</a>

					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.MAINTENANCE_CTL%>">
							<i class="fa fa-calculator"></i>Maintenance
						</a> <a class="dropdown-item" href="<%=ORSView.MAINTENANCE_LIST_CTL%>">
							<i class="fa fa-sort-amount-down"></i>Maintenance List
						</a>
					</div></li> --%>

				<%-- <li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">Client</a>

					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.CLIENT_CTL%>">
							<i class="fa fa-calculator"></i> Add Client
						</a> <a class="dropdown-item" href="<%=ORSView.CLIENT_LIST_CTL%>">
							<i class="fa fa-sort-amount-down"></i> Client List
						</a>
					</div></li> --%>
				<%-- 			<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;">shift</a>

					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.SHIFT_CTL%>">
							<i class="fa fa-calculator"></i> Add shift
						</a> <a class="dropdown-item" href="<%=ORSView.SHIFT_LIST_CTL%>">
							<i class="fa fa-sort-amount-down"></i> shift List
						</a>
					</div></li> --%>
				<%-- 	<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
					style="color: white;"> Enrollment </a>

					<div class="dropdown-menu">
						<a class="dropdown-item" href="<%=ORSView.ENROLLMENT_CTL%>"> <i
							class="fa fa-id-card"></i> Enrollment
						</a> <a class="dropdown-item" href="<%=ORSView.ENROLLMENT_LIST_CTL%>">
							<i class="fa fa-sort-amount-down"></i> Enrollment List
						</a>
					</div></li>
 --%>

				<%
				}
				%>

				<%
				}
				%>

				<!-- ✅ WELCOME DROPDOWN -->
				<li class="nav-item dropdown ml-3"><a
					class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">
						<span style="color: navy;"><b><%=welcomeMsg%></b></span>
				</a>
					<div class="dropdown-menu dropdown-menu-right">
						<%
						if (userLoggedIn) {
						%>
						<a class="dropdown-item"
							href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">
							<i class="fa fa-sign-out-alt"></i> Logout
						</a> <a class="dropdown-item" href="<%=ORSView.MY_PROFILE_CTL%>">
							<i class="fa fa-user-tie"></i> My Profile
						</a> <a class="dropdown-item" href="<%=ORSView.CHANGE_PASSWORD_CTL%>">
							<i class="fa fa-edit"></i> Change Password
						</a> <a class="dropdown-item" target="blank"
							href="<%=ORSView.JAVA_DOC_VIEW%>"> <i class="fa fa-clone"></i>
							Java Doc
						</a>
						<%
						} else {
						%>
						<a class="dropdown-item" href="<%=ORSView.LOGIN_CTL%>"> <i
							class="fa fa-sign-in-alt"></i> Login
						</a> <a class="dropdown-item"
							href="<%=ORSView.USER_REGISTRATION_CTL%>"> <i
							class="fa fa-registered"></i> User Registration
						</a>
						<%
						}
						%>
					</div></li>


			</ul>
		</div>
	</nav>

</body>
</html>
