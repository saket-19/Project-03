package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EventRegistrationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class EventRegistrationModelHibImpl implements EventRegistrationModelInt {

    @Override
    public void add(EventRegistrationDTO dto) throws ApplicationException, DuplicateRecordException {

        EventRegistrationDTO existDto = null;
        existDto = findByParticipant(dto.getParticipant());
        if (existDto != null) {
            throw new DuplicateRecordException("Event Registration already exists");
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
            throw new ApplicationException("Exception in EventRegistration Add " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(EventRegistrationDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new ApplicationException("Exception in EventRegistration delete " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(EventRegistrationDTO dto) throws ApplicationException, DuplicateRecordException {

        EventRegistrationDTO existDto = findByParticipant(dto.getParticipant());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Event Registration already exists");
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
            throw new ApplicationException("Exception in EventRegistration update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }

    @Override
    public List search(EventRegistrationDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(EventRegistrationDTO.class);

            if (dto != null) {
                if (dto.getParticipant() != null && dto.getParticipant().length() > 0) {
                    criteria.add(Restrictions.like("participant", dto.getParticipant() + "%"));
                }
                if (dto.getEventName() != null && dto.getEventName().length() > 0) {
                    criteria.add(Restrictions.like("eventName", dto.getEventName() + "%"));
                }
                if (dto.getTicketType() != null && dto.getTicketType().length() > 0) {
                    criteria.add(Restrictions.like("ticketType", dto.getTicketType() + "%"));
                }
                if (dto.getPaymentMode() != null && dto.getPaymentMode().length() > 0) {
                    criteria.add(Restrictions.like("paymentMode", dto.getPaymentMode() + "%"));
                }
                if (dto.getSeatNumber() != null && dto.getSeatNumber().length() > 0) {
                    criteria.add(Restrictions.like("seatNumber", dto.getSeatNumber() + "%"));
                }
                if (dto.getRegistrationDate() != null) {
                    criteria.add(Restrictions.eq("registrationDate", dto.getRegistrationDate()));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in EventRegistration search " + e.getMessage());
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public EventRegistrationDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        EventRegistrationDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (EventRegistrationDTO) session.get(EventRegistrationDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in getting EventRegistration by PK");
        } finally {
            session.close();
        }

        return dto;
    }

    @Override
    public EventRegistrationDTO findByParticipant(String participant) throws ApplicationException {

        Session session = null;
        EventRegistrationDTO dto = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(EventRegistrationDTO.class);
            criteria.add(Restrictions.eq("participant", participant));
            List list = criteria.list();
            if (list.size() > 0) {
                dto = (EventRegistrationDTO) list.get(0);
            }
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in findByParticipant of EventRegistration");
        } finally {
            session.close();
        }
        return dto;
    }
}