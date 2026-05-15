package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.co.rays.project_3.dto.ProfileDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ProfileModelHibImpl;
import in.co.rays.project_3.model.ProfileModelInt;

public class ProfileModelTest {

	public static ProfileModelInt model = new ProfileModelHibImpl();
	
	public static void main(String[] args) throws Exception {	
		testAdd();
	}

	private static void testAdd() throws ParseException, ApplicationException, DuplicateRecordException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		ProfileDTO dto = new ProfileDTO();
		dto.setName("Krati");
		dto.setGender("Female");
		dto.setDob(sdf.parse("31-12-1995"));
		dto.setStatus("active");
		
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		dto.setCreatedBy("admins");
		dto.setModifiedBy("admins");
		
		model.add(dto);
	}
}
