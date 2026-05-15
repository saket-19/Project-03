package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.MaintenanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface MaintenanceModelInt {

	public void add(MaintenanceDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(MaintenanceDTO dto) throws ApplicationException;

	public void update(MaintenanceDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List search(MaintenanceDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public MaintenanceDTO findByPK(long pk) throws ApplicationException;

	public MaintenanceDTO findByCode(String code) throws ApplicationException;

}