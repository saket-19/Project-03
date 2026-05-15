package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.MaintenanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.MaintenanceModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/MaintenanceCtl" })
public class MaintenanceCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap statusMap = new HashMap();

        statusMap.put("Scheduled", "Scheduled");
        statusMap.put("In Progress", "In Progress");
        statusMap.put("Completed", "Completed");
        statusMap.put("Pending", "Pending");
        statusMap.put("Cancelled", "Cancelled");

        request.setAttribute("statusList", statusMap);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("code"))) {
            request.setAttribute("code",
                    PropertyReader.getValue("error.require", "Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("equipmentName"))) {
            request.setAttribute("equipmentName",
                    PropertyReader.getValue("error.require", "Equipment Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("maintenanceDate"))) {
            request.setAttribute("maintenanceDate",
                    PropertyReader.getValue("error.require", "Maintenance Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("maintenanceDate"))) {
            request.setAttribute("maintenanceDate",
                    PropertyReader.getValue("error.date", "Maintenance Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("technicianName"))) {
            request.setAttribute("technicianName",
                    PropertyReader.getValue("error.require", "Technician Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        MaintenanceDTO dto = new MaintenanceDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setCode(DataUtility.getString(request.getParameter("code")));
        dto.setEquipmentName(DataUtility.getString(request.getParameter("equipmentName")));
        dto.setMaintenanceDate(DataUtility.getDate(request.getParameter("maintenanceDate")));
        dto.setTechnicianName(DataUtility.getString(request.getParameter("technicianName")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        MaintenanceModelInt model = ModelFactory.getInstance().getMaintenanceModel();

        if (id > 0 || op != null) {

            MaintenanceDTO dto;

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
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));
        MaintenanceModelInt model = ModelFactory.getInstance().getMaintenanceModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

            MaintenanceDTO dto = (MaintenanceDTO) populateDTO(request);

            try {

                if (id > 0) {
                    model.update(dto);
                    ServletUtility.setSuccessMessage("Data updated successfully", request);
                    ServletUtility.setDto(dto, request);

                } else {
                    try {
                        model.add(dto);
                        ServletUtility.setSuccessMessage("Data saved successfully", request);

                    } catch (DuplicateRecordException e) {
                        ServletUtility.setDto(dto, request);
                        ServletUtility.setErrorMessage("Code already exists", request);
                    }
                }

            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Code already exists", request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            MaintenanceDTO dto = (MaintenanceDTO) populateDTO(request);

            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.MAINTENANCE_LIST_CTL, request, response);
                return;

            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.MAINTENANCE_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.MAINTENANCE_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.MAINTENANCE_VIEW;
    }
}