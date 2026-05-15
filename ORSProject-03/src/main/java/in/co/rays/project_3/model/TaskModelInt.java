package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.TaskDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface TaskModelInt {

	public void add(TaskDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(TaskDTO dto) throws ApplicationException;

	public void update(TaskDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List search(TaskDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public TaskDTO findByPK(long pk) throws ApplicationException;

	public TaskDTO findByTaskCode(String taskCode) throws ApplicationException;

}