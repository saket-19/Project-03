package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ClientDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface ClientModelInt {
	
	public void add(ClientDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(ClientDTO dto) throws ApplicationException;

	public void update(ClientDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List search(ClientDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public ClientDTO findByPK(long pk) throws ApplicationException;

	public ClientDTO findByCode(String code) throws ApplicationException;

}