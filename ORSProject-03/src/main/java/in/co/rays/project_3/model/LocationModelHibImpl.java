package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.LocationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class LocationModelHibImpl implements LocationModelInt {

	@Override
	public long add(LocationDTO dto) throws ApplicationException, DuplicateRecordException {
		
		LocationDTO existDto = null;
		existDto = findByCity(dto.getCity());
		if (existDto != null) {
			throw new DuplicateRecordException("location name already exists already exist");
		}
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		int pk = 0;
		try {

			
			tx = session.beginTransaction();

			session.save(dto);

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in location Add " + e.getMessage());
		} finally {
			session.close();
		}
		return pk;
	}

	@Override
	public void delete(LocationDTO dto) throws ApplicationException {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();	
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			
			throw new ApplicationException("Exception in location delete " + e.getMessage());
		} finally {
			session.close();
		}
		
	}

	@Override
	public void update(LocationDTO dto) throws ApplicationException, DuplicateRecordException {
		
		LocationDTO existDto = findByCity(dto.getCity());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("event name already exists");
		}
		
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new ApplicationException("Exception in event update " + e.getMessage());
		} finally {
			session.close();
		}
		
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return search(null, 0, 0);
	}

	@Override
	public List search(LocationDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LocationDTO.class);
			
			if (dto.getCity() != null && dto.getCity().length() > 0) {
				criteria.add(Restrictions.like("city", dto.getCity() + "%"));
			}
			if (dto.getState() != null && dto.getState().length() > 0) {
				criteria.add(Restrictions.like("state", dto.getState() + "%"));
			}
			if (dto.getCountry() != null && dto.getCountry().length() > 0) {
				criteria.add(Restrictions.eq("country", dto.getCountry()));
			}
			
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("exception in location search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public LocationDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		LocationDTO dto = null;
		
		try {
			session = HibDataSource.getSession();
			dto = (LocationDTO) session.get(LocationDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting location by pk");
		} finally {
			session.close();
		}
		
		return dto;
	}

	@Override
	public LocationDTO findByCity(String city) throws ApplicationException {
		Session session = null;
		LocationDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LocationDTO.class);
			criteria.add(Restrictions.eq("city", city));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (LocationDTO) list.get(0);
			}
		} catch (HibernateException e) {
			throw new ApplicationException("exception in find by city of location model");
		} finally {
			session.close();
		}
		return dto;
	}

}
