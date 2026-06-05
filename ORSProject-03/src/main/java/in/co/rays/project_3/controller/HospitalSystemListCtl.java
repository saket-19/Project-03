package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.HospitalSystemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.HospitalSystemModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * HospitalSystem List functionality controller.to perform Search and List operation.
 * 
 * @author saket
 *
 */
@WebServlet(name = "HospitalSystemListCtl", urlPatterns = { "/ctl/HospitalSystemListCtl" })
public class HospitalSystemListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(HospitalSystemListCtl.class);

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		HospitalSystemDTO dto = new HospitalSystemDTO();

		dto.setPatientName(DataUtility.getString(request.getParameter("patientName")));
		dto.setDoctorName(DataUtility.getString(request.getParameter("doctorName")));
		dto.setDisease(DataUtility.getString(request.getParameter("disease")));

		populateBean(dto, request);
		return dto;
	}

	/**
	 * Contains Display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("HospitalSystemListCtl doGet Start");
		List list;
		List next;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		HospitalSystemDTO dto = (HospitalSystemDTO) populateDTO(request);
		HospitalSystemModelInt model = ModelFactory.getInstance().getHospitalSystemModel();
		try {
			list = model.search(dto, pageNo, pageSize);

			next = model.search(dto, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);
			return;
		}
		log.debug("HospitalSystemListCtl doGet End");
	}

	/**
	 * Contains Submit logics
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("HospitalSystemListCtl doPost Start");
		List list = null;
		List next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		HospitalSystemDTO dto = (HospitalSystemDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		HospitalSystemModelInt model = ModelFactory.getInstance().getHospitalSystemModel();
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
				ServletUtility.redirect(ORSView.HOSPITAL_SYSTEM_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.HOSPITAL_SYSTEM_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					HospitalSystemDTO deletedto = new HospitalSystemDTO();
					for (String id : ids) {
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
						ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select atleast one record", request);
				}
			}
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.HOSPITAL_SYSTEM_LIST_CTL, request, response);
				return;
			}
			dto = (HospitalSystemDTO) populateDTO(request);

			list = model.search(dto, pageNo, pageSize);

			ServletUtility.setDto(dto, request);
			next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("HospitalSystemListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.HOSPITAL_SYSTEM_LIST_VIEW;
	}
}