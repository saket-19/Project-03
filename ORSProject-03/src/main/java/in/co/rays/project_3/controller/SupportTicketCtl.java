package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.SupportTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.SupportTicketModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/SupportTicketCtl" })
public class SupportTicketCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap status = new HashMap();
        status.put("Open", "Open");
        status.put("In Progress", "In Progress");
        status.put("Resolved", "Resolved");
        status.put("Closed", "Closed");
        request.setAttribute("statusL", status);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("TicketNo"))) {
            request.setAttribute("TicketNo", PropertyReader.getValue("error.require", "Ticket No"));
            pass = false;
        } else if (!DataValidator.isCode(request.getParameter("TicketNo"))) {
			request.setAttribute("TicketNo", "enter valid code");
		}
        
        if (DataValidator.isNull(request.getParameter("issue"))) {
            request.setAttribute("issue", PropertyReader.getValue("error.require", "Issue"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("createdDate"))) {
            request.setAttribute("createdDate", PropertyReader.getValue("error.require", "Created Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("createdDate"))) {
            request.setAttribute("createdDate", PropertyReader.getValue("error.date", "Created Date"));
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

        SupportTicketDTO dto = new SupportTicketDTO();
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setTicketNo(DataUtility.getString(request.getParameter("TicketNo")));
        dto.setIssue(DataUtility.getString(request.getParameter("issue")));
        dto.setCreatedDate(DataUtility.getDate(request.getParameter("createdDate")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        SupportTicketModelInt model = ModelFactory.getInstance().getSupportTicketModel();
        if (id > 0 || op != null) {
            SupportTicketDTO dto;
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
        SupportTicketModelInt model = ModelFactory.getInstance().getSupportTicketModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            SupportTicketDTO dto = (SupportTicketDTO) populateDTO(request);

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
                        ServletUtility.setErrorMessage("Ticket No already exists", request);
                    }
                }
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Ticket No already exists", request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            SupportTicketDTO dto = (SupportTicketDTO) populateDTO(request);
            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.SUPPORT_TICKET_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.SUPPORT_TICKET_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.SUPPORT_TICKET_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.SUPPORT_TICKET_VIEW;
    }
}