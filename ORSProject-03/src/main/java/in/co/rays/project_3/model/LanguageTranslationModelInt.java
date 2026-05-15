package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.LanguageTranslationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of LanguageTranslation model
 * @author saket
 *
 */
public interface LanguageTranslationModelInt {
public long add(LanguageTranslationDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(LanguageTranslationDTO dto)throws ApplicationException;
public void update(LanguageTranslationDTO dto)throws ApplicationException,DuplicateRecordException;
public LanguageTranslationDTO findByPK(long pk)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(LanguageTranslationDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(LanguageTranslationDTO dto)throws ApplicationException;
}