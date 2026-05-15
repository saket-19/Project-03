package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EventModelHibImpl;
import in.co.rays.project_3.model.EventModelInt;
import in.co.rays.project_3.model.WatchlistModelHibImp;
import in.co.rays.project_3.model.WatchlistModelInt;

public class TestEventModel {
	
	public static EventModelInt model = new EventModelHibImpl();
	
	public static void main(String[] args) throws Exception {
//		addtest();
//		testUpdate();
//		testdelete();
//		testsearch();
//		testFindByPk();
//		testFindByName();
		
	
	}

	private static void testFindByName() throws ApplicationException {
		EventDTO dto = model.findByName("freshers");
		System.out.println(dto.getEventName());
	}

	private static void testFindByPk() throws ApplicationException {
		EventDTO dto = model.findByPK(1L);
		System.out.println(dto.getEventName());
	}

	private static void testsearch() throws ApplicationException {
		EventDTO dto = new EventDTO();
		
		List list = model.search(dto, 1, 10);
		Iterator<EventDTO> it = list.iterator();
		while (it.hasNext()) {
			dto =  it.next();
			System.out.println(dto.getEventName());
			System.out.println(dto.getEventDate());
		}
		
	}

	private static void testdelete() throws ApplicationException {
	
		EventDTO dto = new EventDTO();
		dto.setId(2L);
		model.delete(dto);
		System.out.println("data deleted successfully");
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException, ParseException {
		EventDTO dto = model.findByPK(1L);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		dto.setId(1L);
		dto.setEventName("freshers");
		dto.setEventType("Fest");
		dto.setEventDate(sdf.parse("10-02-2026"));
		dto.setCategory("Extra-CURRICULAR");
		dto.setModifiedBy("admin");
		dto.setCreatedBy("admin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.update(dto);
		System.out.println("data updated successfully");
	}

	private static void addtest() throws ApplicationException, DuplicateRecordException, ParseException {
		EventDTO dto = new EventDTO();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		dto.setId(1L);
		dto.setEventName("farewell");
		dto.setEventType("Fest");
		dto.setEventDate(sdf.parse("28-02-2026"));
		dto.setCategory("Extra-CURRICULAR");
		dto.setModifiedBy("admin");
		dto.setCreatedBy("admin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.add(dto);
		System.out.println("data added successfully");
	}

}
