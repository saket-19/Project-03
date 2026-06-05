package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.HospitalSystemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.HospitalSystemModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * HospitalSystem functionality controller.to perform add,delete and update operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/HospitalSystemCtl" })
public class HospitalSystemCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(HospitalSystemCtl.class);

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("patientName"))) {
			request.setAttribute("patientName", PropertyReader.getValue("error.require", "Patient Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("doctorName"))) {
			request.setAttribute("doctorName", PropertyReader.getValue("error.require", "Doctor Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("disease"))) {
			request.setAttribute("disease", PropertyReader.getValue("error.require", "Disease"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("room"))) {
			request.setAttribute("room", PropertyReader.getValue("error.require", "Room"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		HospitalSystemDTO dto = new HospitalSystemDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setPatientName(DataUtility.getString(request.getParameter("patientName")));
		dto.setDoctorName(DataUtility.getString(request.getParameter("doctorName")));
		dto.setDisease(DataUtility.getString(request.getParameter("disease")));
		dto.setRoom(DataUtility.getString(request.getParameter("room")));

		populateBean(dto, request);

		log.debug("HospitalSystemCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("HospitalSystemCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		HospitalSystemModelInt model = ModelFactory.getInstance().getHospitalSystemModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			HospitalSystemDTO dto = null;
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
		HospitalSystemModelInt model = ModelFactory.getInstance().getHospitalSystemModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			HospitalSystemDTO dto = (HospitalSystemDTO) populateDTO(request);
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
						ServletUtility.setErrorMessage("Record already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Record already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			HospitalSystemDTO dto = (HospitalSystemDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.HOSPITAL_SYSTEM_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.HOSPITAL_SYSTEM_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.HOSPITAL_SYSTEM_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("HospitalSystemCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.HOSPITAL_SYSTEM_VIEW;
	}
}