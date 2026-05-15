package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ShiftDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class ShiftModelHibImpl implements ShiftModelInt {

	@Override
	public long add(ShiftDTO dto) throws ApplicationException, DuplicateRecordException {

		ShiftDTO existDto = findByCode(dto.getCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Shift Code already exists");
		}

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		try {
			tx = session.beginTransaction();
			pk = (Long) session.save(dto);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in add Shift");
		} finally {
			session.close();
		}
		return pk;
	}

	@Override
	public void update(ShiftDTO dto) throws ApplicationException, DuplicateRecordException {

		ShiftDTO existDto = findByCode(dto.getCode());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Shift Code already exists");
		}

		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in update Shift");
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(ShiftDTO dto) throws ApplicationException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in delete Shift");
		} finally {
			session.close();
		}
	}

	@Override
	public ShiftDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		ShiftDTO dto = null;

		try {
			dto = (ShiftDTO) session.get(ShiftDTO.class, pk);
		} catch (Exception e) {
			throw new ApplicationException("Exception in findByPK");
		} finally {
			session.close();
		}
		return dto;
	}

	@Override
	public ShiftDTO findByCode(String code) throws ApplicationException {

		Session session = HibDataSource.getSession();
		ShiftDTO dto = null;

		try {
			Criteria criteria = session.createCriteria(ShiftDTO.class);
			criteria.add(Restrictions.eq("code", code));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (ShiftDTO) list.get(0);
			}
		} catch (Exception e) {
			throw new ApplicationException("Exception in findByCode");
		} finally {
			session.close();
		}
		return dto;
	}

	@Override
	public List search(ShiftDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ShiftDTO.class);

			if (dto != null) {

				if (dto.getCode() != null && dto.getCode().length() > 0) {
					criteria.add(Restrictions.like("code", dto.getCode() + "%"));
				}

				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (Exception e) {
			throw new ApplicationException("Exception in search Shift");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}
}