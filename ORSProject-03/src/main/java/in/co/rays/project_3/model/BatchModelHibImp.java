package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.BatchDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of Batch model
 * 
 * @author saket
 *
 */
public class BatchModelHibImp implements BatchModelInt {

	public long add(BatchDTO dto) throws ApplicationException, DuplicateRecordException {

		BatchDTO existDto = null;
		existDto = findByCode(dto.getBatchCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Batch code already exist");
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
			throw new ApplicationException("Exception in Batch Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();

	}

	public void delete(BatchDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Batch Delete" + e.getMessage());
		} finally {
			session.close();
		}
	}

	public void update(BatchDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		BatchDTO existDto = findByCode(dto.getBatchCode());
		// Check if updated BatchCode already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Batch code is already exist");
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
			throw new ApplicationException("Exception in Batch update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	public BatchDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		BatchDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (BatchDTO) session.get(BatchDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Batch by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	public BatchDTO findByCode(String batchCode) throws ApplicationException {
		Session session = null;
		BatchDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(BatchDTO.class);
			criteria.add(Restrictions.eq("batchCode", batchCode));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (BatchDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting Batch by Code " + e.getMessage());

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
			Criteria criteria = session.createCriteria(BatchDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in Batch list");
		} finally {
			session.close();
		}

		return list;
	}

	public List search(BatchDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(BatchDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<BatchDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(BatchDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getBatchCode() != null && dto.getBatchCode().length() > 0) {
					criteria.add(Restrictions.like("batchCode", dto.getBatchCode() + "%"));
				}
				if (dto.getBatchName() != null && dto.getBatchName().length() > 0) {
					criteria.add(Restrictions.like("batchName", dto.getBatchName() + "%"));
				}
				if (dto.getStatus() != null && dto.getStatus().length() > 0) {
					criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
				}
				if (dto.getTotalRecords() > 0) {
					criteria.add(Restrictions.eq("totalRecords", dto.getTotalRecords()));
				}
			}
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<BatchDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Batch search");
		} finally {
			session.close();
		}

		return list;
	}

}