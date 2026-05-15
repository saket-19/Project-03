package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.SupportTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class SupportTicketModelHibImpl implements SupportTicketModelInt {

    @Override
    public void add(SupportTicketDTO dto) throws ApplicationException, DuplicateRecordException {

        SupportTicketDTO existDto = findByTicketNo(dto.getTicketNo());
        if (existDto != null) {
            throw new DuplicateRecordException("Ticket No already exists");
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
            throw new ApplicationException("Exception in SupportTicket Add " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(SupportTicketDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new ApplicationException("Exception in SupportTicket delete " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(SupportTicketDTO dto) throws ApplicationException, DuplicateRecordException {

        SupportTicketDTO existDto = findByTicketNo(dto.getTicketNo());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Ticket No already exists");
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
            throw new ApplicationException("Exception in SupportTicket update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }

    @Override
    public List search(SupportTicketDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(SupportTicketDTO.class);

            if (dto != null) {
                if (dto.getTicketNo() != null && dto.getTicketNo().length() > 0) {
                    criteria.add(Restrictions.like("TicketNo", dto.getTicketNo() + "%"));
                }
                if (dto.getIssue() != null && dto.getIssue().length() > 0) {
                    criteria.add(Restrictions.like("issue", dto.getIssue() + "%"));
                }
                if (dto.getStatus() != null && dto.getStatus().length() > 0) {
                    criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
                }
                if (dto.getCreatedDate() != null) {
                    criteria.add(Restrictions.eq("createdDate", dto.getCreatedDate()));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in SupportTicket search " + e.getMessage());
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public SupportTicketDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        SupportTicketDTO dto = null;
        try {
            session = HibDataSource.getSession();
            dto = (SupportTicketDTO) session.get(SupportTicketDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in getting SupportTicket by PK");
        } finally {
            session.close();
        }
        return dto;
    }

    @Override
    public SupportTicketDTO findByTicketNo(String ticketNo) throws ApplicationException {

        Session session = null;
        SupportTicketDTO dto = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(SupportTicketDTO.class);
            criteria.add(Restrictions.eq("TicketNo", ticketNo));
            List list = criteria.list();
            if (list.size() > 0) {
                dto = (SupportTicketDTO) list.get(0);
            }
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in findByTicketNo of SupportTicket");
        } finally {
            session.close();
        }
        return dto;
    }
}