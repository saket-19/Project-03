package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ParkingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class ParkingModelHibImpl implements ParkingModelInt {

    @Override
    public void add(ParkingDTO dto) throws ApplicationException, DuplicateRecordException {

        ParkingDTO existDto = findByLocation(dto.getLocation());
        if (existDto != null) {
            throw new DuplicateRecordException("Parking Location already exists");
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
            throw new ApplicationException("Exception in Parking Add " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(ParkingDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new ApplicationException("Exception in Parking delete " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(ParkingDTO dto) throws ApplicationException, DuplicateRecordException {

        ParkingDTO existDto = findByLocation(dto.getLocation());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Parking Location already exists");
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
            throw new ApplicationException("Exception in Parking update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }

    @Override
    public List search(ParkingDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(ParkingDTO.class);

            if (dto != null) {
                if (dto.getLocation() != null && dto.getLocation().length() > 0) {
                    criteria.add(Restrictions.like("location", dto.getLocation() + "%"));
                }
                if (dto.getCapacity() != null && dto.getCapacity() > 0) {
                    criteria.add(Restrictions.eq("capacity", dto.getCapacity()));
                }
                if (dto.getFee() != null && dto.getFee() > 0) {
                    criteria.add(Restrictions.eq("fee", dto.getFee()));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Parking search " + e.getMessage());
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public ParkingDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        ParkingDTO dto = null;
        try {
            session = HibDataSource.getSession();
            dto = (ParkingDTO) session.get(ParkingDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in getting Parking by PK");
        } finally {
            session.close();
        }
        return dto;
    }

    @Override
    public ParkingDTO findByLocation(String location) throws ApplicationException {

        Session session = null;
        ParkingDTO dto = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(ParkingDTO.class);
            criteria.add(Restrictions.eq("location", location));
            List list = criteria.list();
            if (list.size() > 0) {
                dto = (ParkingDTO) list.get(0);
            }
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in findByLocation of Parking");
        } finally {
            session.close();
        }
        return dto;
    }
}