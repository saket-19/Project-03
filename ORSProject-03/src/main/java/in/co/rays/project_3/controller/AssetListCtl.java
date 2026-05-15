package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.AssetDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.AssetModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "AssetListCtl", urlPatterns = { "/ctl/AssetListCtl" })
public class AssetListCtl extends BaseCtl {

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap assetType = new HashMap();
        assetType.put("Hardware", "Hardware");
        assetType.put("Software", "Software");
        assetType.put("Furniture", "Furniture");
        assetType.put("Vehicle", "Vehicle");
        assetType.put("Equipment", "Equipment");
        request.setAttribute("assetTypeL", assetType);

        HashMap assetStatus = new HashMap();
        assetStatus.put("Active", "Active");
        assetStatus.put("Inactive", "Inactive");
        assetStatus.put("Under Maintenance", "Under Maintenance");
        assetStatus.put("Disposed", "Disposed");
        request.setAttribute("assetStatusL", assetStatus);
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {
        AssetDTO dto = new AssetDTO();
        dto.setAssetCode(DataUtility.getString(request.getParameter("assetCode")));
        dto.setAssetName(DataUtility.getString(request.getParameter("assetName")));
        dto.setAssetType(DataUtility.getString(request.getParameter("assetType")));
        dto.setAssetStatus(DataUtility.getString(request.getParameter("assetStatus")));
        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List list = null;
        List next = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        AssetDTO dto = (AssetDTO) populateDTO(request);
        AssetModelInt model = ModelFactory.getInstance().getAssetModel();
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
        AssetDTO dto = (AssetDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
        AssetModelInt model = ModelFactory.getInstance().getAssetModel();
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
                ServletUtility.redirect(ORSView.ASSET_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ASSET_LIST_CTL, request, response);
                return;
            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ASSET_LIST_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    AssetDTO deletebean = new AssetDTO();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getLong(id));
                        model.delete(deletebean);
                        ServletUtility.setSuccessMessage("Data Delete Successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select atleast one record", request);
                }
            }
            dto = (AssetDTO) populateDTO(request);
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
        return ORSView.ASSET_LIST_VIEW;
    }
}