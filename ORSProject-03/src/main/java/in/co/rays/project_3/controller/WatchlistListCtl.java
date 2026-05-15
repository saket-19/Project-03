package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.WatchlistDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.WatchlistModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/WatchlistListCtl" })
public class WatchlistListCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {
		HashMap map = new HashMap();
		map.put("movie", "movie");
		map.put("series", "series");

		request.setAttribute("map", map);
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		WatchlistDTO dto = new WatchlistDTO();
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setType(DataUtility.getString(request.getParameter("type")));
		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List list = null;
		List next = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		WatchlistDTO dto = (WatchlistDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		
		WatchlistModelInt model = ModelFactory.getInstance().getWatchlistModel();
		
		  try {
	            list = model.search(dto, pageNo, pageSize);
	            next = model.search(dto, pageNo+1, pageSize);
	            ServletUtility.setList(list, request);
	            if (list == null || list.size() == 0) {
	                ServletUtility.setErrorMessage("No record found ", request);
	            } if (next==null||next.size()==0){
					request.setAttribute("nextListSize", 0);
					
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
	      }
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List list = null;
        List next=null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader
                .getValue("page.size")) : pageSize;

        WatchlistDTO dto = (WatchlistDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        WatchlistModelInt model =ModelFactory.getInstance().getWatchlistModel();
         String[] ids=request.getParameterValues("ids");
        try {

            if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op)
                    || "Previous".equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }

            }else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.WATCHLIST_CTL, request, response);
				return;
			}else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.WATCHLIST_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					WatchlistDTO deletebean = new WatchlistDTO();
					for (String id : ids) {
						deletebean.setId(DataUtility.getLong(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Data Delete Successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}
            
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.WATCHLIST_LIST_CTL, request, response);
				return;
			}
			
			dto = (WatchlistDTO) populateDTO(request);
            list = model.search(dto, pageNo, pageSize);
            ServletUtility.setDto(dto, request);
            next = model.search(dto, pageNo+1, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.size() == 0&&!OP_DELETE.equalsIgnoreCase(op)) {
                ServletUtility.setErrorMessage("No record found ", request);
            }if(next==null||next.size()==0){
				request.setAttribute("nextListSize", 0);
				
			}else{
				request.setAttribute("nextListSize", next.size());
			}
            
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
		return ORSView.WATCHLIST_LIST_VIEW;
	}

}
