package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CollegeDTO;
import in.co.rays.project_3.dto.PaymentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.CollegeModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PaymentModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;


@WebServlet(urlPatterns = { "/ctl/PaymentCtl" })
public class PaymentCtl extends BaseCtl {

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
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("payerName"))) {
			request.setAttribute("payerName", PropertyReader.getValue("error.require", "payer Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("paymentId"))) {
			request.setAttribute("paymentId", PropertyReader.getValue("error.require", "payment Id"));
			pass = false;
		} 
		if (DataValidator.isNull(request.getParameter("amount"))) {
			request.setAttribute("amount", PropertyReader.getValue("error.require", "amount"));
			pass = false;
		} 
		if (DataValidator.isNull(request.getParameter("paymentMode"))) {
			request.setAttribute("paymentMode", PropertyReader.getValue("error.require", "payment Mode"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("paymentStatus"))) {
			request.setAttribute("paymentStatus", PropertyReader.getValue("error.require", "payment Status"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		PaymentDTO dto = new PaymentDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setPayerName(DataUtility.getString(request.getParameter("payerName")));
		dto.setPaymentId(DataUtility.getLong(request.getParameter("paymentId")));
		dto.setPaymentMode(DataUtility.getString(request.getParameter("paymentMode")));
		dto.setPaymentStatus(DataUtility.getString(request.getParameter("paymentStatus")));
		dto.setAmount(DataUtility.getLong(request.getParameter("amount")));
		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));
		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();
		if (id > 0 || op != null) {
			PaymentDTO dto;
			try {
				dto = model.findByPk(id);
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			PaymentDTO dto = (PaymentDTO) populateDTO(request);

			try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setDto(dto, request);

					ServletUtility.setSuccessMessage("Payment Successfully Updated", request);

				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Payment Successfully Added", request);
				}
				// ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Payment ID Already Exists", request);
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.PAYMENT_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.PAYMENT_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.PAYMENT_VIEW;
	}

}
