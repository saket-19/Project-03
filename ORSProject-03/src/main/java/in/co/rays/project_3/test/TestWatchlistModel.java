package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.dto.WatchlistDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.WatchlistModelHibImp;
import in.co.rays.project_3.model.WatchlistModelInt;

public class TestWatchlistModel {
	
	public static WatchlistModelInt model = new WatchlistModelHibImp();
	
	public static void main(String[] args) throws Exception {
//		addtest();
//		testUpdate();
//		testdelete();
//		testsearch();
//		testFindByPk();
//		testFindByName();
		
	
	}

	private static void testFindByName() throws ApplicationException {
		WatchlistDTO dto = model.findByName("people we meet on vacation");
		System.out.println(dto.getName());
	}

	private static void testFindByPk() throws ApplicationException {
		WatchlistDTO dto = model.findByPK(1L);
		System.out.println(dto.getName());
	}

	private static void testsearch() throws ApplicationException {
		WatchlistDTO dto = new WatchlistDTO();
		
		List list = model.search(dto);
		Iterator<WatchlistDTO> it = list.iterator();
		while (it.hasNext()) {
			dto =  it.next();
			System.out.println(dto.getName());
			System.out.println(dto.getGenre());
		}
		
	}

	private static void testdelete() throws ApplicationException {
	
		WatchlistDTO dto = new WatchlistDTO();
		dto.setId(2L);
		model.delete(dto);
		System.out.println("data deleted successfully");
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException {
		WatchlistDTO dto = new WatchlistDTO();
		dto.setId(1L);
		dto.setName("people we meet on vacation");
		dto.setType("movie");
		dto.setGenre("romcom");
		dto.setDescription("must watch");
		dto.setModifiedBy("admin");
		dto.setCreatedBy("admin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.update(dto);
		System.out.println("data updated successfully");
	}

	private static void addtest() throws ApplicationException, DuplicateRecordException {
		WatchlistDTO dto = new WatchlistDTO();
		dto.setName("stranger things");
		dto.setType("series");
		dto.setGenre("fantasy thriller");
		dto.setDescription("done watching");
		dto.setModifiedBy("admin");
		dto.setCreatedBy("admin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.add(dto);
		System.out.println("data added successfully");
	}

}
