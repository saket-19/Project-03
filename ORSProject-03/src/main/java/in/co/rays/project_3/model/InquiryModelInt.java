package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.InquiryDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Inquiry model
 * 
 * @author krati
 *
 */
public interface InquiryModelInt {

	public long add(InquiryDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(InquiryDTO dto) throws ApplicationException;

	public void update(InquiryDTO dto) throws ApplicationException, DuplicateRecordException;

	public InquiryDTO findByPK(long pk) throws ApplicationException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(InquiryDTO dto) throws ApplicationException;

	public List search(InquiryDTO dto, int pageNo, int pageSize) throws ApplicationException;

}
