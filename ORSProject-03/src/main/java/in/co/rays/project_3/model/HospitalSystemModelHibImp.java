package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.HospitalSystemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of HospitalSystem model
 * 
 * @author saket
 *
 */
public class HospitalSystemModelHibImp implements HospitalSystemModelInt {

	public long add(HospitalSystemDTO dto) throws ApplicationException, DuplicateRecordException {

	    Session session = HibDataSource.getSession();
	    Transaction tx = null;

	    try {

	        tx = session.beginTransaction();
	        session.save(dto);
	        tx.commit();

	    } catch (HibernateException e) {

	        if (tx != null) {
	            try {
	                tx.rollback();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }

	        throw new ApplicationException("Database Server is Down");

	    } finally {

	        if (session != null) {
	            session.close();
	        }
	    }

	    return dto.getId();
	}

	public void delete(HospitalSystemDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in HospitalSystem Delete" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void update(HospitalSystemDTO dto) throws ApplicationException, DuplicateRecordException {
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
			throw new ApplicationException("Exception in HospitalSystem update" + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public HospitalSystemDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		HospitalSystemDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (HospitalSystemDTO) session.get(HospitalSystemDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting HospitalSystem by pk");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dto;
	}

	public HospitalSystemDTO findByPatientName(String patientName) throws ApplicationException {
		Session session = null;
		HospitalSystemDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(HospitalSystemDTO.class);
			criteria.add(Restrictions.eq("patientName", patientName));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (HospitalSystemDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting HospitalSystem by PatientName " + e.getMessage());
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
			Criteria criteria = session.createCriteria(HospitalSystemDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in HospitalSystem list");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}

	public List search(HospitalSystemDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(HospitalSystemDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<HospitalSystemDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(HospitalSystemDTO.class);
			if (dto != null) {
				if (dto.getId() > 0) {
				    criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getPatientName() != null && dto.getPatientName().length() > 0) {
					criteria.add(Restrictions.like("patientName", dto.getPatientName() + "%"));
				}
				if (dto.getDoctorName() != null && dto.getDoctorName().length() > 0) {
					criteria.add(Restrictions.like("doctorName", dto.getDoctorName() + "%"));
				}
				if (dto.getDisease() != null && dto.getDisease().length() > 0) {
					criteria.add(Restrictions.like("disease", dto.getDisease() + "%"));
				}
				if (dto.getRoom() != null && dto.getRoom().length() > 0) {
					criteria.add(Restrictions.like("room", dto.getRoom() + "%"));
				}
			}
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<HospitalSystemDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in HospitalSystem search");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
}