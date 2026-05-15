package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.UserProfileDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of UserProfile model
 * 
 * @author saket
 *
 */
public class UserProfileModelHibImp implements UserProfileModelInt {

	public long add(UserProfileDTO dto) throws ApplicationException, DuplicateRecordException {

		UserProfileDTO existDto = null;
		existDto = findByCode(dto.getProfileCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Profile code already exist");
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
			throw new ApplicationException("Exception in UserProfile Add " + e.getMessage());
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
		return dto.getId();
	}

	public void delete(UserProfileDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in UserProfile Delete" + e.getMessage());
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
	}

	public void update(UserProfileDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		UserProfileDTO existDto = findByCode(dto.getProfileCode());
		// Check if updated ProfileCode already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Profile code is already exist");
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
			throw new ApplicationException("Exception in UserProfile update" + e.getMessage());
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
	}

	public UserProfileDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		UserProfileDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (UserProfileDTO) session.get(UserProfileDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting UserProfile by pk");
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}

		return dto;
	}

	public UserProfileDTO findByCode(String profileCode) throws ApplicationException {
		Session session = null;
		UserProfileDTO dto = null;
		try {
			session = HibDataSource.getSession();
			if (session == null) {
				throw new ApplicationException("Could not get session from HibDataSource");
			}
			Criteria criteria = session.createCriteria(UserProfileDTO.class);
			criteria.add(Restrictions.eq("profileCode", profileCode));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (UserProfileDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting UserProfile by Code " + e.getMessage());
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
			Criteria criteria = session.createCriteria(UserProfileDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in UserProfile list");
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}

		return list;
	}

	public List search(UserProfileDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(UserProfileDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<UserProfileDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserProfileDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getProfileCode() != null && dto.getProfileCode().length() > 0) {
					criteria.add(Restrictions.like("profileCode", dto.getProfileCode() + "%"));
				}
				if (dto.getUserName() != null && dto.getUserName().length() > 0) {
					criteria.add(Restrictions.like("userName", dto.getUserName() + "%"));
				}
				if (dto.getMobileNumber() != null && dto.getMobileNumber().length() > 0) {
					criteria.add(Restrictions.like("mobileNumber", dto.getMobileNumber() + "%"));
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
			list = (ArrayList<UserProfileDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in UserProfile search");
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}

		return list;
	}
}