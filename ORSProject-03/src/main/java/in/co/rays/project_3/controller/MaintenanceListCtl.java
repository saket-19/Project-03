package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.MaintenanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.MaintenanceModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "MaintenanceListCtl", urlPatterns = { "/ctl/MaintenanceListCtl" })
public class MaintenanceListCtl extends BaseCtl {

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
    protected BaseDTO populateDTO(HttpServletRequest request) {

        MaintenanceDTO dto = new MaintenanceDTO();

        dto.setCode(DataUtility.getString(request.getParameter("code")));
        dto.setEquipmentName(DataUtility.getString(request.getParameter("equipmentName")));
        dto.setMaintenanceDate(DataUtility.getDate(request.getParameter("maintenanceDate")));
        dto.setTechnicianName(DataUtility.getString(request.getParameter("technicianName")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List list = null;
        List next = null;

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        MaintenanceDTO dto = (MaintenanceDTO) populateDTO(request);
        MaintenanceModelInt model = ModelFactory.getInstance().getMaintenanceModel();

        try {

            list = model.search(dto, pageNo, pageSize);

            next = model.search(dto, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            if (next == null || next.size() == 0) {
                request.setAttribute("nextListSize", "0");
            } else {
                request.setAttribute("nextListSize", next.size());
            }

            ServletUtility.setDto(dto, request);
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
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0)
                ? DataUtility.getInt(PropertyReader.getValue("page.size"))
                : pageSize;

        MaintenanceDTO dto = (MaintenanceDTO) populateDTO(request);

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        MaintenanceModelInt model = ModelFactory.getInstance().getMaintenanceModel();

        try {

            if (OP_SEARCH.equalsIgnoreCase(op)
                    || OP_NEXT.equalsIgnoreCase(op)
                    || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;

                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;

                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.MAINTENANCE_CTL, request, response);
                return;

            } else if (OP_RESET.equalsIgnoreCase(op)
                    || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.MAINTENANCE_LIST_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    MaintenanceDTO deleteDto = new MaintenanceDTO();

                    for (String id : ids) {
                        deleteDto.setId(DataUtility.getLong(id));
                        model.delete(deleteDto);
                    }

                    ServletUtility.setSuccessMessage("Data Delete Successfully", request);

                } else {
                    ServletUtility.setErrorMessage("Select atleast one record", request);
                }
            }

            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            if (list == null || list.size() == 0
                    && !OP_DELETE.equalsIgnoreCase(op)) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            if (next == null || next.size() == 0) {
                request.setAttribute("nextListSize", "0");
            } else {
                request.setAttribute("nextListSize", next.size());
            }

            ServletUtility.setDto(dto, request);
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
        return ORSView.MAINTENANCE_LIST_VIEW;
    }
}