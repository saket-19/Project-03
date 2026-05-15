package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EmiDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EmiModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/EmiCtl" })
public class EmiCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap status = new HashMap();
        status.put("Pending", "Pending");
        status.put("Paid", "Paid");
        status.put("Overdue", "Overdue");
        status.put("Cancelled", "Cancelled");
        request.setAttribute("statusL", status);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("emiCode"))) {
            request.setAttribute("emiCode", PropertyReader.getValue("error.require", "EMI Code"));
            pass = false;
        } else if (!DataValidator.isCode(request.getParameter("emiCode"))) {
        	request.setAttribute("emiCode", "enter valid code");
            pass = false;
		}
        if (DataValidator.isNull(request.getParameter("amount"))) {
            request.setAttribute("amount", PropertyReader.getValue("error.require", "Amount"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("dueDate"))) {
            request.setAttribute("dueDate", PropertyReader.getValue("error.require", "Due Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dueDate"))) {
            request.setAttribute("dueDate", PropertyReader.getValue("error.date", "Due Date"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        EmiDTO dto = new EmiDTO();
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setEmiCode(DataUtility.getString(request.getParameter("emiCode")));
        dto.setAmount(DataUtility.getString(request.getParameter("amount")));
        dto.setDueDate(DataUtility.getDate(request.getParameter("dueDate")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        EmiModelInt model = ModelFactory.getInstance().getEmiModel();
        if (id > 0 || op != null) {
            EmiDTO dto;
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
        EmiModelInt model = ModelFactory.getInstance().getEmiModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            EmiDTO dto = (EmiDTO) populateDTO(request);

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
                        ServletUtility.setErrorMessage("EMI Code already exists", request);
                    }
                }
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("EMI Code already exists", request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            EmiDTO dto = (EmiDTO) populateDTO(request);
            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.EMI_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.EMI_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.EMI_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.EMI_VIEW;
    }
}