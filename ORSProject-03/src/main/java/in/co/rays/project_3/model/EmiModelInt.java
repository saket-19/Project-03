package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.EmiDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface EmiModelInt {

    public void add(EmiDTO dto) throws ApplicationException, DuplicateRecordException;
    public void delete(EmiDTO dto) throws ApplicationException;
    public void update(EmiDTO dto) throws ApplicationException, DuplicateRecordException;
    public List list() throws ApplicationException;
    public List search(EmiDTO dto, int pageNo, int pageSize) throws ApplicationException;
    public EmiDTO findByPK(long pk) throws ApplicationException;
    public EmiDTO findByEmiCode(String emiCode) throws ApplicationException;
}