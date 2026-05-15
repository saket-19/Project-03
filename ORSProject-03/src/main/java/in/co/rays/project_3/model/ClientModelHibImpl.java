package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ClientDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class ClientModelHibImpl implements ClientModelInt {

	@Override
	public void add(ClientDTO dto) throws ApplicationException, DuplicateRecordException {

		ClientDTO existDto = null;
		existDto = findByCode(dto.getCode());

		if (existDto != null) {
			throw new DuplicateRecordException("Client code already exists");
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
			throw new ApplicationException("Exception in Client Add " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void delete(ClientDTO dto) throws ApplicationException {

		Session session = null;
		Transaction tx = null;

		try {

			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();

		} catch (HibernateException e) {

			tx.rollback();
			throw new ApplicationException("Exception in Client Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(ClientDTO dto) throws ApplicationException, DuplicateRecordException {

		ClientDTO existDto = findByCode(dto.getCode());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Client code already exists");
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
			throw new ApplicationException("Exception in Client Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}

	@Override
	public List search(ClientDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ClientDTO.class);

			if (dto != null) {

				if (dto.getCode() != null && dto.getCode().length() > 0) {
					criteria.add(Restrictions.like("code", dto.getCode() + "%"));
				}

				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}

				if (dto.getCompany() != null && dto.getCompany().length() > 0) {
					criteria.add(Restrictions.like("company", dto.getCompany() + "%"));
				}

				if (dto.getEmail() != null && dto.getEmail().length() > 0) {
					criteria.add(Restrictions.like("email", dto.getEmail() + "%"));
				}

				if (dto.getContact() != null && dto.getContact().length() > 0) {
					criteria.add(Restrictions.like("contact", dto.getContact() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Client Search");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public ClientDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		ClientDTO dto = null;

		try {

			session = HibDataSource.getSession();
			dto = (ClientDTO) session.get(ClientDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Client by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public ClientDTO findByCode(String code) throws ApplicationException {

		Session session = null;
		ClientDTO dto = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ClientDTO.class);
			criteria.add(Restrictions.eq("code", code));

			List list = criteria.list();

			if (list.size() > 0) {
				dto = (ClientDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in finding Client by Code");

		} finally {
			session.close();
		}

		return dto;
	}
}