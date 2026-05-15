package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.TaskDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class TaskModelHibImpl implements TaskModelInt {

	@Override
	public void add(TaskDTO dto) throws ApplicationException, DuplicateRecordException {

		TaskDTO existDto = null;
		existDto = findByTaskCode(dto.getTaskCode());
		if (existDto != null) {
			throw new DuplicateRecordException("Task code already exists already exist");
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
			throw new ApplicationException("Exception in task Add " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(TaskDTO dto) throws ApplicationException {

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			throw new ApplicationException("Exception in task delete " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(TaskDTO dto) throws ApplicationException, DuplicateRecordException {

		TaskDTO existDto = findByTaskCode(dto.getTaskCode());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("task code already exists");
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
			throw new ApplicationException("Exception in task update " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}

	@Override
	public List search(TaskDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TaskDTO.class);

			if (dto != null) {

				if (dto.getTaskCode() != null && dto.getTaskCode().length() > 0) {
					criteria.add(Restrictions.like("taskCode", dto.getTaskCode() + "%"));
				}

				if (dto.getTask() != null && dto.getTask().length() > 0) {
					criteria.add(Restrictions.like("task", dto.getTask() + "%"));
				}

				if (dto.getAssignedTo() != null && dto.getAssignedTo().length() > 0) {
					criteria.add(Restrictions.like("assignedTo", dto.getAssignedTo() + "%"));
				}

				if (dto.getDueDate() != null && dto.getDueDate().getDate() > 0) {
					criteria.add(Restrictions.eq("dueDate", dto.getDueDate()));
				}

				if (dto.getTaskStatus() != null && dto.getTaskStatus().length() > 0) {
					criteria.add(Restrictions.like("taskStatus", dto.getTaskStatus() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("exception in task search");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public TaskDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		TaskDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (TaskDTO) session.get(TaskDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting task by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public TaskDTO findByTaskCode(String taskCode) throws ApplicationException {

		Session session = null;
		TaskDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TaskDTO.class);
			criteria.add(Restrictions.eq("taskCode", taskCode));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (TaskDTO) list.get(0);
			}
		} catch (HibernateException e) {
			throw new ApplicationException("exception in find by task code");
		} finally {
			session.close();
		}

		return dto;
	}
}