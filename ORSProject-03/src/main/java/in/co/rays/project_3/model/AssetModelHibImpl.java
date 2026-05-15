package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.AssetDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class AssetModelHibImpl implements AssetModelInt {

    @Override
    public void add(AssetDTO dto) throws ApplicationException, DuplicateRecordException {

        AssetDTO existDto = findByAssetCode(dto.getAssetCode());
        if (existDto != null) {
            throw new DuplicateRecordException("Asset Code already exists");
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
            throw new ApplicationException("Exception in Asset Add " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(AssetDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;
        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw new ApplicationException("Exception in Asset delete " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void update(AssetDTO dto) throws ApplicationException, DuplicateRecordException {

        AssetDTO existDto = findByAssetCode(dto.getAssetCode());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Asset Code already exists");
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
            throw new ApplicationException("Exception in Asset update " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }

    @Override
    public List search(AssetDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(AssetDTO.class);

            if (dto != null) {
                if (dto.getAssetCode() != null && dto.getAssetCode().length() > 0) {
                    criteria.add(Restrictions.like("assetCode", dto.getAssetCode() + "%"));
                }
                if (dto.getAssetName() != null && dto.getAssetName().length() > 0) {
                    criteria.add(Restrictions.like("assetName", dto.getAssetName() + "%"));
                }
                if (dto.getAssetType() != null && dto.getAssetType().length() > 0) {
                    criteria.add(Restrictions.like("assetType", dto.getAssetType() + "%"));
                }
                if (dto.getAssetStatus() != null && dto.getAssetStatus().length() > 0) {
                    criteria.add(Restrictions.like("assetStatus", dto.getAssetStatus() + "%"));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Asset search " + e.getMessage());
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public AssetDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        AssetDTO dto = null;
        try {
            session = HibDataSource.getSession();
            dto = (AssetDTO) session.get(AssetDTO.class, pk);
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in getting Asset by PK");
        } finally {
            session.close();
        }
        return dto;
    }

    @Override
    public AssetDTO findByAssetCode(String assetCode) throws ApplicationException {

        Session session = null;
        AssetDTO dto = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(AssetDTO.class);
            criteria.add(Restrictions.eq("assetCode", assetCode));
            List list = criteria.list();
            if (list.size() > 0) {
                dto = (AssetDTO) list.get(0);
            }
        } catch (HibernateException e) {
            throw new ApplicationException("Exception in findByAssetCode of Asset");
        } finally {
            session.close();
        }
        return dto;
    }
}