package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.InquiryDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of Inquiry model
 * 
 * @author krati
 *
 */
public class InquiryModelHibImp implements InquiryModelInt {

	public long add(InquiryDTO dto) throws ApplicationException, DuplicateRecordException {
		
		InquiryDTO existDto = null;
		existDto = findByEmail(dto.getEmail());
		if (existDto != null) {
			throw new DuplicateRecordException("email id already exist");
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
			throw new ApplicationException("Exception in Inquiry Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();
	}

	public void delete(InquiryDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Inquiry Delete " + e.getMessage());
		} finally {
			session.close();
		}
	}

	public void update(InquiryDTO dto) throws ApplicationException, DuplicateRecordException {
		
		InquiryDTO existDto = findByEmail(dto.getEmail());
		// Check if updated LoginId already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			 throw new DuplicateRecordException("email id already exist");
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
			throw new ApplicationException("Exception in Inquiry Update " + e.getMessage());
		} finally {
			session.close();
		}
	}

	public InquiryDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		InquiryDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (InquiryDTO) session.get(InquiryDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in getting Inquiry by PK");
		} finally {
			session.close();
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
			Criteria criteria = session.createCriteria(InquiryDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Inquiry List");
		} finally {
			session.close();
		}
		return list;
	}

	public List search(InquiryDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(InquiryDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<InquiryDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(InquiryDTO.class);

			if (dto != null) {
				if (dto.getInquirerName() != null && dto.getInquirerName().length() > 0) {
					criteria.add(Restrictions.like("inquirerName", dto.getInquirerName() + "%"));
				}
				if (dto.getEmail() != null && dto.getEmail().length() > 0) {
					criteria.add(Restrictions.like("email", dto.getEmail() + "%"));
				}
				if (dto.getInquirySubject() != null && dto.getInquirySubject().length() > 0) {
					criteria.add(Restrictions.like("inquirySubject", dto.getInquirySubject() + "%"));
				}
				if (dto.getInquiryStatus() != null && dto.getInquiryStatus().length() > 0) {
					criteria.add(Restrictions.like("inquiryStatus", dto.getInquiryStatus() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<InquiryDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Inquiry Search");
		} finally {
			session.close();
		}
		return list;
	}
	
	public InquiryDTO findByEmail(String email) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		InquiryDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(InquiryDTO.class);
			criteria.add(Restrictions.eq("email", email));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (InquiryDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting email " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}
}
