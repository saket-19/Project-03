package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.VoiceCommandDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of VoiceCommand model
 * 
 * @author saket
 *
 */
public class VoiceCommandModelHibImp implements VoiceCommandModelInt {

	public long add(VoiceCommandDTO dto) throws ApplicationException, DuplicateRecordException {

		VoiceCommandDTO existDto = null;
		existDto = findByCode(dto.getCommandCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Command code already exist");
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
			throw new ApplicationException("Exception in VoiceCommand Add " + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto.getId();
	}

	public void delete(VoiceCommandDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in VoiceCommand Delete" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void update(VoiceCommandDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
		VoiceCommandDTO existDto = findByCode(dto.getCommandCode());
		// Check if updated CommandCode already exist
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Command code is already exist");
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
			throw new ApplicationException("Exception in VoiceCommand update" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public VoiceCommandDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		VoiceCommandDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (VoiceCommandDTO) session.get(VoiceCommandDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting VoiceCommand by pk");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto;
	}

	public VoiceCommandDTO findByCode(String commandCode) throws ApplicationException {
		Session session = null;
		VoiceCommandDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(VoiceCommandDTO.class);
			criteria.add(Restrictions.eq("commandCode", commandCode));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (VoiceCommandDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting VoiceCommand by Code " + e.getMessage());
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
			Criteria criteria = session.createCriteria(VoiceCommandDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in VoiceCommand list");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public List search(VoiceCommandDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(VoiceCommandDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<VoiceCommandDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(VoiceCommandDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getCommandCode() != null && dto.getCommandCode().length() > 0) {
					criteria.add(Restrictions.like("commandCode", dto.getCommandCode() + "%"));
				}
				if (dto.getUserName() != null && dto.getUserName().length() > 0) {
					criteria.add(Restrictions.like("userName", dto.getUserName() + "%"));
				}
				if (dto.getCommandText() != null && dto.getCommandText().length() > 0) {
					criteria.add(Restrictions.like("commandText", dto.getCommandText() + "%"));
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
			list = (ArrayList<VoiceCommandDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in VoiceCommand search");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}