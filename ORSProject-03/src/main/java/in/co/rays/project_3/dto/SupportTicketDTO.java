package in.co.rays.project_3.dto;

import java.util.Date;

public class SupportTicketDTO extends BaseDTO{

	private String TicketNo;
	private String issue;
	private Date createdDate;
	private String status;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTicketNo() {
		return TicketNo;
	}

	public void setTicketNo(String ticketNo) {
		TicketNo = ticketNo;
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
