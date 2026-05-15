package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ProfileDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class ProfileModelHibImpl implements ProfileModelInt{

	@Override
	public long add(ProfileDTO dto) throws ApplicationException, DuplicateRecordException {
		ProfileDTO existDto = null;
		existDto = findByName(dto.getName());
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
	public void delete(ProfileDTO dto) throws ApplicationException {
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();	
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new ApplicationException("Exception in profile delete " + e.getMessage());
		} finally {
			session.close();
		}
		
	}

	@Override
	public void update(ProfileDTO dto) throws ApplicationException, DuplicateRecordException {
		
		ProfileDTO existDto = findByName(dto.getName());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("profile name already exists");
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
			throw new ApplicationException("Exception in profile update " + e.getMessage());
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
	public List search(ProfileDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ProfileDTO.class);
			
			if (dto.getName() != null && dto.getName().length() > 0) {
				criteria.add(Restrictions.like("name", dto.getName() + "%"));
			}
			if (dto.getGender() != null && dto.getGender().length() > 0) {
				criteria.add(Restrictions.like("gender", dto.getGender() + "%"));
			}
			
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("exception in profile search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public ProfileDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		ProfileDTO dto = null;
		
		try {
			session = HibDataSource.getSession();
			dto = (ProfileDTO) session.get(ProfileDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting profile by pk");
		} finally {
			session.close();
		}
		
		return dto;
	}

	@Override
	public ProfileDTO findByName(String name) throws ApplicationException {
		Session session = null;
		ProfileDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ProfileDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (ProfileDTO) list.get(0);
			}
		} catch (HibernateException e) {
			throw new ApplicationException("exception in find by name of profile model");
		} finally {
			session.close();
		}
		return dto;
	}

}
