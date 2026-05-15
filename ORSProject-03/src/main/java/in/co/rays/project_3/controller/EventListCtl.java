package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CourseDTO;
import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.CourseModelInt;
import in.co.rays.project_3.model.EventModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "EventListCtl", urlPatterns = { "/ctl/EventListCtl" })
public class EventListCtl extends BaseCtl {
	
	@Override
	protected void preload(HttpServletRequest request) {

		HashMap category = new HashMap();
		category.put("technical", "technical");
		category.put("non-technical", "non-technical");
		category.put("academic", "academic");
		category.put("extra curricular", "extra curricular");
		request.setAttribute("categoryL", category);
		
	}
	
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		EventDTO dto = new EventDTO();
		dto.setEventName(DataUtility.getString(request.getParameter("eventName")));
		dto.setEventDate(DataUtility.getDate(request.getParameter("eventDate")));
		dto.setEventType(DataUtility.getString(request.getParameter("eventType")));
		dto.setCategory(DataUtility.getString(request.getParameter("category")));
		return dto;
	}

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List list = null;
		List next = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		EventDTO dto = (EventDTO) populateDTO(request);
		EventModelInt model = ModelFactory.getInstance().getEventModel();
		try {
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setDto(dto, request);
			ServletUtility.setList(list, request);
			System.out.println("<>>><<<>>>>+" + list);
			next = model.search(dto, pageNo + 1, pageSize);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", "0");
			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		List list=null;
		List next=null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		EventDTO dto = (EventDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");
		EventModelInt model =ModelFactory.getInstance().getEventModel();
		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EVENT_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.EVENT_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EVENT_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					EventDTO deletebean = new EventDTO();
					for (String id : ids) {
						deletebean.setId(DataUtility.getLong(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Data Delete Successfully", request);
					}

				} else {
					ServletUtility.setErrorMessage("Select atleast one record", request);
				}
			}
			dto = (EventDTO) populateDTO(request);
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setDto(dto, request);
			 next = model.search(dto, pageNo + 1, pageSize);
			 ServletUtility.setList(list, request);
			if (list == null || list.size() == 0&&!OP_DELETE.equalsIgnoreCase(op)) {
				ServletUtility.setErrorMessage("No record found", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", "0");
			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected String getView() {
		return ORSView.EVENT_LIST_VIEW;
	}

}
