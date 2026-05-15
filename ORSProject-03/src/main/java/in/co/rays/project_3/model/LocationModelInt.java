package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.LocationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface LocationModelInt {
	
	public long add(LocationDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(LocationDTO dto)throws ApplicationException;
	public void update(LocationDTO dto)throws ApplicationException, DuplicateRecordException;
	public List list()throws ApplicationException;
	public List search(LocationDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public LocationDTO findByPK(long pk)throws ApplicationException;
	public LocationDTO  findByCity(String city)throws ApplicationException;

}
