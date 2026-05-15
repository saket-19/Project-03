package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.dto.StudentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EventModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StudentModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;


@WebServlet(urlPatterns={"/ctl/EventCtl"})
public class EventCtl extends BaseCtl{
	
	@Override
	protected void preload(HttpServletRequest request) {
		
		
		HashMap eventType = new HashMap();
		eventType.put("seminar", "seminar");
		eventType.put("workshop", "workshop");
		eventType.put("fest", "fest");
		eventType.put("sports", "sports");
		eventType.put("cultural", "cultural");
		request.setAttribute("event", eventType);
		
		
		HashMap category = new HashMap();
		category.put("technical", "technical");
		category.put("non-technical", "non-technical");
		category.put("academic", "academic");
		category.put("extra curricular", "extra curricular");
		request.setAttribute("categoryL", category);
		
	}
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		
		boolean pass = true;
		
		if (DataValidator.isNull(request.getParameter("eventName"))) {
			request.setAttribute("eventName", PropertyReader.getValue("error.require", "Event Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("eventDate"))) {
			request.setAttribute("eventDate", PropertyReader.getValue("error.require", "date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("eventDate"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("eventType"))) {
			request.setAttribute("eventType", PropertyReader.getValue("error.require", "Event Type"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("category"))) {
			request.setAttribute("category", PropertyReader.getValue("error.require", "category"));
			pass = false;
		}
		
		return pass;
	}
	
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		
		EventDTO dto = new EventDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setEventName(DataUtility.getString(request.getParameter("eventName")));
		dto.setEventDate(DataUtility.getDate(request.getParameter("eventDate")));
		dto.setEventType(DataUtility.getString(request.getParameter("eventType")));
		dto.setCategory(DataUtility.getString(request.getParameter("category")));
		
		populateBean(dto, request);
		
		return dto;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		EventModelInt model = ModelFactory.getInstance().getEventModel();
		if (id > 0 || op != null) {
			EventDTO dto;
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));
		EventModelInt model = ModelFactory.getInstance().getEventModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			EventDTO dto = (EventDTO) populateDTO(request);

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data updated successfully", request);
					ServletUtility.setDto(dto, request);

				} else {
					try {
						model.add(dto);
						ServletUtility.setSuccessMessage("Data saved successfully", request);
					} catch (ApplicationException e) {
						ServletUtility.handleDBDown(getView(), request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Event already exists", request);
					}

				}


			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Event already exists", request);
			}

		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {

			EventDTO dto = (EventDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.EVENT_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EVENT_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EVENT_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.EVENT_VIEW;
	}

}
