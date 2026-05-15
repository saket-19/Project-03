package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ShiftDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface ShiftModelInt {

	public long add(ShiftDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(ShiftDTO dto) throws ApplicationException;

	public void update(ShiftDTO dto) throws ApplicationException, DuplicateRecordException;

	public ShiftDTO findByPK(long pk) throws ApplicationException;

	public ShiftDTO findByCode(String code) throws ApplicationException;

	public List search(ShiftDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public List list() throws ApplicationException;
}