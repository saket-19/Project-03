package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EnrollmentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class EnrollmentModelHibImpl implements EnrollmentModelInt {

	@Override
	public void add(EnrollmentDTO dto)
			throws ApplicationException, DuplicateRecordException {

		EnrollmentDTO existDto = findByEnrollmentNo(dto.getEnrollmentNo());

		if (existDto != null) {
			throw new DuplicateRecordException("Enrollment number already exists");
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
			throw new ApplicationException("Exception in Enrollment Add " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(EnrollmentDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Enrollment delete " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(EnrollmentDTO dto)
			throws ApplicationException, DuplicateRecordException {

		EnrollmentDTO existDto = findByEnrollmentNo(dto.getEnrollmentNo());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Enrollment number already exists");
		}

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Enrollment update " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public List list() throws ApplicationException {
		return search(new EnrollmentDTO(), 0, 0);
	}

	@Override
	public List search(EnrollmentDTO dto, int pageNo, int pageSize)
			throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EnrollmentDTO.class);

			if (dto != null) {

				if (dto.getEnrollmentNo() != null
						&& dto.getEnrollmentNo().length() > 0) {
					criteria.add(Restrictions.like("enrollmentNo",
							dto.getEnrollmentNo() + "%"));
				}

				if (dto.getStudentName() != null
						&& dto.getStudentName().length() > 0) {
					criteria.add(Restrictions.like("studentName",
							dto.getStudentName() + "%"));
				}

				if (dto.getCourse() != null
						&& dto.getCourse().length() > 0) {
					criteria.add(Restrictions.like("course",
							dto.getCourse() + "%"));
				}

				if (dto.getEnrollmentDate() != null
						&& dto.getEnrollmentDate().getDate() > 0) {
					criteria.add(Restrictions.eq("enrollmentDate",
							dto.getEnrollmentDate()));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Enrollment search");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public EnrollmentDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		EnrollmentDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (EnrollmentDTO) session.get(EnrollmentDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in getting Enrollment by PK");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public EnrollmentDTO findByEnrollmentNo(String enrollmentNo)
			throws ApplicationException {

		Session session = null;
		EnrollmentDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EnrollmentDTO.class);
			criteria.add(Restrictions.eq("enrollmentNo", enrollmentNo));

			List list = criteria.list();

			if (list.size() > 0) {
				dto = (EnrollmentDTO) list.get(0);
			}

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in findByEnrollmentNo");
		} finally {
			session.close();
		}

		return dto;
	}
}