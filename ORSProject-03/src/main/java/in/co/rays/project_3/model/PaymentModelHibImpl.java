package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.PaymentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class PaymentModelHibImpl implements PaymentModelInt {

	@Override
	public void add(PaymentDTO dto) throws ApplicationException, DuplicateRecordException {

		PaymentDTO exitsdto = findByPaymentId(dto.getPaymentId());
		if (exitsdto != null) {
			throw new DuplicateRecordException("payment id already exists");
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
	public void delete(PaymentDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
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
			throw new ApplicationException("Exception in payment Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void update(PaymentDTO dto) throws ApplicationException, DuplicateRecordException {

		PaymentDTO existDto = findByPaymentId(dto.getPaymentId());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("payment id already exists");
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
			throw new ApplicationException("Exception in payment update " + e.getMessage());
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
	public List search(PaymentDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(PaymentDTO.class);

			if (dto.getPayerName() != null && dto.getPayerName().length() > 0) {
				criteria.add(Restrictions.like("payerName", dto.getPayerName() + "%"));
			}
			if (dto.getPaymentMode() != null && dto.getPaymentMode().length() > 0) {
				criteria.add(Restrictions.like("paymentMode", dto.getPaymentMode() + "%"));
			}
			if (dto.getPaymentStatus() != null && dto.getPaymentStatus().length() > 0) {
				criteria.add(Restrictions.like("paymentStatus", dto.getPaymentStatus()));
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("exception in payment search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public PaymentDTO findByPk(long pk) throws ApplicationException {
		Session session = null;
		PaymentDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (PaymentDTO) session.get(PaymentDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting event by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public PaymentDTO findByPaymentId(long paymentId) throws ApplicationException {
		Session session = null;
		PaymentDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(PaymentDTO.class);
			criteria.add(Restrictions.eq("paymentId", paymentId));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (PaymentDTO) list.get(0);
			}
		} catch (HibernateException e) {
			throw new ApplicationException("exception in find by payId of payment");
		} finally {
			session.close();
		}
		return dto;
	}

}
