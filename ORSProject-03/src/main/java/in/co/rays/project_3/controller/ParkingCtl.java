package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ParkingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ParkingModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/ParkingCtl" })
public class ParkingCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {
        // No dropdowns needed for Parking
    }

    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("location"))) {
            request.setAttribute("location", PropertyReader.getValue("error.require", "Location"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("capacity"))) {
            request.setAttribute("capacity", PropertyReader.getValue("error.require", "Capacity"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("fee"))) {
            request.setAttribute("fee", PropertyReader.getValue("error.require", "Fee"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        ParkingDTO dto = new ParkingDTO();
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setLocation(DataUtility.getString(request.getParameter("location")));
        dto.setCapacity(DataUtility.getInt(request.getParameter("capacity")));
        dto.setFee(DataUtility.getDouble(request.getParameter("fee")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        ParkingModelInt model = ModelFactory.getInstance().getParkingModel();
        if (id > 0 || op != null) {
            ParkingDTO dto;
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
        ParkingModelInt model = ModelFactory.getInstance().getParkingModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            ParkingDTO dto = (ParkingDTO) populateDTO(request);

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
                        ServletUtility.setErrorMessage("Parking Location already exists", request);
                    }
                }
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Parking Location already exists", request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            ParkingDTO dto = (ParkingDTO) populateDTO(request);
            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.PARKING_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PARKING_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PARKING_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.PARKING_VIEW;
    }
}