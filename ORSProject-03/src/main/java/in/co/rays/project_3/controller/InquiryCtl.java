package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.InquiryDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.InquiryModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/InquiryCtl" })
public class InquiryCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull (request.getParameter("inquirerName"))) {
			request.setAttribute("inquirerName",
					PropertyReader.getValue("error.require", "Inquirer Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("email"))) {
			request.setAttribute("email",
					PropertyReader.getValue("error.require", "Email"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("email"))) {
			request.setAttribute("email",
					PropertyReader.getValue("error.email", "Email"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("inquirySubject"))) {
			request.setAttribute("inquirySubject",
					PropertyReader.getValue("error.require", "Inquiry Subject"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("inquiryStatus"))) {
			request.setAttribute("inquiryStatus",
					PropertyReader.getValue("error.require", "Inquiry Status"));
			pass = false;
		}

		return pass;
	}


	protected BaseDTO populateDTO(HttpServletRequest request) {

		InquiryDTO dto = new InquiryDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setInquirerName(DataUtility.getString(request.getParameter("inquirerName")));
		dto.setEmail(DataUtility.getString(request.getParameter("email")));
		dto.setInquirySubject(DataUtility.getString(request.getParameter("inquirySubject")));
		dto.setInquiryStatus(DataUtility.getString(request.getParameter("inquiryStatus")));

		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		InquiryModelInt model = ModelFactory.getInstance().getInquiryModel();

		if (id > 0 || op != null) {
			try {
				InquiryDTO dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		InquiryModelInt model = ModelFactory.getInstance().getInquiryModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			InquiryDTO dto = (InquiryDTO) populateDTO(request);

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Data is successfully Saved", request);
				}
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("email id already exists", request);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			InquiryDTO dto = (InquiryDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.INQUIRY_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INQUIRY_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INQUIRY_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.INQUIRY_VIEW;
	}
}
