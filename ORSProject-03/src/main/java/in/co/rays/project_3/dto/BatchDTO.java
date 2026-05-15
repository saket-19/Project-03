package in.co.rays.project_3.dto;

/**
 * Batch JavaDto encapsulates batch attributes
 * @author saket
 */
public class BatchDTO extends BaseDTO {
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	private String batchCode;
	private String batchName;
	private Integer totalRecords;
	private String status;

	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
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
		return batchName + "" + batchCode;
	}
}