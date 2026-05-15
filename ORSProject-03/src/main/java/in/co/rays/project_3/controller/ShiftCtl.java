package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ShiftDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ShiftModelHibImpl;
import in.co.rays.project_3.model.ShiftModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "ShiftCtl", urlPatterns = { "/ctl/ShiftCtl" })
public class ShiftCtl extends BaseCtl {



	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("code"))) {
			request.setAttribute("code",
					PropertyReader.getValue("error.require", "Code"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name",
					PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("startTime"))) {
			request.setAttribute("startTime",
					PropertyReader.getValue("error.require", "Start Time"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("endTime"))) {
			request.setAttribute("endTime",
					PropertyReader.getValue("error.require", "End Time"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		ShiftDTO dto = new ShiftDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setCode(DataUtility.getString(request.getParameter("code")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setStartTime(DataUtility.getString(request.getParameter("startTime")));
		dto.setEndTime(DataUtility.getString(request.getParameter("endTime")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long id = DataUtility.getLong(request.getParameter("id"));
		ShiftModelInt model = new ShiftModelHibImpl();

		if (id > 0) {
			try {
				ShiftDTO dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		ShiftModelInt model = new ShiftModelHibImpl();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {

			ShiftDTO dto = (ShiftDTO) populateDTO(request);

			try {
				model.add(dto);
				ServletUtility.setSuccessMessage("Shift added successfully", request);
				ServletUtility.forward(getView(), request, response);
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage("Shift Code already exists", request);
				ServletUtility.forward(getView(), request, response);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

		else if (OP_UPDATE.equalsIgnoreCase(op)) {

			ShiftDTO dto = (ShiftDTO) populateDTO(request);

			try {
				model.update(dto);
				ServletUtility.setSuccessMessage("Shift updated successfully", request);
				ServletUtility.forward(getView(), request, response);
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage("Shift Code already exists", request);
				ServletUtility.forward(getView(), request, response);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {

			ShiftDTO dto = (ShiftDTO) populateDTO(request);

			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.SHIFT_LIST_CTL, request, response);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

		else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SHIFT_LIST_CTL, request, response);
		}

		else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SHIFT_CTL, request, response);
		}
	}

	@Override
	protected String getView() {
		return ORSView.SHIFT_VIEW;
	}
}