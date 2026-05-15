package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ShiftDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ShiftModelHibImpl;
import in.co.rays.project_3.model.ShiftModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "ShiftListCtl", urlPatterns = { "/ctl/ShiftListCtl" })
public class ShiftListCtl extends BaseCtl {
	
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		
		ShiftDTO dto = new ShiftDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setCode(DataUtility.getString(request.getParameter("code")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setStartTime(DataUtility.getString(request.getParameter("startTime")));
		dto.setEndTime(DataUtility.getString(request.getParameter("endTime")));
		
		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = 1;
		int pageSize = DataUtility.getInt(
				ServletUtility.getParameter("pageSize", request));

		if (pageSize == 0) {
			pageSize = 10;
		}

		ShiftDTO dto = new ShiftDTO();

		populateDTO(request);

		ShiftModelInt model = new ShiftModelHibImpl();

		try {

			List list = model.search(dto, pageNo, pageSize);
			List nextList = model.search(dto, pageNo + 1, pageSize);

			request.setAttribute("nextListSize", nextList.size());

			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setList(list, request);

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? 10 : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));

		ShiftDTO dto = new ShiftDTO();

		dto.setCode(DataUtility.getString(request.getParameter("code")));
		dto.setName(DataUtility.getString(request.getParameter("name")));

		ShiftModelInt model = new ShiftModelHibImpl();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		}

		else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		}

		else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
			pageNo--;
		}

		else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SHIFT_CTL, request, response);
			return;
		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {

			String[] ids = request.getParameterValues("ids");

			if (ids != null) {
				for (String id : ids) {
					ShiftDTO deleteDto = new ShiftDTO();
					deleteDto.setId(DataUtility.getLong(id));
					try {
						model.delete(deleteDto);
					} catch (ApplicationException e) {
						e.printStackTrace();
					}
				}
				ServletUtility.setSuccessMessage("Shift deleted successfully", request);
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}

		else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SHIFT_LIST_CTL, request, response);
			return;
		}

		try {

			List list = model.search(dto, pageNo, pageSize);
			List nextList = model.search(dto, pageNo + 1, pageSize);

			request.setAttribute("nextListSize", nextList.size());

			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setList(list, request);

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getView() {
		return ORSView.SHIFT_LIST_VIEW;
	}
}