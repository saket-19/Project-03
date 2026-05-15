package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.WatchlistDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class WatchlistModelHibImp implements WatchlistModelInt {

	@Override
	public long add(WatchlistDTO dto) throws ApplicationException, DuplicateRecordException {
		
		WatchlistDTO existdto = findByName(dto.getName());
		if (existdto != null) {
			throw new DuplicateRecordException("name already exist");
		}
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(dto);
			
			tx.commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in add watchlist " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();
	}

	@Override
	public void delete(WatchlistDTO dto) throws ApplicationException {
		
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
			throw new ApplicationException("Exception in watchlist Delete" + e.getMessage());
		} finally {
			session.close();
		}
		
	}

	@Override
	public void update(WatchlistDTO dto) throws ApplicationException, DuplicateRecordException {
		
		WatchlistDTO existdto = findByName(dto.getName());
		if (existdto != null && existdto.getId() == dto.getId()) {
			throw new DuplicateRecordException("name already exist");
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
			throw new ApplicationException("Exception in wishlist update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(WatchlistDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("exception in watchlist list");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List search(WatchlistDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(WatchlistDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		Session session = null;
		List<WatchlistDTO> list = null;
		
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(WatchlistDTO.class);
			
			if (dto != null) {
				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}
				if (dto.getType() != null && dto.getType().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getType() + "%"));
				}
				if (dto.getGenre() != null && dto.getGenre().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getGenre() + "%"));
				}
				if (dto.getDescription() != null && dto.getDescription().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getDescription() + "%"));
				}
			}
			
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in watchlist search");
		} finally {
			session.close();
		}
		
		return list;
	}

	@Override
	public WatchlistDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		WatchlistDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (WatchlistDTO) session.get(WatchlistDTO.class , pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting watchlist by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public WatchlistDTO findByName(String name) throws ApplicationException {
		Session session = null;
		WatchlistDTO dto = null;
		
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(WatchlistDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (WatchlistDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting watchlist by name" + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

}
