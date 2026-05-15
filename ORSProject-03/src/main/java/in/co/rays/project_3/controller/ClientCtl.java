package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ClientDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ClientModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/ClientCtl" })
public class ClientCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("code"))) {
			request.setAttribute("code",
					PropertyReader.getValue("error.require", "Code"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name",
					PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("company"))) {
			request.setAttribute("company",
					PropertyReader.getValue("error.require", "Company"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("email"))) {
			request.setAttribute("email",
					PropertyReader.getValue("error.require", "Email"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("email"))) {
			request.setAttribute("email",
					PropertyReader.getValue("error.email", "Email"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("contact"))) {
			request.setAttribute("contact",
					PropertyReader.getValue("error.require", "Contact"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		ClientDTO dto = new ClientDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setCode(DataUtility.getString(request.getParameter("code")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setCompany(DataUtility.getString(request.getParameter("company")));
		dto.setEmail(DataUtility.getString(request.getParameter("email")));
		dto.setContact(DataUtility.getString(request.getParameter("contact")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		ClientModelInt model = ModelFactory.getInstance().getClientModel();

		if (id > 0 || op != null) {
			ClientDTO dto;
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
			HttpServletResponse response) throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		ClientModelInt model = ModelFactory.getInstance().getClientModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)
				|| OP_UPDATE.equalsIgnoreCase(op)) {

			ClientDTO dto = (ClientDTO) populateDTO(request);

			try {

				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage(
							"Data updated successfully", request);
					ServletUtility.setDto(dto, request);

				} else {

					try {
						model.add(dto);
						ServletUtility.setSuccessMessage(
								"Data saved successfully", request);

					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage(
								"Client already exists", request);
					}
				}

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage(
						"Client already exists", request);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			ClientDTO dto = (ClientDTO) populateDTO(request);

			try {
				model.delete(dto);
				ServletUtility.redirect(
						ORSView.CLIENT_LIST_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				ServletUtility.handleDBDown(getView(), request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(
					ORSView.CLIENT_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(
					ORSView.CLIENT_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.CLIENT_VIEW;
	}
}