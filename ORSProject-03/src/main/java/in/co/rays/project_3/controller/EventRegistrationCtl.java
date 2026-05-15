package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EventRegistrationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EventRegistrationModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/EventRegistrationCtl" })
public class EventRegistrationCtl extends BaseCtl {

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
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("participant"))) {
            request.setAttribute("participant", PropertyReader.getValue("error.require", "Participant"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("eventName"))) {
            request.setAttribute("eventName", PropertyReader.getValue("error.require", "Event Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("registrationDate"))) {
            request.setAttribute("registrationDate", PropertyReader.getValue("error.require", "Registration Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("registrationDate"))) {
            request.setAttribute("registrationDate", PropertyReader.getValue("error.date", "Registration Date"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("ticketType"))) {
            request.setAttribute("ticketType", PropertyReader.getValue("error.require", "Ticket Type"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("paymentMode"))) {
            request.setAttribute("paymentMode", PropertyReader.getValue("error.require", "Payment Mode"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("seatNumber"))) {
            request.setAttribute("seatNumber", PropertyReader.getValue("error.require", "Seat Number"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        EventRegistrationDTO dto = new EventRegistrationDTO();
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setParticipant(DataUtility.getString(request.getParameter("participant")));
        dto.setEventName(DataUtility.getString(request.getParameter("eventName")));
        dto.setRegistrationDate(DataUtility.getDate(request.getParameter("registrationDate")));
        dto.setTicketType(DataUtility.getString(request.getParameter("ticketType")));
        dto.setPaymentMode(DataUtility.getString(request.getParameter("paymentMode")));
        dto.setSeatNumber(DataUtility.getString(request.getParameter("seatNumber")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        EventRegistrationModelInt model = ModelFactory.getInstance().getEventRegistrationModel();
        if (id > 0 || op != null) {
            EventRegistrationDTO dto;
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
        EventRegistrationModelInt model = ModelFactory.getInstance().getEventRegistrationModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            EventRegistrationDTO dto = (EventRegistrationDTO) populateDTO(request);

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
                        ServletUtility.setErrorMessage("Event Registration already exists", request);
                    }
                }
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Event Registration already exists", request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            EventRegistrationDTO dto = (EventRegistrationDTO) populateDTO(request);
            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.EVENT_REGISTRATION_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.EVENT_REGISTRATION_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.EVENT_REGISTRATION_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.EVENT_REGISTRATION_VIEW;
    }
}