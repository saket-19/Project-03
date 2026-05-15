package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EmiDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class EmiModelHibImpl implements EmiModelInt {

    @Override
    public void add(EmiDTO dto) throws ApplicationException, DuplicateRecordException {

        EmiDTO existDto = findByEmiCode(dto.getEmiCode());
        if (existDto != null) {
            throw new DuplicateRecordException("EMI Code already exists");
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
            throw new ApplicationException("Exception in EMI Add " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(EmiDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new ApplicationException("Exception in EMI delete " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(EmiDTO dto) throws ApplicationException, DuplicateRecordException {

        EmiDTO existDto = findByEmiCode(dto.getEmiCode());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("EMI Code already exists");
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
            throw new ApplicationException("Exception in EMI update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }

    @Override
    public List search(EmiDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(EmiDTO.class);

            if (dto != null) {
                if (dto.getEmiCode() != null && dto.getEmiCode().length() > 0) {
                    criteria.add(Restrictions.like("emiCode", dto.getEmiCode() + "%"));
                }
                if (dto.getAmount() != null && dto.getAmount().length() > 0) {
                    criteria.add(Restrictions.like("amount", dto.getAmount() + "%"));
                }
                if (dto.getStatus() != null && dto.getStatus().length() > 0) {
                    criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
                }
                if (dto.getDueDate() != null) {
                    criteria.add(Restrictions.eq("dueDate", dto.getDueDate()));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in EMI search " + e.getMessage());
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public EmiDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        EmiDTO dto = null;
        try {
            session = HibDataSource.getSession();
            dto = (EmiDTO) session.get(EmiDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in getting EMI by PK");
        } finally {
            session.close();
        }
        return dto;
    }

    @Override
    public EmiDTO findByEmiCode(String emiCode) throws ApplicationException {

        Session session = null;
        EmiDTO dto = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(EmiDTO.class);
            criteria.add(Restrictions.eq("emiCode", emiCode));
            List list = criteria.list();
            if (list.size() > 0) {
                dto = (EmiDTO) list.get(0);
            }
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in findByEmiCode of EMI");
        } finally {
            session.close();
        }
        return dto;
    }
}