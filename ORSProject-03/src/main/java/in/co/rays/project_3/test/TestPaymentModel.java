package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.project_3.dto.PaymentDTO;

import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.PaymentModelHibImpl;
import in.co.rays.project_3.model.PaymentModelInt;


public class TestPaymentModel {
	
	public static PaymentModelInt model = new PaymentModelHibImpl();
	
	public static void main(String[] args) throws Exception {
//		addtest();
//		testUpdate();
//		testdelete();
//		testsearch();
//		testFindByPk();
//		testFindByName();
		
	
	}

	private static void testFindByName() throws ApplicationException {
		PaymentDTO dto = model.findByPaymentId(123L);
		System.out.println(dto.getPayerName());
	}

	private static void testFindByPk() throws ApplicationException {
		PaymentDTO dto = model.findByPk(1L);
		System.out.println(dto.getPayerName());
	}

	private static void testsearch() throws ApplicationException {
		PaymentDTO dto = new PaymentDTO();
		
		List list = model.search(dto, 1, 10);
		Iterator<PaymentDTO> it = list.iterator();
		while (it.hasNext()) {
			dto =  it.next();
			System.out.println(dto.getPayerName());
			System.out.println(dto.getPaymentMode());
		}
		
	}

//	private static void testdelete() throws ApplicationException {
//	
//		model.delete(2L);
//		System.out.println("data deleted successfully");
//	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException, ParseException {
		PaymentDTO dto = model.findByPk(1L);
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		dto.setPaymentStatus("pending");
		
		model.update(dto);
		System.out.println("data updated successfully");
	}

	private static void addtest() throws ApplicationException, DuplicateRecordException, ParseException {
		PaymentDTO dto = new PaymentDTO();
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		dto.setId(1L);
		dto.setPayerName("krati");
		dto.setPaymentId(123L);
		dto.setAmount(4500L);
		dto.setPaymentMode("cash");
		dto.setPaymentStatus("processed");
		dto.setModifiedBy("admin");
		dto.setCreatedBy("admin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.add(dto);
		System.out.println("data added successfully");
	}

}
