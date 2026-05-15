package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.UserProfileDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.UserProfileModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * UserProfile functionality controller.to perform add,delete and update
 * operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/UserProfileCtl" })
public class UserProfileCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UserProfileCtl.class);

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("profileCode"))) {
			request.setAttribute("profileCode", PropertyReader.getValue("error.require", "Profile Code"));
			pass = false;
		} else if (!DataValidator.isCode(request.getParameter("profileCode"))) {

			request.setAttribute("profileCode", "Profile Code must be in format like AB-101");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("userName"))) {
			request.setAttribute("userName", PropertyReader.getValue("error.require", "User Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobileNumber"))) {
			request.setAttribute("mobileNumber", PropertyReader.getValue("error.require", "Mobile Number"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobileNumber"))) {
			request.setAttribute("mobileNumber", "Please Enter Valid Mobile Number");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("status"))) {
			request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		UserProfileDTO dto = new UserProfileDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setProfileCode(DataUtility.getString(request.getParameter("profileCode")));
		dto.setUserName(DataUtility.getString(request.getParameter("userName")));
		dto.setMobileNumber(DataUtility.getString(request.getParameter("mobileNumber")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		populateBean(dto, request);

		log.debug("UserProfileCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("UserProfileCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserProfileModelInt model = ModelFactory.getInstance().getUserProfileModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			UserProfileDTO dto = null;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserProfileModelInt model = ModelFactory.getInstance().getUserProfileModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			UserProfileDTO dto = (UserProfileDTO) populateDTO(request);
			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);
					ServletUtility.setDto(dto, request);
				} else {

					try {
						model.add(dto);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleDBDown(getView(), request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Profile code already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Profile code already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			UserProfileDTO dto = (UserProfileDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.USER_PROFILE_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_PROFILE_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_PROFILE_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("UserProfileCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.USER_PROFILE_VIEW;
	}
}