package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.AssetDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.AssetModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/AssetCtl" })
public class AssetCtl extends BaseCtl {

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
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("assetCode"))) {
            request.setAttribute("assetCode", PropertyReader.getValue("error.require", "Asset Code"));
            pass = false;
        } else if (!DataValidator.isCode(request.getParameter("assetCode"))) {
			request.setAttribute("assetCode", "invalid code format");
			pass = false;
		}
        if (DataValidator.isNull(request.getParameter("assetName"))) {
            request.setAttribute("assetName", PropertyReader.getValue("error.require", "Asset Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("assetType"))) {
            request.setAttribute("assetType", PropertyReader.getValue("error.require", "Asset Type"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("assetStatus"))) {
            request.setAttribute("assetStatus", PropertyReader.getValue("error.require", "Asset Status"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        AssetDTO dto = new AssetDTO();
        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setAssetCode(DataUtility.getString(request.getParameter("assetCode")));
        dto.setAssetName(DataUtility.getString(request.getParameter("assetName")));
        dto.setAssetType(DataUtility.getString(request.getParameter("assetType")));
        dto.setAssetStatus(DataUtility.getString(request.getParameter("assetStatus")));

        populateBean(dto, request);

        return dto;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));

        AssetModelInt model = ModelFactory.getInstance().getAssetModel();
        if (id > 0 || op != null) {
            AssetDTO dto;
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
        AssetModelInt model = ModelFactory.getInstance().getAssetModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
            AssetDTO dto = (AssetDTO) populateDTO(request);

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
                        ServletUtility.setErrorMessage("Asset Code already exists", request);
                    }
                }
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Asset Code already exists", request);
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            AssetDTO dto = (AssetDTO) populateDTO(request);
            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.ASSET_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                ServletUtility.handleDBDown(getView(), request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ASSET_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ASSET_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.ASSET_VIEW;
    }
}