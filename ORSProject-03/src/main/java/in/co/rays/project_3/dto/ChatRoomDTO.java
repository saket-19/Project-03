package in.co.rays.project_3.dto;

/**
 * ChatRoom JavaDto encapsulates chatRoom attributes
 * 
 * @author saket
 */
public class ChatRoomDTO extends BaseDTO {
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";

	private String chatCode;
	private String roomName;
	private String createdBy;
	private String status;



	public String getChatCode() {
		return chatCode;
	}

	public void setChatCode(String chatCode) {
		this.chatCode = chatCode;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
		return chatCode + "" + roomName;
	}
}