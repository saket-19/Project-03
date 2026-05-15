package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.PaymentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface PaymentModelInt {

	public void add(PaymentDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(PaymentDTO dto) throws ApplicationException;
	public void update(PaymentDTO dto)throws ApplicationException,DuplicateRecordException;
	public List list()throws ApplicationException;
	public List search(PaymentDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public PaymentDTO findByPk(long pk)throws ApplicationException;
	public PaymentDTO findByPaymentId(long paymentId) throws ApplicationException;
	
}
