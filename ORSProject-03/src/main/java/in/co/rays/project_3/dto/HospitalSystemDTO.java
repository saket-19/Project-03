package in.co.rays.project_3.dto;

/**
 * HospitalSystem JavaDto encapsulates hospitalSystem attributes
 * @author saket
 */
public class HospitalSystemDTO extends BaseDTO {
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	private String patientName;
	private String doctorName;
	private String disease;
	private String room;

	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
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
		return patientName + "" + doctorName;
	}
}