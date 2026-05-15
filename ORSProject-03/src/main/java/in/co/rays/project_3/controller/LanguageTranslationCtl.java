package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.LanguageTranslationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.LanguageTranslationModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * LanguageTranslation functionality controller.to perform add,delete and update operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/LanguageTranslationCtl" })
public class LanguageTranslationCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LanguageTranslationCtl.class);

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("sourceLanguage"))) {
			request.setAttribute("sourceLanguage", PropertyReader.getValue("error.require", "Source Language"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("targetLanguage"))) {
			request.setAttribute("targetLanguage", PropertyReader.getValue("error.require", "Target Language"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("inputText"))) {
			request.setAttribute("inputText", PropertyReader.getValue("error.require", "Input Text"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("translatedText"))) {
			request.setAttribute("translatedText", PropertyReader.getValue("error.require", "Translated Text"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		LanguageTranslationDTO dto = new LanguageTranslationDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setSourceLanguage(DataUtility.getString(request.getParameter("sourceLanguage")));
		dto.setTargetLanguage(DataUtility.getString(request.getParameter("targetLanguage")));
		dto.setInputText(DataUtility.getString(request.getParameter("inputText")));
		dto.setTranslatedText(DataUtility.getString(request.getParameter("translatedText")));

		populateBean(dto, request);

		log.debug("LanguageTranslationCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("LanguageTranslationCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		LanguageTranslationModelInt model = ModelFactory.getInstance().getLanguageTranslationModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			LanguageTranslationDTO dto = null;
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
		LanguageTranslationModelInt model = ModelFactory.getInstance().getLanguageTranslationModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			LanguageTranslationDTO dto = (LanguageTranslationDTO) populateDTO(request);
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
						ServletUtility.setErrorMessage("Language already exists", request);
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

			LanguageTranslationDTO dto = (LanguageTranslationDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.LANGUAGE_TRANSLATION_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.LANGUAGE_TRANSLATION_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.LANGUAGE_TRANSLATION_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("LanguageTranslationCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.LANGUAGE_TRANSLATION_VIEW;
	}
}