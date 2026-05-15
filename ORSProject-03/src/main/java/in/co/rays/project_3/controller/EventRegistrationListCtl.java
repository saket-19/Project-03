package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EventRegistrationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.EventRegistrationModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "EventRegistrationListCtl", urlPatterns = { "/ctl/EventRegistrationListCtl" })
public class EventRegistrationListCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap ticketType = new HashMap();
        ticketType.put("VIP", "VIP");
        ticketType.put("General", "General");
        ticketType.put("Student", "Student");
        ticketType.put("Premium", "Premium");
        request.setAttribute("ticketTypeL", ticketType);

        HashMap paymentMode = new HashMap();
        paymentMode.put("Cash", "Cash");
        paymentMode.put("Online", "Online");
        paymentMode.put("Card", "Card");
        paymentMode.put("UPI", "UPI");
        request.setAttribute("paymentModeL", paymentMode);
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {
        EventRegistrationDTO dto = new EventRegistrationDTO();
        dto.setParticipant(DataUtility.getString(request.getParameter("participant")));
        dto.setEventName(DataUtility.getString(request.getParameter("eventName")));
        dto.setRegistrationDate(DataUtility.getDate(request.getParameter("registrationDate")));
        dto.setTicketType(DataUtility.getString(request.getParameter("ticketType")));
        dto.setPaymentMode(DataUtility.getString(request.getParameter("paymentMode")));
        dto.setSeatNumber(DataUtility.getString(request.getParameter("seatNumber")));
        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List list = null;
        List next = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        EventRegistrationDTO dto = (EventRegistrationDTO) populateDTO(request);
        EventRegistrationModelInt model = ModelFactory.getInstance().getEventRegistrationModel();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List list = null;
        List next = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        EventRegistrationDTO dto = (EventRegistrationDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
        EventRegistrationModelInt model = ModelFactory.getInstance().getEventRegistrationModel();
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
                ServletUtility.redirect(ORSView.EVENT_REGISTRATION_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.EVENT_REGISTRATION_LIST_CTL, request, response);
                return;
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.EVENT_REGISTRATION_LIST_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    EventRegistrationDTO deletebean = new EventRegistrationDTO();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getLong(id));
                        model.delete(deletebean);
                        ServletUtility.setSuccessMessage("Data Delete Successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select atleast one record", request);
                }
            }
            dto = (EventRegistrationDTO) populateDTO(request);
            list = model.search(dto, pageNo, pageSize);
            ServletUtility.setDto(dto, request);
            next = model.search(dto, pageNo + 1, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
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

    @Override
    protected String getView() {
        return ORSView.EVENT_REGISTRATION_LIST_VIEW;
    }
}