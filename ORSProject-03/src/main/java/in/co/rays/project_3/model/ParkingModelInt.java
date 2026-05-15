package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.ParkingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface ParkingModelInt {

    public void add(ParkingDTO dto) throws ApplicationException, DuplicateRecordException;
    public void delete(ParkingDTO dto) throws ApplicationException;
    public void update(ParkingDTO dto) throws ApplicationException, DuplicateRecordException;
    public List list() throws ApplicationException;
    public List search(ParkingDTO dto, int pageNo, int pageSize) throws ApplicationException;
    public ParkingDTO findByPK(long pk) throws ApplicationException;
    public ParkingDTO findByLocation(String location) throws ApplicationException;
}