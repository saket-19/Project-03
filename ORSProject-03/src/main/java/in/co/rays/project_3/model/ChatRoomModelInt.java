package in.co.rays.project_3.model;

import java.util.List;
import in.co.rays.project_3.dto.ChatRoomDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of ChatRoom model
 * @author saket
 *
 */
public interface ChatRoomModelInt {
public long add(ChatRoomDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(ChatRoomDTO dto)throws ApplicationException;
public void update(ChatRoomDTO dto)throws ApplicationException,DuplicateRecordException;
public ChatRoomDTO findByPK(long pk)throws ApplicationException;
public ChatRoomDTO findByCode(String chatCode)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(ChatRoomDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(ChatRoomDTO dto)throws ApplicationException;
}