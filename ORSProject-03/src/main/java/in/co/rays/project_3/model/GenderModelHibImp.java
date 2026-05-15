package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.GenderDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of Gender model
 * 
 * @author saket
 *
 */
public class GenderModelHibImp implements GenderModelInt {

	public long add(GenderDTO dto) throws ApplicationException, DuplicateRecordException {

		GenderDTO existDto = null;
		existDto = findByCode(dto.getGenderCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Gender code already exist");
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
			throw new ApplicationException("Exception in Gender Add " + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto.getId();
	}

	public void delete(GenderDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Gender Delete" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void update(GenderDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		GenderDTO existDto = findByCode(dto.getGenderCode());
		// Check if updated GenderCode already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Gender code is already exist");
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
			throw new ApplicationException("Exception in Gender update" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public GenderDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		GenderDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (GenderDTO) session.get(GenderDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Gender by pk");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto;
	}

	public GenderDTO findByCode(String genderCode) throws ApplicationException {
		Session session = null;
		GenderDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(GenderDTO.class);
			criteria.add(Restrictions.eq("genderCode", genderCode));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (GenderDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting Gender by Code " + e.getMessage());
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
			Criteria criteria = session.createCriteria(GenderDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in Gender list");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public List search(GenderDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(GenderDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<GenderDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(GenderDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getGenderCode() != null && dto.getGenderCode().length() > 0) {
					criteria.add(Restrictions.like("genderCode", dto.getGenderCode() + "%"));
				}
				if (dto.getGenderType() != null && dto.getGenderType().length() > 0) {
					criteria.add(Restrictions.like("genderType", dto.getGenderType() + "%"));
				}
				if (dto.getDescription() != null && dto.getDescription().length() > 0) {
					criteria.add(Restrictions.like("description", dto.getDescription() + "%"));
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
			list = (ArrayList<GenderDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Gender search");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}