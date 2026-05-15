package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.MobileVerificationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.MobileVerificationModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.HTMLUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * MobileVerification functionality controller.to perform add,delete and update operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/MobileVerificationCtl" })
public class MobileVerificationCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MobileVerificationCtl.class);

	protected void preload(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MobileVerificationDTO.ACTIVE, MobileVerificationDTO.ACTIVE);
		map.put(MobileVerificationDTO.INACTIVE, MobileVerificationDTO.INACTIVE);
		request.setAttribute("statusList", map);
	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("verificationCode"))) {
			request.setAttribute("verificationCode", PropertyReader.getValue("error.require", "Verification Code"));
			pass = false;
		}
		else if (!DataValidator.isCode(request.getParameter("verificationCode"))) {

			request.setAttribute("verificationCode",
					"verification Code must be in format AB-123");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobileNumber"))) {
			request.setAttribute("mobileNumber", PropertyReader.getValue("error.require", "Mobile Number"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobileNumber"))) {
			request.setAttribute("mobileNumber", "Please Enter Valid Mobile Number");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("otp"))) {
			request.setAttribute("otp", PropertyReader.getValue("error.require", "OTP"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("status"))) {
			request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		MobileVerificationDTO dto = new MobileVerificationDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setVerificationCode(DataUtility.getString(request.getParameter("verificationCode")));
		dto.setMobileNumber(DataUtility.getString(request.getParameter("mobileNumber")));
		dto.setOtp(DataUtility.getString(request.getParameter("otp")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		populateBean(dto, request);

		log.debug("MobileVerificationCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("MobileVerificationCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		MobileVerificationModelInt model = ModelFactory.getInstance().getMobileVerificationModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			MobileVerificationDTO dto = null;
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
		MobileVerificationModelInt model = ModelFactory.getInstance().getMobileVerificationModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			MobileVerificationDTO dto = (MobileVerificationDTO) populateDTO(request);
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
						ServletUtility.setErrorMessage("Verification code already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Verification code already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			MobileVerificationDTO dto = (MobileVerificationDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.MOBILE_VERIFICATION_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.MOBILE_VERIFICATION_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.MOBILE_VERIFICATION_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("MobileVerificationCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.MOBILE_VERIFICATION_VIEW;
	}
}