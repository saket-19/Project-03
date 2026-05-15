package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.MaintenanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class MaintenanceModelHibImpl implements MaintenanceModelInt {

    @Override
    public void add(MaintenanceDTO dto)
            throws ApplicationException, DuplicateRecordException {

        MaintenanceDTO existDto = findByCode(dto.getCode());
        if (existDto != null) {
            throw new DuplicateRecordException("Code already exists");
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
            throw new ApplicationException("Exception in Maintenance Add " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(MaintenanceDTO dto)
            throws ApplicationException {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            throw new ApplicationException("Exception in Maintenance delete " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(MaintenanceDTO dto)
            throws ApplicationException, DuplicateRecordException {

        MaintenanceDTO existDto = findByCode(dto.getCode());

        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Code already exists");
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
            throw new ApplicationException("Exception in Maintenance update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List list() throws ApplicationException {
        return search(new MaintenanceDTO(), 0, 0);
    }

    @Override
    public List search(MaintenanceDTO dto, int pageNo, int pageSize)
            throws ApplicationException {

        Session session = null;
        List list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MaintenanceDTO.class);

            if (dto != null) {

                if (dto.getCode() != null && dto.getCode().length() > 0) {
                    criteria.add(Restrictions.like("code", dto.getCode() + "%"));
                }

                if (dto.getEquipmentName() != null && dto.getEquipmentName().length() > 0) {
                    criteria.add(Restrictions.like("equipmentName", dto.getEquipmentName() + "%"));
                }

                if (dto.getMaintenanceDate() != null && dto.getMaintenanceDate().getDate() > 0) {
                    criteria.add(Restrictions.eq("maintenanceDate", dto.getMaintenanceDate()));
                }

                if (dto.getTechnicianName() != null && dto.getTechnicianName().length() > 0) {
                    criteria.add(Restrictions.like("technicianName", dto.getTechnicianName() + "%"));
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

            list = criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Maintenance search");
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public MaintenanceDTO findByPK(long pk)
            throws ApplicationException {

        Session session = null;
        MaintenanceDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (MaintenanceDTO) session.get(MaintenanceDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in getting Maintenance by PK");
        } finally {
            session.close();
        }

        return dto;
    }

    @Override
    public MaintenanceDTO findByCode(String code)
            throws ApplicationException {

        Session session = null;
        MaintenanceDTO dto = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MaintenanceDTO.class);
            criteria.add(Restrictions.eq("code", code));

            List list = criteria.list();

            if (list.size() > 0) {
                dto = (MaintenanceDTO) list.get(0);
            }

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in findByCode of Maintenance");
        } finally {
            session.close();
        }

        return dto;
    }
}