package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.HospitalSystemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of HospitalSystem model
 * @author saket
 *
 */
public interface HospitalSystemModelInt {
public long add(HospitalSystemDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(HospitalSystemDTO dto)throws ApplicationException;
public void update(HospitalSystemDTO dto)throws ApplicationException,DuplicateRecordException;
public HospitalSystemDTO findByPK(long pk)throws ApplicationException;
public HospitalSystemDTO findByPatientName(String patientName)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(HospitalSystemDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(HospitalSystemDTO dto)throws ApplicationException;
}