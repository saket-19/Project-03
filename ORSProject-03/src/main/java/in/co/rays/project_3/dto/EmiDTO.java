package in.co.rays.project_3.dto;

import java.util.Date;

public class EmiDTO extends BaseDTO {

//	private Long id;
	private String emiCode;
	private String amount;
	private Date dueDate;
	private String status;

	public String getEmiCode() {
		return emiCode;
	}

	public void setEmiCode(String emiCode) {
		this.emiCode = emiCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
