package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ChatRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ChatRoomModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * ChatRoom functionality controller.to perform add,delete and update operation
 * 
 * @author saket
 *
 */
@WebServlet(urlPatterns = { "/ctl/ChatRoomCtl" })
public class ChatRoomCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ChatRoomCtl.class);

	protected void preload(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ChatRoomDTO.ACTIVE, ChatRoomDTO.ACTIVE);
		map.put(ChatRoomDTO.INACTIVE, ChatRoomDTO.INACTIVE);
		request.setAttribute("statusList", map);
	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("chatCode"))) {
			request.setAttribute("chatCode", PropertyReader.getValue("error.require", "Chat Code"));
			pass = false;
		}
		else if (!DataValidator.isCode(request.getParameter("chatCode"))) {

			request.setAttribute("chatCode",
					"Chat Code must be in format AB-123");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("roomName"))) {
			request.setAttribute("roomName", PropertyReader.getValue("error.require", "Room Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("createdBy"))) {
			request.setAttribute("createdBy", PropertyReader.getValue("error.require", "Created By"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("status"))) {
			request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		ChatRoomDTO dto = new ChatRoomDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setChatCode(DataUtility.getString(request.getParameter("chatCode")));
		dto.setRoomName(DataUtility.getString(request.getParameter("roomName")));
		dto.setCreatedBy(DataUtility.getString(request.getParameter("createdBy")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		log.debug("ChatRoomCtl Method populateDTO Ended");

		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("ChatRoomCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		ChatRoomModelInt model = ModelFactory.getInstance().getChatRoomModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			ChatRoomDTO dto = null;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		ChatRoomModelInt model = ModelFactory.getInstance().getChatRoomModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			ChatRoomDTO dto = (ChatRoomDTO) populateDTO(request);
			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);
					ServletUtility.setDto(dto, request);
				} else {

					try {
						model.add(dto);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleDBDown(getView(), request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Chat code already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Chat code already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			ChatRoomDTO dto = (ChatRoomDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.CHAT_ROOM_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CHAT_ROOM_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CHAT_ROOM_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("ChatRoomCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return ORSView.CHAT_ROOM_VIEW;
	}
}