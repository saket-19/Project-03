package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.TaskDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.TaskModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/TaskCtl" })
public class TaskCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {

		HashMap status = new HashMap();
		status.put("Pending", "Pending");
		status.put("In Progress", "In Progress");
		status.put("Completed", "Completed");
		status.put("On Hold", "On Hold");

		request.setAttribute("statusList", status);

	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("taskCode"))) {
			request.setAttribute("taskCode",
					PropertyReader.getValue("error.require", "Task Code"));
			pass = false;
		}
		if (!DataValidator.isCode(request.getParameter("taskCode"))) {

			request.setAttribute("taskCode",
					"Task Code must be in format AB-123");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("task"))) {
			request.setAttribute("task",
					PropertyReader.getValue("error.require", "Task"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("assignedTo"))) {
			request.setAttribute("assignedTo",
					PropertyReader.getValue("error.require", "Assigned To"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("dueDate"))) {
			request.setAttribute("dueDate",
					PropertyReader.getValue("error.require", "Due Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("dueDate"))) {
			request.setAttribute("dueDate",
					PropertyReader.getValue("error.date", "Due Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("taskStatus"))) {
			request.setAttribute("taskStatus",
					PropertyReader.getValue("error.require", "Task Status"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		TaskDTO dto = new TaskDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setTaskCode(DataUtility.getString(request.getParameter("taskCode")));
		dto.setTask(DataUtility.getString(request.getParameter("task")));
		dto.setAssignedTo(DataUtility.getString(request.getParameter("assignedTo")));
		dto.setDueDate(DataUtility.getDate(request.getParameter("dueDate")));
		dto.setTaskStatus(DataUtility.getString(request.getParameter("taskStatus")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		TaskModelInt model = ModelFactory.getInstance().getTaskModel();

		if (id > 0 || op != null) {

			TaskDTO dto;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		TaskModelInt model = ModelFactory.getInstance().getTaskModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			TaskDTO dto = (TaskDTO) populateDTO(request);

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
						ServletUtility.setErrorMessage("Task already exists", request);
					}
				}

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Task already exists", request);
			}

		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {

			TaskDTO dto = (TaskDTO) populateDTO(request);

			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.TASK_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		}

		else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TASK_LIST_CTL, request, response);
			return;

		}

		else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TASK_CTL, request, response);
			return;

		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.TASK_VIEW;
	}

}