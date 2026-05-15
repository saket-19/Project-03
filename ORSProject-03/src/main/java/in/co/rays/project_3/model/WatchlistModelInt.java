package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.WatchlistDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface WatchlistModelInt {
	
	public long add(WatchlistDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(WatchlistDTO dto)throws ApplicationException;
	public void update(WatchlistDTO dto)throws ApplicationException,DuplicateRecordException;
	public List list()throws ApplicationException;
	public List list(int pageNo,int pageSize)throws ApplicationException;
	public List search(WatchlistDTO dto)throws ApplicationException;
	public List search(WatchlistDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public WatchlistDTO findByPK(long pk)throws ApplicationException;
	public WatchlistDTO findByName(String name)throws ApplicationException;

}
