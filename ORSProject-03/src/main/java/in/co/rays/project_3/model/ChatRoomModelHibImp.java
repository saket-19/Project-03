package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ChatRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of ChatRoom model
 * 
 * @author saket
 *
 */
public class ChatRoomModelHibImp implements ChatRoomModelInt {

	public long add(ChatRoomDTO dto) throws ApplicationException, DuplicateRecordException {

		ChatRoomDTO existDto = null;
		existDto = findByCode(dto.getChatCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Chat code already exist");
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
			throw new ApplicationException("Exception in ChatRoom Add " + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto.getId();
	}

	public void delete(ChatRoomDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in ChatRoom Delete" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void update(ChatRoomDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		ChatRoomDTO existDto = findByCode(dto.getChatCode());
		// Check if updated ChatCode already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Chat code is already exist");
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
			throw new ApplicationException("Exception in ChatRoom update" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public ChatRoomDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		ChatRoomDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (ChatRoomDTO) session.get(ChatRoomDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting ChatRoom by pk");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto;
	}

	public ChatRoomDTO findByCode(String chatCode) throws ApplicationException {
		Session session = null;
		ChatRoomDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ChatRoomDTO.class);
			criteria.add(Restrictions.eq("chatCode", chatCode));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (ChatRoomDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting ChatRoom by Code " + e.getMessage());
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
			Criteria criteria = session.createCriteria(ChatRoomDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in ChatRoom list");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public List search(ChatRoomDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(ChatRoomDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<ChatRoomDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ChatRoomDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getChatCode() != null && dto.getChatCode().length() > 0) {
					criteria.add(Restrictions.like("chatCode", dto.getChatCode() + "%"));
				}
				if (dto.getRoomName() != null && dto.getRoomName().length() > 0) {
					criteria.add(Restrictions.like("roomName", dto.getRoomName() + "%"));
				}
				if (dto.getCreatedBy() != null && dto.getCreatedBy().length() > 0) {
					criteria.add(Restrictions.like("createdBy", dto.getCreatedBy() + "%"));
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
			list = (ArrayList<ChatRoomDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in ChatRoom search");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}