package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PaymentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PaymentModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * role list functionality controller. to show list and search of role operation
 * 
 * @author krati
 *
 */
@WebServlet(name = "PaymentListCtl", urlPatterns = { "/ctl/PaymentListCtl" })
public class PaymentListCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {
		HashMap payMode = new HashMap();
		payMode.put("cash", "cash");
		payMode.put("UPI", "UPI");
		payMode.put("cheque", "cheque");
		payMode.put("debit card", "debit card");
		payMode.put("credit card", "credit card");
		request.setAttribute("payyMode", payMode);

		HashMap payStatus = new HashMap();
		payStatus.put("processed", "processed");
		payStatus.put("pending", "pending");
		payStatus.put("processing", "processing");
		request.setAttribute("payyStatus", payStatus);
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		PaymentDTO dto = new PaymentDTO();
		dto.setPayerName(DataUtility.getString(request.getParameter("payerName")));
		dto.setPaymentMode(DataUtility.getString(request.getParameter("paymentMode")));
		dto.setPaymentStatus(DataUtility.getString(request.getParameter("paymentStatus")));
		populateBean(dto, request);

		return dto;
	}

	/**
	 * Contains Display logics
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List list = null;
		List next = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		PaymentDTO dto = (PaymentDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();
		try {
			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

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
		}
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List list = null;
		List next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		PaymentDTO dto = (PaymentDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");
		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

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
				ServletUtility.redirect(ORSView.PAYMENT_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.PAYMENT_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.PAYMENT_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					PaymentDTO deletebean = new PaymentDTO();
					for (String id : ids) {
						deletebean.setId(DataUtility.getLong(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Data Delete Successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}
			dto = (PaymentDTO) populateDTO(request);
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setDto(dto, request);
			next = model.search(dto, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			if (next == null || next.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
				request.setAttribute("nextListSize", 0);

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
		}
		
	}

	@Override
	protected String getView() {
		return ORSView.PAYMENT_LIST_VIEW;
	}

}
