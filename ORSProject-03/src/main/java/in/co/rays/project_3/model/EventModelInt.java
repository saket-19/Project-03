package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface EventModelInt {
	
	public void add(EventDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(EventDTO dto)throws ApplicationException;
	public void update(EventDTO dto)throws ApplicationException,DuplicateRecordException;
	public List list()throws ApplicationException;
	public List search(EventDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public EventDTO findByPK(long pk)throws ApplicationException;
	public EventDTO findByName(String name)throws ApplicationException;

}
