package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.GenderDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.GenderModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Gender functionality controller.to perform add,delete and update operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/GenderCtl" })
public class GenderCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(GenderCtl.class);

	protected void preload(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(GenderDTO.ACTIVE, GenderDTO.ACTIVE);
		map.put(GenderDTO.INACTIVE, GenderDTO.INACTIVE);
		request.setAttribute("statusList", map);
	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("genderCode"))) {
			request.setAttribute("genderCode", PropertyReader.getValue("error.require", "Gender Code"));
			pass = false;
		}
		else if (!DataValidator.isCode(request.getParameter("genderCode"))) {

			request.setAttribute("genderCode",
					"Gender Code must be in format AB-123");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("genderType"))) {
			request.setAttribute("genderType", PropertyReader.getValue("error.require", "Gender Type"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("status"))) {
			request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		GenderDTO dto = new GenderDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setGenderCode(DataUtility.getString(request.getParameter("genderCode")));
		dto.setGenderType(DataUtility.getString(request.getParameter("genderType")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		populateBean(dto, request);

		log.debug("GenderCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("GenderCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		GenderModelInt model = ModelFactory.getInstance().getGenderModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			GenderDTO dto = null;
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
		GenderModelInt model = ModelFactory.getInstance().getGenderModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			GenderDTO dto = (GenderDTO) populateDTO(request);
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
						ServletUtility.setErrorMessage("Gender code already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Gender code already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			GenderDTO dto = (GenderDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.GENDER_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.GENDER_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.GENDER_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("GenderCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.GENDER_VIEW;
	}
}