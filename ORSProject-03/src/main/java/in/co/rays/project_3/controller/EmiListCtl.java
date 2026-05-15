package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EmiDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.EmiModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "EmiListCtl", urlPatterns = { "/ctl/EmiListCtl" })
public class EmiListCtl extends BaseCtl {

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
    protected BaseDTO populateDTO(HttpServletRequest request) {
        EmiDTO dto = new EmiDTO();
        dto.setEmiCode(DataUtility.getString(request.getParameter("emiCode")));
        dto.setAmount(DataUtility.getString(request.getParameter("amount")));
        dto.setDueDate(DataUtility.getDate(request.getParameter("dueDate")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));
        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List list = null;
        List next = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        EmiDTO dto = (EmiDTO) populateDTO(request);
        EmiModelInt model = ModelFactory.getInstance().getEmiModel();
        try {
            list = model.search(dto, pageNo, pageSize);
            ServletUtility.setDto(dto, request);
            ServletUtility.setList(list, request);
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
        EmiDTO dto = (EmiDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
        EmiModelInt model = ModelFactory.getInstance().getEmiModel();
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
                ServletUtility.redirect(ORSView.EMI_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.EMI_LIST_CTL, request, response);
                return;
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.EMI_LIST_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    EmiDTO deletebean = new EmiDTO();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getLong(id));
                        model.delete(deletebean);
                        ServletUtility.setSuccessMessage("Data Delete Successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select atleast one record", request);
                }
            }
            dto = (EmiDTO) populateDTO(request);
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
        return ORSView.EMI_LIST_VIEW;
    }
}