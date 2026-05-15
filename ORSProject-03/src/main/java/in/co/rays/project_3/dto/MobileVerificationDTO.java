package in.co.rays.project_3.dto;

/**
 * MobileVerification JavaDto encapsulates mobileVerification attributes
 * @author saket
 */
public class MobileVerificationDTO extends BaseDTO {
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	private String verificationCode;
	private String mobileNumber;
	private String otp;
	private String status;

	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static String getActive() {
		return ACTIVE;
	}
	public static String getInactive() {
		return INACTIVE;
	}
	public String getKey() {
		return id + "";
	}
	public String getValue() {
		return verificationCode + "" + mobileNumber;
	}
}
