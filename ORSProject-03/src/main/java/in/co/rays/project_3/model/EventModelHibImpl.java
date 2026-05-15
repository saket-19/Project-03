package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CourseDTO;
import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class EventModelHibImpl implements EventModelInt {

	@Override
	public void add(EventDTO dto) throws ApplicationException, DuplicateRecordException {
		
		
		EventDTO existDto = null;
		existDto = findByName(dto.getEventName());
		if (existDto != null) {
			throw new DuplicateRecordException("Event name already exists already exist");
		}
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {

			int pk = 0;
			tx = session.beginTransaction();

			session.save(dto);

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in event Add " + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void delete(EventDTO dto) throws ApplicationException {
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();	
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			
			throw new ApplicationException("Exception in event delete " + e.getMessage());
		} finally {
			session.close();
		}
		
	}

	@Override
	public void update(EventDTO dto) throws ApplicationException, DuplicateRecordException {
		
		EventDTO existDto = findByName(dto.getEventName());
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
	public List search(EventDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EventDTO.class);
			
			if (dto.getEventName() != null && dto.getEventName().length() > 0) {
				criteria.add(Restrictions.like("eventName", dto.getEventName() + "%"));
			}
			if (dto.getEventType() != null && dto.getEventType().length() > 0) {
				criteria.add(Restrictions.like("eventType", dto.getEventType() + "%"));
			}
			if (dto.getEventDate() != null && dto.getEventDate().getDate() > 0) {
				criteria.add(Restrictions.eq("eventDate", dto.getEventDate()));
			}
			if (dto.getCategory() != null && dto.getCategory().length() > 0) {
				criteria.add(Restrictions.like("category", dto.getCategory() + "%"));
			}
			
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("exception in event search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public EventDTO findByPK(long pk) throws ApplicationException {
		
		Session session = null;
		EventDTO dto = null;
		
		try {
			session = HibDataSource.getSession();
			dto = (EventDTO) session.get(EventDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting event by pk");
		} finally {
			session.close();
		}
		
		return dto;
	}

	@Override
	public EventDTO findByName(String name) throws ApplicationException {
		
		Session session = null;
		EventDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EventDTO.class);
			criteria.add(Restrictions.eq("eventName", name));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (EventDTO) list.get(0);
			}
		} catch (HibernateException e) {
			throw new ApplicationException("exception in find by name of event");
		} finally {
			session.close();
		}
		return dto;
	}

}
