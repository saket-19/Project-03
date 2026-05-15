package in.co.rays.project_3.dto;

public class InquiryDTO extends BaseDTO {

	private String inquirerName;
	private String email;
	private String inquirySubject;
	private String inquiryStatus;

	public String getInquirerName() {
		return inquirerName;
	}

	public void setInquirerName(String inquirerName) {
		this.inquirerName = inquirerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInquirySubject() {
		return inquirySubject;
	}

	public void setInquirySubject(String inquirySubject) {
		this.inquirySubject = inquirySubject;
	}

	public String getInquiryStatus() {
		return inquiryStatus;
	}

	public void setInquiryStatus(String inquiryStatus) {
		this.inquiryStatus = inquiryStatus;
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
