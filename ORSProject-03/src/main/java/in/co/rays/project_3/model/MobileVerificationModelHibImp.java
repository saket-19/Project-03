package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.MobileVerificationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of MobileVerification model
 * 
 * @author saket
 *
 */
public class MobileVerificationModelHibImp implements MobileVerificationModelInt {

	public long add(MobileVerificationDTO dto) throws ApplicationException, DuplicateRecordException {

		MobileVerificationDTO existDto = null;
		existDto = findByCode(dto.getVerificationCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Verification code already exist");
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
			throw new ApplicationException("Exception in MobileVerification Add " + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto.getId();
	}

	public void delete(MobileVerificationDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in MobileVerification Delete" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void update(MobileVerificationDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		MobileVerificationDTO existDto = findByCode(dto.getVerificationCode());
		// Check if updated VerificationCode already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Verification code is already exist");
		}

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in MobileVerification update" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public MobileVerificationDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		MobileVerificationDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (MobileVerificationDTO) session.get(MobileVerificationDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting MobileVerification by pk");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto;
	}

	public MobileVerificationDTO findByCode(String verificationCode) throws ApplicationException {
		Session session = null;
		MobileVerificationDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(MobileVerificationDTO.class);
			criteria.add(Restrictions.eq("verificationCode", verificationCode));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (MobileVerificationDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting MobileVerification by Code " + e.getMessage());
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
			Criteria criteria = session.createCriteria(MobileVerificationDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in MobileVerification list");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public List search(MobileVerificationDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(MobileVerificationDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<MobileVerificationDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(MobileVerificationDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getVerificationCode() != null && dto.getVerificationCode().length() > 0) {
					criteria.add(Restrictions.like("verificationCode", dto.getVerificationCode() + "%"));
				}
				if (dto.getMobileNumber() != null && dto.getMobileNumber().length() > 0) {
					criteria.add(Restrictions.like("mobileNumber", dto.getMobileNumber() + "%"));
				}
				if (dto.getOtp() != null && dto.getOtp().length() > 0) {
					criteria.add(Restrictions.like("otp", dto.getOtp() + "%"));
				}
				if (dto.getStatus() != null && dto.getStatus().length() > 0) {
					criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
				}
			}
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<MobileVerificationDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in MobileVerification search");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}