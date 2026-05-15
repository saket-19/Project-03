package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.InquiryDTO;
import in.co.rays.project_3.model.InquiryModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "InquiryListCtl", urlPatterns = { "/ctl/InquiryListCtl" })
public class InquiryListCtl extends BaseCtl {


	protected BaseDTO populateDTO(HttpServletRequest request) {

		InquiryDTO dto = new InquiryDTO();

		dto.setInquirerName(DataUtility.getString(request.getParameter("inquirerName")));
		dto.setEmail(DataUtility.getString(request.getParameter("email")));
		dto.setInquiryStatus(DataUtility.getString(request.getParameter("inquiryStatus")));

		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list;
		List next;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		InquiryDTO dto = (InquiryDTO) populateDTO(request);
		InquiryModelInt model = ModelFactory.getInstance().getInquiryModel();

		try {
			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);

			request.setAttribute("nextListSize", next.size());
			ServletUtility.forward(getView(), request, response);

		} catch (Exception e) {
			ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list;
		List next;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		InquiryDTO dto = (InquiryDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		InquiryModelInt model = ModelFactory.getInstance().getInquiryModel();

		try {
			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.INQUIRY_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				String[] ids = request.getParameterValues("ids");
				if (ids != null) {
					for (String id : ids) {
						InquiryDTO deletedto = new InquiryDTO();
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
					}
					ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);
				}
			}

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);

			request.setAttribute("nextListSize", next.size());
			ServletUtility.forward(getView(), request, response);

		} catch (Exception e) {
			ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);
		}
	}

	@Override
	protected String getView() {
		return ORSView.INQUIRY_LIST_VIEW;
	}
}
