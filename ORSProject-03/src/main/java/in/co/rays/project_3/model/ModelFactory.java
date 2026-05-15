package in.co.rays.project_3.model;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * ModelFactory decides which model implementation run
 * 
 * @author saket
 *
 */
public final class ModelFactory {

	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.project_3.bundle.system");
	private static final String DATABASE = rb.getString("DATABASE");
	private static ModelFactory mFactory = null;
	private static HashMap modelCache = new HashMap();

	private ModelFactory() {

	}

	public static ModelFactory getInstance() {
		if (mFactory == null) {
			mFactory = new ModelFactory();
		}
		return mFactory;
	}



	public MarksheetModelInt getMarksheetModel() {
		MarksheetModelInt marksheetModel = (MarksheetModelInt) modelCache.get("marksheetModel");
		if (marksheetModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				marksheetModel = new MarksheetModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				marksheetModel = new MarksheetModelJDBCImpl();
			}
			modelCache.put("marksheetModel", marksheetModel);
		}
		return marksheetModel;
	}

	public CollegeModelInt getCollegeModel() {
		CollegeModelInt collegeModel = (CollegeModelInt) modelCache.get("collegeModel");
		if (collegeModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				collegeModel = new CollegeModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				collegeModel = new CollegeModelJDBCImpl();
			}
			modelCache.put("collegeModel", collegeModel);
		}
		return collegeModel;
	}

	public RoleModelInt getRoleModel() {
		RoleModelInt roleModel = (RoleModelInt) modelCache.get("roleModel");
		if (roleModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				roleModel = new RoleModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				roleModel = new RoleModelJDBCImpl();
			}
			modelCache.put("roleModel", roleModel);
		}
		return roleModel;
	}

	public UserModelInt getUserModel() {

		UserModelInt userModel = (UserModelInt) modelCache.get("userModel");
		if (userModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				userModel = new UserModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				userModel = new UserModelJDBCImpl();
			}
			modelCache.put("userModel", userModel);
		}

		return userModel;
	}

	public StudentModelInt getStudentModel() {
		StudentModelInt studentModel = (StudentModelInt) modelCache.get("studentModel");
		if (studentModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				studentModel = new StudentModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				studentModel = new StudentModelJDBCImpl();
			}
			modelCache.put("studentModel", studentModel);
		}

		return studentModel;
	}

	public CourseModelInt getCourseModel() {
		CourseModelInt courseModel = (CourseModelInt) modelCache.get("courseModel");
		if (courseModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				courseModel = new CourseModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				courseModel = new CourseModelJDBCImpl();
			}
			modelCache.put("courseModel", courseModel);
		}

		return courseModel;
	}

	public TimetableModelInt getTimetableModel() {

		TimetableModelInt timetableModel = (TimetableModelInt) modelCache.get("timetableModel");

		if (timetableModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				timetableModel = new TimetableModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				timetableModel = new TimetableModelJDBCImpl();
			}
			modelCache.put("timetableModel", timetableModel);
		}

		return timetableModel;
	}

	public SubjectModelInt getSubjectModel() {
		SubjectModelInt subjectModel = (SubjectModelInt) modelCache.get("subjectModel");
		if (subjectModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				subjectModel = new SubjectModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				subjectModel = new SubjectModelJDBCImpl();
			}
			modelCache.put("subjectModel", subjectModel);
		}

		return subjectModel;
	}

	public FacultyModelInt getFacultyModel() {
		FacultyModelInt facultyModel = (FacultyModelInt) modelCache.get("facultyModel");
		if (facultyModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				facultyModel = new FacultyModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				facultyModel = new FacultyModelJDBCImpl();
			}
			modelCache.put("facultyModel", facultyModel);
		}

		return facultyModel;
	}
	
	
	//usecases
	
	public WatchlistModelInt getWatchlistModel() {
		WatchlistModelInt watchlistModel = (WatchlistModelInt) modelCache.get("watchlistModel");
		if (watchlistModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				watchlistModel = new WatchlistModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
			}
		}
		return watchlistModel;
	}
	
	public EventModelInt getEventModel() {
		EventModelInt eventModel = (EventModelInt) modelCache.get("eventModel");
		if (eventModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				eventModel = new EventModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
			}
		}
		return eventModel;
	}
	
	public PaymentModelInt getPaymentModel() {
		PaymentModelInt paymentModel = (PaymentModelInt) modelCache.get("paymentModel");
		if (paymentModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				paymentModel = new PaymentModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
			}
		}
		return paymentModel;
	}
	
	public LocationModelInt getLocationModel() {
		LocationModelInt locationModel = (LocationModelInt) modelCache.get("locationModel");
		if (locationModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				locationModel = new LocationModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
			}
		}
		return locationModel;
	}
	
	public ProfileModelInt getProfileModel() {
		ProfileModelInt ProfileModel = (ProfileModelInt) modelCache.get("ProfileModel");
		if (ProfileModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				ProfileModel = new ProfileModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
			}
		}
		return ProfileModel;
	}
	
	public InquiryModelInt getInquiryModel() {

		InquiryModelInt inquiryModel = (InquiryModelInt) modelCache.get("inquiryModel");
		if (inquiryModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				inquiryModel = new InquiryModelHibImp();
			}
			modelCache.put("inquiryModel", inquiryModel);
		}
		return inquiryModel;
	}
	
	public TaskModelInt getTaskModel() {
	    TaskModelInt taskModel =
	            (TaskModelInt) modelCache.get("taskModel");

	    if (taskModel == null) {

	        if ("Hibernate".equals(DATABASE)) {
	            taskModel = new TaskModelHibImpl();
	        }

	        if ("JDBC".equals(DATABASE)) {
	           
	        }

	        modelCache.put("taskModel", taskModel);
	    }

	    return taskModel;
	}
	
	public MaintenanceModelInt getMaintenanceModel() {

	    MaintenanceModelInt maintenanceModel =
	            (MaintenanceModelInt) modelCache.get("maintenanceModel");

	    if (maintenanceModel == null) {

	        if ("Hibernate".equals(DATABASE)) {
	            maintenanceModel = new MaintenanceModelHibImpl();
	        }

	        if ("JDBC".equals(DATABASE)) {
	        }

	        modelCache.put("maintenanceModel", maintenanceModel);
	    }

	    return maintenanceModel;
	}
	
	public ClientModelInt getClientModel() {
		ClientModelInt clientModel = (ClientModelInt) modelCache.get("clientModel");

		if (clientModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				clientModel = new ClientModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
			}
		}
		return clientModel;
	}
	
	public EnrollmentModelInt getEnrollmentModel() {
		EnrollmentModelInt enrollmentModel = 
				(EnrollmentModelInt) modelCache.get("enrollmentModel");

		if (enrollmentModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				enrollmentModel = new EnrollmentModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				// Add JDBC implementation here if needed
			}
		}

		return enrollmentModel;
	}
	
	public EventRegistrationModelInt getEventRegistrationModel() {
	    EventRegistrationModelInt eventRegistrationModel = (EventRegistrationModelInt) modelCache.get("eventRegistrationModel");
	    if (eventRegistrationModel == null) {
	        if ("Hibernate".equals(DATABASE)) {
	            eventRegistrationModel = new EventRegistrationModelHibImpl();
	        }
	        if ("JDBC".equals(DATABASE)) {
	        }
	    }
	    return eventRegistrationModel;
	}
	
	public SupportTicketModelInt getSupportTicketModel() {
	    SupportTicketModelInt supportTicketModel = (SupportTicketModelInt) modelCache.get("supportTicketModel");
	    if (supportTicketModel == null) {
	        if ("Hibernate".equals(DATABASE)) {
	            supportTicketModel = new SupportTicketModelHibImpl();
	        }
	        if ("JDBC".equals(DATABASE)) {
	        }
	    }
	    return supportTicketModel;
	}
	
	public EmiModelInt getEmiModel() {
	    EmiModelInt emiModel = (EmiModelInt) modelCache.get("emiModel");
	    if (emiModel == null) {
	        if ("Hibernate".equals(DATABASE)) {
	            emiModel = new EmiModelHibImpl();
	        }
	        if ("JDBC".equals(DATABASE)) {
	        }
	    }
	    return emiModel;
	}
	
	public ParkingModelInt getParkingModel() {
	    ParkingModelInt parkingModel = (ParkingModelInt) modelCache.get("parkingModel");
	    if (parkingModel == null) {
	        if ("Hibernate".equals(DATABASE)) {
	            parkingModel = new ParkingModelHibImpl();
	        }
	        if ("JDBC".equals(DATABASE)) {
	        }
	    }
	    return parkingModel;
	}
	
	public AssetModelInt getAssetModel() {
	    AssetModelInt assetModel = (AssetModelInt) modelCache.get("assetModel");
	    if (assetModel == null) {
	        if ("Hibernate".equals(DATABASE)) {
	            assetModel = new AssetModelHibImpl();
	        }
	        if ("JDBC".equals(DATABASE)) {
	        }
	    }
	    return assetModel;
	}
	public BatchModelInt getBatchModel() {
		BatchModelInt batchModel = (BatchModelInt) modelCache.get("batchModel");
		if (batchModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				batchModel = new BatchModelHibImp();
			}
						modelCache.put("batchModel", batchModel);
		}
		return batchModel;
	}
	public UserProfileModelInt getUserProfileModel() {
		UserProfileModelInt userProfileModel = (UserProfileModelInt) modelCache.get("userProfileModel");
		if (userProfileModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				userProfileModel = new UserProfileModelHibImp();
			}
			
			modelCache.put("userProfileModel", userProfileModel);
		}
		return userProfileModel;
	}
	public LanguageTranslationModelInt getLanguageTranslationModel() {
		LanguageTranslationModelInt languageTranslationModel = (LanguageTranslationModelInt) modelCache.get("languageTranslationModel");
		if (languageTranslationModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				languageTranslationModel = new LanguageTranslationModelHibImp();
			}
			
			modelCache.put("languageTranslationModel", languageTranslationModel);
		}
		return languageTranslationModel;
	}
	public MobileVerificationModelInt getMobileVerificationModel() {
		MobileVerificationModelInt mobileVerificationModel = (MobileVerificationModelInt) modelCache.get("mobileVerificationModel");
		if (mobileVerificationModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				mobileVerificationModel = new MobileVerificationModelHibImp();
			}
			
			modelCache.put("mobileVerificationModel", mobileVerificationModel);
		}
		return mobileVerificationModel;
	}
	public GenderModelInt getGenderModel() {
		GenderModelInt genderModel = (GenderModelInt) modelCache.get("genderModel");
		if (genderModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				genderModel = new GenderModelHibImp();
			}
			
			modelCache.put("genderModel", genderModel);
		}
		return genderModel;
	}
	public VoiceCommandModelInt getVoiceCommandModel() {
		VoiceCommandModelInt voiceCommandModel = (VoiceCommandModelInt) modelCache.get("voiceCommandModel");
		if (voiceCommandModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				voiceCommandModel = new VoiceCommandModelHibImp();
			}
			
			modelCache.put("voiceCommandModel", voiceCommandModel);
		}
		return voiceCommandModel;
	}
	public ChatRoomModelInt getChatRoomModel() {
		ChatRoomModelInt chatRoomModel = (ChatRoomModelInt) modelCache.get("chatRoomModel");
		if (chatRoomModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				chatRoomModel = new ChatRoomModelHibImp();
			}
			
			modelCache.put("chatRoomModel", chatRoomModel);
		}
		return chatRoomModel;
	}
}
