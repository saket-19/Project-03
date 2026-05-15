package in.co.rays.project_3.dto;

/**
 * VoiceCommand JavaDto encapsulates voiceCommand attributes
 * @author saket
 */
public class VoiceCommandDTO extends BaseDTO {
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	private String commandCode;
	private String userName;
	private String commandText;
	private String status;

	public String getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCommandText() {
		return commandText;
	}
	public void setCommandText(String commandText) {
		this.commandText = commandText;
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
		return commandCode + "" + userName;
	}
}