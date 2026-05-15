package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.LanguageTranslationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of LanguageTranslation model
 * 
 * @author saket
 *
 */
public class LanguageTranslationModelHibImp implements LanguageTranslationModelInt {
	public long add(LanguageTranslationDTO dto)
	        throws ApplicationException, DuplicateRecordException {

	    LanguageTranslationDTO existDto = null;

	    existDto = findBySourceLanguage(dto.getSourceLanguage());

	    if (existDto != null) {
	        throw new DuplicateRecordException("Language already exists");
	    }

	    Session session = HibDataSource.getSession();

	    Transaction tx = null;

	    try {

	        tx = session.beginTransaction();

	        session.save(dto);

	        tx.commit();

	    } catch (HibernateException e) {

	        if (tx != null) {
	            tx.rollback();
	        }

	        throw new ApplicationException(
	                "Exception in LanguageTranslation Add " + e.getMessage());

	    } finally {

	        if (session != null) {
	            session.close();
	        }
	    }

	    return dto.getId();
	}
	public void delete(LanguageTranslationDTO dto) throws ApplicationException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in LanguageTranslation Delete" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	public LanguageTranslationDTO findBySourceLanguage(String sourceLanguage)
	        throws ApplicationException {

	    Session session = null;

	    LanguageTranslationDTO dto = null;

	    try {

	        session = HibDataSource.getSession();

	        Criteria criteria =
	                session.createCriteria(LanguageTranslationDTO.class);

	        criteria.add(
	                Restrictions.eq("sourceLanguage", sourceLanguage));

	        List list = criteria.list();

	        if (list.size() > 0) {
	            dto = (LanguageTranslationDTO) list.get(0);
	        }

	    } catch (HibernateException e) {

	        throw new ApplicationException(
	                "Exception in getting LanguageTranslation by Source Language");

	    } finally {

	        if (session != null) {
	            session.close();
	        }
	    }

	    return dto;
	}

	public void update(LanguageTranslationDTO dto)
	        throws ApplicationException, DuplicateRecordException {

	    LanguageTranslationDTO existDto = null;

	    existDto = findBySourceLanguage(dto.getSourceLanguage());

	    if (existDto != null && existDto.getId() != dto.getId()) {

	        throw new DuplicateRecordException(
	                "Language already exists");
	    }

	    Session session = null;

	    Transaction tx = null;

	    try {

	        session = HibDataSource.getSession();

	        tx = session.beginTransaction();

	        session.saveOrUpdate(dto);

	        tx.commit();

	    } catch (HibernateException e) {

	        if (tx != null) {
	            tx.rollback();
	        }

	        throw new ApplicationException(
	                "Exception in LanguageTranslation update "
	                        + e.getMessage());

	    } finally {

	        if (session != null) {
	            session.close();
	        }
	    }
	}

	public LanguageTranslationDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		LanguageTranslationDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (LanguageTranslationDTO) session.get(LanguageTranslationDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting LanguageTranslation by pk");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LanguageTranslationDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in LanguageTranslation list");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public List search(LanguageTranslationDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(LanguageTranslationDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<LanguageTranslationDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LanguageTranslationDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getSourceLanguage() != null && dto.getSourceLanguage().length() > 0) {
					criteria.add(Restrictions.like("sourceLanguage", dto.getSourceLanguage() + "%"));
				}
				if (dto.getTargetLanguage() != null && dto.getTargetLanguage().length() > 0) {
					criteria.add(Restrictions.like("targetLanguage", dto.getTargetLanguage() + "%"));
				}
				if (dto.getInputText() != null && dto.getInputText().length() > 0) {
					criteria.add(Restrictions.like("inputText", dto.getInputText() + "%"));
				}
				if (dto.getTranslatedText() != null && dto.getTranslatedText().length() > 0) {
					criteria.add(Restrictions.like("translatedText", dto.getTranslatedText() + "%"));
				}
			}
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<LanguageTranslationDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in LanguageTranslation search");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}