package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.BatchDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.BatchModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Batch functionality controller.to perform add,delete and update operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/BatchCtl" })
public class BatchCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BatchCtl.class);

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("batchCode"))) {
			request.setAttribute("batchCode", PropertyReader.getValue("error.require", "Batch Code"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("batchName"))) {
			request.setAttribute("batchName", PropertyReader.getValue("error.require", "Batch Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("status"))) {
			request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		BatchDTO dto = new BatchDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setBatchCode(DataUtility.getString(request.getParameter("batchCode")));
		dto.setBatchName(DataUtility.getString(request.getParameter("batchName")));
		dto.setTotalRecords(DataUtility.getInt(request.getParameter("totalRecords")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		populateBean(dto, request);

		log.debug("BatchCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("BatchCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		BatchModelInt model = ModelFactory.getInstance().getBatchModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			BatchDTO dto = null;
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
		BatchModelInt model = ModelFactory.getInstance().getBatchModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			BatchDTO dto = (BatchDTO) populateDTO(request);
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
						ServletUtility.setErrorMessage("Batch code already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Batch code already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			BatchDTO dto = (BatchDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.BATCH_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.BATCH_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.BATCH_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("BatchCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.BATCH_VIEW;
	}

}