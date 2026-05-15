package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.StudentDTO;
import in.co.rays.project_3.dto.WatchlistDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StudentModelInt;
import in.co.rays.project_3.model.WatchlistModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns={"/ctl/WatchlistCtl"})
public class WatchlistCtl extends BaseCtl{
	
	@Override
	protected void preload(HttpServletRequest request) {
		HashMap map = new HashMap();
		map.put("movie", "movie");
		map.put("series", "series");
		
		request.setAttribute("map", map);
	}
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		
		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("type"))) {
			request.setAttribute("type", PropertyReader.getValue("error.require", "Type"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("genre"))) {
			request.setAttribute("genre", PropertyReader.getValue("error.require", "Genre"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("des"))) {
			request.setAttribute("des", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}
		
		return pass;
	}
	
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		
		WatchlistDTO dto = new WatchlistDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setType(DataUtility.getString(request.getParameter("type")));
		dto.setGenre(DataUtility.getString(request.getParameter("genre")));
		dto.setDescription(DataUtility.getString(request.getParameter("des")));
		
		populateBean(dto, request);
		return dto;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		WatchlistModelInt model = ModelFactory.getInstance().getWatchlistModel();
		if (id > 0 || op != null) {
			WatchlistDTO dto;
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		String op = DataUtility.getString(request.getParameter("operation"));

		// get model

		WatchlistModelInt model = ModelFactory.getInstance().getWatchlistModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			WatchlistDTO dto = (WatchlistDTO) populateDTO(request);

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data is successfully Update", request);
					ServletUtility.setDto(dto, request);

				} else {
					try {
						model.add(dto);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (Exception e) {
						ServletUtility.handleDBDown(getView(), request, response);
						return;
					}
//					} catch (DuplicateRecordException e) {
//						ServletUtility.setDto(dto, request);
//						ServletUtility.setErrorMessage("Student already exists", request);
//					}

				}

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Student Email Id already exists", request);
			}

		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {

			WatchlistDTO dto = (WatchlistDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.WATCHLIST_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.WATCHLIST_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.WATCHLIST_CTL, request, response);
			return;

		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.WATCHLIST_VIEW;
	}

}
