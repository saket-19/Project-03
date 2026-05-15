package in.co.rays.project_3.dto;

/**
 * UserProfile JavaDto encapsulates userProfile attributes
 * @author saket
 */
public class UserProfileDTO extends BaseDTO {
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	private String profileCode;
	private String userName;
	private String mobileNumber;
	private String status;

	public String getProfileCode() {
		return profileCode;
	}
	public void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
		return userName + "" + profileCode;
	}
}