package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EnrollmentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EnrollmentModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/EnrollmentCtl" })
public class EnrollmentCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("enrollmentNo"))) {
			request.setAttribute("enrollmentNo", PropertyReader.getValue("error.require", "Enrollment No"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("studentName"))) {
			request.setAttribute("studentName", PropertyReader.getValue("error.require", "Student Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("course"))) {
			request.setAttribute("course", PropertyReader.getValue("error.require", "Course"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("enrollmentDate"))) {
			request.setAttribute("enrollmentDate", PropertyReader.getValue("error.require", "Enrollment Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("enrollmentDate"))) {
			request.setAttribute("enrollmentDate", PropertyReader.getValue("error.date", "Enrollment Date"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		EnrollmentDTO dto = new EnrollmentDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setEnrollmentNo(DataUtility.getString(request.getParameter("enrollmentNo")));
		dto.setStudentName(DataUtility.getString(request.getParameter("studentName")));
		dto.setCourse(DataUtility.getString(request.getParameter("course")));
		dto.setEnrollmentDate(DataUtility.getDate(request.getParameter("enrollmentDate")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		EnrollmentModelInt model = ModelFactory.getInstance().getEnrollmentModel();

		if (id > 0 || op != null) {

			EnrollmentDTO dto;

			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		EnrollmentModelInt model = ModelFactory.getInstance().getEnrollmentModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			EnrollmentDTO dto = (EnrollmentDTO) populateDTO(request);

			try {

				if (id > 0) {

					model.update(dto);
					ServletUtility.setSuccessMessage("Data updated successfully", request);
					ServletUtility.setDto(dto, request);

				} else {

					try {
						model.add(dto);
						ServletUtility.setSuccessMessage("Data saved successfully", request);

					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Enrollment already exists", request);
					}
				}

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Enrollment already exists", request);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			EnrollmentDTO dto = (EnrollmentDTO) populateDTO(request);

			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.ENROLLMENT_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ENROLLMENT_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ENROLLMENT_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.ENROLLMENT_VIEW;
	}
}