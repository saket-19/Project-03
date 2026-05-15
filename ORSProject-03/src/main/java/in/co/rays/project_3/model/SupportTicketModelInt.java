package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.SupportTicketDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface SupportTicketModelInt {

    public void add(SupportTicketDTO dto) throws ApplicationException, DuplicateRecordException;
    public void delete(SupportTicketDTO dto) throws ApplicationException;
    public void update(SupportTicketDTO dto) throws ApplicationException, DuplicateRecordException;
    public List list() throws ApplicationException;
    public List search(SupportTicketDTO dto, int pageNo, int pageSize) throws ApplicationException;
    public SupportTicketDTO findByPK(long pk) throws ApplicationException;
    public SupportTicketDTO findByTicketNo(String ticketNo) throws ApplicationException;
    
}