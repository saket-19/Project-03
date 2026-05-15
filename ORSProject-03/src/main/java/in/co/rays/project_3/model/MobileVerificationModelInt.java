package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.MobileVerificationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of MobileVerification model
 * @author saket
 *
 */
public interface MobileVerificationModelInt {
public long add(MobileVerificationDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(MobileVerificationDTO dto)throws ApplicationException;
public void update(MobileVerificationDTO dto)throws ApplicationException,DuplicateRecordException;
public MobileVerificationDTO findByPK(long pk)throws ApplicationException;
public MobileVerificationDTO findByCode(String verificationCode)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(MobileVerificationDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(MobileVerificationDTO dto)throws ApplicationException;
}