package in.co.rays.project_3.dto;

import java.util.Date;

public class MaintenanceDTO extends BaseDTO {
	
//	private Long id;
	private String code;
	private String equipmentName;
	private Date maintenanceDate;
	private String technicianName;
	private String status;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public Date getMaintenanceDate() {
		return maintenanceDate;
	}
	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public String getTechnicianName() {
		return technicianName;
	}
	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
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
