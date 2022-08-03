package com.EA.eform.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.EA.eform.db.dao.DataDetailDAO;
import com.EA.eform.db.dao.DataMasterDAO;
import com.EA.eform.db.manager.DBManager;
import com.EA.eform.db.model.DataDetail;
import com.EA.eform.db.model.DataMaster;
import com.EA.eform.dom.fieldUnit.Field;
import com.EA.eform.log.LogController;
import com.EA.eform.object.BKGObject;
import com.EA.eform.object.FormObject;
import com.EA.eform.util.CommonUtil;
import com.EA.eform.util.Fmt;
import com.EA.eform.util.Validator;

public class BKGHandler extends AbstractHandler {
	
	private static final String refNumPrefix = "BK";
	private BKGObject bkgObj = null;
	private DataMaster dm = null;
	private String validationErrorCode = null;
	
	@Override
	public void setObject(FormObject obj) {
		try {
			if (bkgObj == null)
				bkgObj = (BKGObject) obj;
		} catch (Exception e) {
			LogController.writeExceptionMessage(LogController.ERROR, e);
			bkgObj = new BKGObject();
		}
	}

	@Override
	public FormObject getObject() {
		return bkgObj;
	}
	
	@Override
	public void storeObjField(HttpServletRequest req, int stepCount) {
		LogController.writeMessage(LogController.INFO, "BKGObject", "storeObjField",
				"set data to bkg object (step" + stepCount + ")");
		switch (stepCount) {
		case 1:
			bkgObj.setFormID(CommonUtil.nullFilter(req.getParameter("type")));

			if (bkgObj.getRefNum() == null || "".equals(bkgObj.getRefNum()))
				bkgObj.setRefNum(CommonUtil.genRefNum(BKGHandler.refNumPrefix, bkgObj.getServerBit(), bkgObj.getFormID()));

			bkgObj.setUserAgent(CommonUtil.filterUserAgent(CommonUtil.nullFilter(req.getHeader("user-agent"))));
			bkgObj.setLanguage(Validator.ckLanguage(CommonUtil.nullFilter(req.getParameter("language"))));

			String fromApp = CommonUtil.nullFilter(req.getParameter("fromApp"));

			if (!"".equals(fromApp) && "Y".equals(fromApp))
				bkgObj.setChannel("App");
			else if (bkgObj.isDesktop())
				bkgObj.setChannel("Desktop");
			else
				bkgObj.setChannel("Mobile Web");

			LogController.writeMessage(LogController.INFO, "BKGObject", "storeObjField",
					"bkgObj.getChannel: " + bkgObj.getChannel());

			// check OpenAPI Signature
			if (Validator.ckOpenAPISignature("", req) == 0) {
				bkgObj.setTspId(CommonUtil.nullFilter(req.getParameter("tspId")));
				bkgObj.setApplicationId(CommonUtil.nullFilter(req.getParameter("applicationId")));
				bkgObj.setTimestamp(CommonUtil.nullFilter(req.getParameter("timestamp")));
			}
					
			// Step 1 fields			
			
			bkgObj.setTitles(req.getParameter("titles"));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getTitles(): "+bkgObj.getTitles());
			
			bkgObj.setLastName(CommonUtil.nullFilter(req.getParameter("lastName")).toUpperCase());
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getLastName(): "+bkgObj.getLastName());
			
			bkgObj.setFirstName(CommonUtil.nullFilter(req.getParameter("firstName")).toUpperCase());
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getFirstName(): "+bkgObj.getFirstName());
			
			bkgObj.setFullName(CommonUtil.nullFilter(req.getParameter("name")).toUpperCase());
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getFullName(): "+bkgObj.getFullName());
			
			bkgObj.setCountryCode(CommonUtil.nullFilter(req.getParameter("countryCode")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getCountryCode(): "+bkgObj.getCountryCode());
			
			bkgObj.setMobile(CommonUtil.nullFilter(req.getParameter("mobile")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getMobile(): "+bkgObj.getMobile());
			
			bkgObj.setHkId(CommonUtil.nullFilter(req.getParameter("hkId")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getHkId(): "+bkgObj.getHkId());
			
			bkgObj.setEmail(CommonUtil.nullFilter(req.getParameter("email")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getEmail(): "+bkgObj.getEmail());
			
			bkgObj.setPromotionCode(Validator.ckChannelCode(req.getParameter("promotionCode"), ""));
			bkgObj.setChannelCode(Validator.ckChannelCode(req.getParameter("channelCode"), ""));
			bkgObj.setDistrict(CommonUtil.nullFilter(req.getParameter("district")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getDistrict(): "+bkgObj.getDistrict());
			
			//Temp hardcode as BQS not available start
			bkgObj.setBranch(CommonUtil.nullFilter(req.getParameter("branch")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getBranch(): "+bkgObj.getBranch());
			
			bkgObj.setDate(CommonUtil.nullFilter(req.getParameter("date")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getDate(): "+bkgObj.getDate());
			
			if("u07".equals(bkgObj.getFormID()) && !bkgObj.isDesktop()){
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - checking mobile dates");
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(bkgObj.getDate());
					LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - original date: " + date);
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
				    String strDate= formatter.format(date);  
				    LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - final date: " + strDate);
				    bkgObj.setDate(strDate);
				    LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getDate(): "+bkgObj.getDate());
				} catch (ParseException e) {
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeObjField", "Cannot parse String to Date: " + e.getMessage());
				}
				
			}
			
			bkgObj.setTimeslot(CommonUtil.nullFilter(req.getParameter("timeslot")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getTimeslot(): "+bkgObj.getTimeslot());	
			
			HashMap<String, String> branchCodeMap = (HashMap<String, String>) req.getSession().getAttribute("branchNameMap");
			if (req.getParameter("branch") == null || req.getParameter("branch").equals("")) {
				bkgObj.setBranchName("");
			}else {
				bkgObj.setBranchName(CommonUtil.nullFilter(branchCodeMap.get(req.getParameter("branch"))));
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getBranchName(): "+bkgObj.getBranchName());
			}
			
			bkgObj.setDateList((ArrayList<String>) req.getSession().getAttribute("dateList"));
			
			bkgObj.setTimeList((ArrayList<String>) req.getSession().getAttribute("timeList"));
			
			if (req.getParameterValues("productTypes") != null) {
				bkgObj.setProductTypes(String.join(";", (req.getParameterValues("productTypes"))));
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getProductTypes(): "+bkgObj.getProductTypes());	
			}else {
				bkgObj.setProductTypes("");
			}
			
			bkgObj.setBranchCodeMap((HashMap<String, String>) req.getSession().getAttribute("branchNameMap"));
			
			if("u06".equals(bkgObj.getFormID())){
				bkgObj.setReferralCode(CommonUtil.nullFilter(req.getParameter("referralCode")));
			}
			else{
				bkgObj.setReferralCode("");
			}
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getReferralCode(): "+bkgObj.getReferralCode());
			
			/*bkgObj.setBranch("1");
			bkgObj.setDate("1");
			bkgObj.setTimeslot("1");
			
			HashMap<String, String> branchCodeMap = new HashMap<String, String>();
			branchCodeMap.put("1", "1");
			bkgObj.setBranchName("1");
			if (req.getParameterValues("productTypes") != null) {
				bkgObj.setProductTypes(String.join(";", (req.getParameterValues("productTypes"))));
			}else {
				bkgObj.setProductTypes("");
			}*/
			//Temp hardcode as BQS not available end
			
			bkgObj.setConfirmCkBox1(CommonUtil.nullFilter(req.getParameter("pdpo")));
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "storeObjField - bkgObj.getConfirmCkBox1(): "+bkgObj.getConfirmCkBox1());	
			
			if(bkgObj.isDesktop()){
				bkgObj.setCaptchaCode(CommonUtil.nullFilter(req.getParameter("captchaCode")));
			}
			
			bkgObj.setSubmitDateTime(CommonUtil.getSubmitDateTime());
			//validationErrorCode = (String) req.getSession().getAttribute("validationErrorCode");
			
			
			break;
		}
	}

	@Override
	public String genErrorCode(int stepID) {
		String[] phonePrefix = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] titleVal = {"Mr.", "Mrs.", "Ms.", "Miss"};
		String[] districtVal = {"HK", "KLN", "NT"};
		String[] weekdayTime = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
		String[] weekendTime = {"09:00", "10:00", "11:00"};
		
		// Check the inputs before submit the JSON response code
		String error = "";
		//error += Validator.isNullOrEmpty(bkgObj.getTitles())? "1":"0";
				
		error += Validator.ckDropDownList(titleVal, bkgObj.getTitles());
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Titles Error: "+error);
		error += Validator.ckEngChiCharacters(bkgObj.getFullName().toUpperCase(), 50);
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Fullname Error: "+error);
		
				
//		if(bkgObj.isDesktop()){
//			error += Validator.ckEngChiCharacters(bkgObj.getFirstName().toUpperCase(), 50);	
//			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Firstname Error: "+error);			
//		}
		
		int countryCodeErr=0;
		countryCodeErr = Validator.ckDigitLength(bkgObj.getCountryCode(), 2, 3);
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Country Code Error: "+countryCodeErr);
		
		
		
		int phoneNumberErr = Validator.ckDigitLength(bkgObj.getMobile(), 8, 12);
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Phone Number Error: "+phoneNumberErr);
		
		if (countryCodeErr == 1 || phoneNumberErr == 1) {
			error += "1";
		}else if (countryCodeErr > 1 || phoneNumberErr > 1) {
			error += "2";
		}else {
			error += "0";
		}
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Phone Error: "+error);


		int emailFormatErr = Validator.ckEmailFormat(bkgObj.getEmail());
		int emailLengthErr = Validator.ckLength(bkgObj.getEmail(), 1, 50);
		
		if (emailFormatErr == 1 || emailLengthErr == 1) {
			error += "1";
		}else if (emailFormatErr > 1 || emailLengthErr > 1) {
			error += "2";
		}else {
			error += "0";
		}
		
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Email Error: "+error);
		
		if ("u06".equals(bkgObj.getFormID()) && !"".equals(bkgObj.getFormID())) {
			//error += Validator.isNullOrEmpty(bkgObj.getDistrict()) ? "1" : "0";
			error += Validator.ckDropDownList(districtVal, bkgObj.getDistrict());
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - District Error: "+error);
			
			if (bkgObj.getBranchCodeMap().keySet() != null) {
				String[] branchCodeVal = bkgObj.getBranchCodeMap().keySet().toArray(new String[bkgObj.getBranchCodeMap().keySet().size()]);
				//error += Validator.isNullOrEmpty(bkgObj.getBranch()) ? "1" : "0";
				error += Validator.ckDropDownList(branchCodeVal, bkgObj.getBranch());
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrorCode", "branchCodeVal[]:"+branchCodeVal.toString());
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Branch Error: "+error);
			} else {
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - getBranchCodeMap().keySet() == null");
				error += 1;
			}

			//Temp hardcode as BQS not available
			//String[] branchCodeVal = {"1"};
			
			if (bkgObj.getDateList() != null) {
				String[] dateVal = bkgObj.getDateList().toArray(new String[bkgObj.getDateList().size()]);
				
				//error += Validator.isNullOrEmpty(bkgObj.getDate()) ? "1" : "0";
				error += Validator.ckDropDownList(dateVal, bkgObj.getDate());
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrorCode", "dateVal[]:"+dateVal.toString());
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Date Error: "+error);
			} else {
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "getDateList() == null");
				error += 1;
			}

			//Temp hardcode as BQS not available
			//String[] dateVal = {"1"};

			if (bkgObj.getTimeList() != null) {
				String[] timeVal = bkgObj.getTimeList().toArray(new String[bkgObj.getTimeList().size()]);
				
				//error += Validator.isNullOrEmpty(bkgObj.getTimeslot()) ? "1" : "0";
				error += Validator.ckDropDownList(timeVal, bkgObj.getTimeslot());
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrorCode", "timeVal[]:"+timeVal.toString());
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - Time Error: "+error);
			} else {
				LogController.writeMessage(LogController.INFO, "", "BKGHandler", "getTimeList() == null");
				error += 1;
			}
			
			//error += Validator.isNullOrEmpty(bkgObj.getProductTypes()) || bkgObj.getProductTypes().length() == 0 ? "1" : "0";
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrorCode", "bkgObj.getProductTypes():"+bkgObj.getProductTypes());
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrorCode", "bkgObj.getProductTypes().length():"+bkgObj.getProductTypes().length());
			LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - ProductTypes Error: "+error);
		}
		
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - bkgObj.getDate(): "+bkgObj.getDate());
		
		if ("u07".equals(bkgObj.getFormID()) && !"".equals(bkgObj.getFormID())) {
			Calendar todayCal = Calendar.getInstance();
			todayCal.set(Calendar.HOUR_OF_DAY,0);
			todayCal.set(Calendar.MINUTE,0);
			todayCal.set(Calendar.SECOND,0);
			todayCal.set(Calendar.MILLISECOND,0);
			 
			Calendar monthAfterCal = Calendar.getInstance();
			monthAfterCal.add(Calendar.MONTH, +3);
			monthAfterCal.set(Calendar.HOUR_OF_DAY,23);
			monthAfterCal.set(Calendar.MINUTE,59);
			monthAfterCal.set(Calendar.SECOND,59);
			monthAfterCal.set(Calendar.MILLISECOND,999);
			 
			if(Validator.isThisDateValid(bkgObj.getDate(), "MM/dd/yyyy")) {
				if(Validator.inValidDayRange(bkgObj.getDate(), "MM/dd/yyyy", todayCal, monthAfterCal)) {
					error += "0";
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Check Appointment Date error: " + error);
				} else {
					error += "1";
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Check Appointment Date error: " + error);
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Check Appointment Date error: Invalid Day Range");
				}
			} else {
				error += "1";
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Check Appointment Date error: " + error);
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Check Appointment Date error: Invalid Date");
			}
			
			try {
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(bkgObj.getDate());
				Calendar appointmentCal = Calendar.getInstance();
				appointmentCal.setTime(date);
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Date: " + appointmentCal.getTime());
				if(appointmentCal.get(Calendar.DAY_OF_WEEK) == 7) {
					error += Validator.ckDropDownList(weekendTime, bkgObj.getTimeslot());
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Week of days: " + appointmentCal.get(Calendar.DAY_OF_WEEK));
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Time: " + bkgObj.getTimeslot());
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Time Error: Invalid Weekend Timeslot");
				} else if(appointmentCal.get(Calendar.DAY_OF_WEEK) > 1 && appointmentCal.get(Calendar.DAY_OF_WEEK) < 7) {
					error += Validator.ckDropDownList(weekdayTime, bkgObj.getTimeslot());
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Week of days: " + appointmentCal.get(Calendar.DAY_OF_WEEK));
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Time: " + bkgObj.getTimeslot());
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Time Error: Invalid Weekday Timeslot");
				}
				else {
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Appointment Time Error: Invalid Timeslot");
					error += 2;
				}
			} catch (ParseException e) {
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "Cannot parse String to Date: " + e.getMessage());
					error += 1;
			}  
		}
			
		//Temp hardcode as BQS not available
		//String[] timeVal = {"1"};	
		
		if(!"".equals(bkgObj.getReferralCode())){
			error += Validator.ckEngNumCharacters(bkgObj.getReferralCode(), 15);
		}
		else{
			error += 0;
		}
	   			
		error +=("on".equalsIgnoreCase(bkgObj.getConfirmCkBox1()))?"0":"1";
		LogController.writeMessage(LogController.INFO, "", "BKGHandler", "genErrCode - TnC Error: "+error);
		
		
		
		if(bkgObj.isDesktop()){
			error += Validator.ckCaptchaCode(bkgObj.getAuthCode(), bkgObj.getCaptchaCode());	
		}
		
		//error += Validator.isNullOrEmpty(bkgObj.getConfirmCkBox1()) ? "1" : "0";
		

		//session.setAttribute("validationErrorCode", error);
		
		LogController.writeMessage(LogController.DEBUG, "BKGHandler", "genErrorCode", "step-" + stepID + "error code: " + error);
		return error;
	}

	@Override
	public void storeObjFormID(String formID) {
		bkgObj.setFormID(formID);
	}

	@Override
	public int storeToDB(int step, int section) {   
		LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Start &  end");
		Connection conn = null;

		try {
			conn = DBManager.makeConnection();
			conn.setAutoCommit(false);
			LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "set data to db (step" + step + "_" + section + ")");
			Timestamp ts = new Timestamp(new Date().getTime());

			// Create/Update Master
			if (dm == null) {
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Master is null. Start to Create.");
				dm = new DataMaster(ts, ts, -1, this.bkgObj.getRefNum(), this.bkgObj.getFormID(), this.bkgObj.getiPAddress(), bkgObj.getUserAgent(), this.bkgObj.getChannelCode(), this.bkgObj.getLanguage(), bkgObj.getChannel(), "completed", null);
				int master_id = DataMasterDAO.creatMasterInDB(dm, conn);
				if (master_id != -1)
					dm.setMaster_id(master_id);
				else {
					LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "master_id is null or -1");
					return 1;
				}
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Master is created. Master ID: " + master_id);
			} else {
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Master exist. Start to Update");
				dm.setUpdate_date_time(ts);

				if (step == 1)
					dm.setStatus("completed");
				else
					dm.setStatus(step + "_" + section);
				int result = DataMasterDAO.updateMasterinDB(dm, conn);
				if (result != -1)
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Master updated. Master ID: " + dm.getMaster_id());
				else {
					LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "Master update failed. Master ID: " + dm.getMaster_id());
					return 2;
				}
			}

			LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Create/Update Detail");
			// Create/Update Detail
			// Set General Data
			String[] keys = null;
			String[] values = null;
			HashMap<String, DataDetail> tmp_details = null;
			boolean hvDetail = false;
			switch (step) {
				case 1:
				   	
					if("u06".equals(this.bkgObj.getFormID())){
						keys = new String[] { "titles", "lastName", "firstName", "fullName", "countryCode", "mobile", "email", "district", "branch", "branchName", "date", "timeslot", "referralCode", "productTypes", "ConfirmCkBox1", "BQSRefNum"};
						values = new String[] {bkgObj.getTitles(), bkgObj.getLastName(), bkgObj.getFirstName(), bkgObj.getFullName(),bkgObj.getCountryCode(), bkgObj.getMobile(), bkgObj.getEmail(), bkgObj.getDistrict(), bkgObj.getBranch(), bkgObj.getBranchName(), bkgObj.getDate(), bkgObj.getTimeslot(), bkgObj.getReferralCode(), bkgObj.getProductTypes(), bkgObj.getConfirmCkBox1(), bkgObj.getBQSRefNum()};
						tmp_details = CommonUtil.createAllDetail( this.dm.getMaster_id(), keys, values, step, section);
		
						String[] tmpKeys = tmp_details.keySet().toArray(new String[tmp_details.size()]);
						System.out.println("tmpKeys.length: " + tmpKeys.length);
						hvDetail = true;
					}
					
					
					if("u07".equals(this.bkgObj.getFormID())){
						keys = new String[] { "titles", "lastName", "firstName", "fullName", "countryCode", "mobile", "email", "date", "timeslot", "ConfirmCkBox1"};
						values = new String[] {bkgObj.getTitles(), bkgObj.getLastName(), bkgObj.getFirstName(),bkgObj.getFullName(), bkgObj.getCountryCode(), bkgObj.getMobile(), bkgObj.getEmail(), bkgObj.getDate(), bkgObj.getTimeslot(), bkgObj.getConfirmCkBox1()};
						tmp_details = CommonUtil.createAllDetail( this.dm.getMaster_id(), keys, values, step, section);
		
						String[] tmpKeys = tmp_details.keySet().toArray(new String[tmp_details.size()]);
						System.out.println("tmpKeys.length: " + tmpKeys.length);
						hvDetail = true;
					}
					break;

			}

			if (hvDetail) {
				if (tmp_details != null) {
					String[] tmpKeys = tmp_details.keySet().toArray(new String[tmp_details.size()]);

					boolean isUpdate = false;
					if (dm.getDetails() != null) {
						LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Already have detail. Check update");
						for (String tmpKey : tmpKeys) {
							if (dm.getDetails().get(tmpKey) != null) {
								isUpdate = true;
								break;
							}
						}
					} else
						LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "No detail found when checking update.");

					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Check detail exist: " + isUpdate);

					if (!isUpdate) {
						// insert detail to handler master
						for (String tmpKey : tmpKeys) {
							if (dm.getDetails() == null) {
								dm.setDetails(tmp_details);
							} else {
								dm.getDetails().put(tmpKey, tmp_details.get(tmpKey));
							}

						}
						// Call Dao Create Detail
						int result = DataDetailDAO.CreateDetailinDB(tmp_details, conn);
						if (result != -1)
							LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Detail Inserted.");
						else {
							LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "Detail Insert failed.");
							return 3;
						}
					} else {
						// update detail to handler master
						LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "start to update detail in java object");
						for (String tmpKey : tmpKeys) {
							dm.getDetails().get(tmpKey).setField_value(tmp_details.get(tmpKey).getField_value());
						}
						LogController.writeMessage(LogController.DEBUG,"BKGHandler", "storeToDB", "Finished to update detail in java object");
						// Call Dao update Detail
						// TODO
						int result = DataDetailDAO.UpdateDetailinDB(tmp_details, conn);
						if (result != -1)
							LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Detail updated.");
						else {
							LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "Detail update failed.");
							return 3;
						}
					}
					LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "Store DB Success.");

				} else {
					LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "Details is null StepID: " + step);
					return 5;
				}

			}

		} catch (Exception e) {
			LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "Exception: " + e);
			return 6;
		} finally {
			if (conn != null) {
				try {
					conn.commit();
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					LogController.writeMessage(LogController.ERROR, "BKGHandler", "storeToDB", "Exception: " + e);
					return 7;
				}
				DBManager.closeConnection(conn);
				LogController.writeMessage(LogController.DEBUG, "BKGHandler", "storeToDB", "storeToDB end");
			}
		}
		return 0;

	}

	@Override
	public int validateSpecialField(Field field, String input) {
		// TODO Auto-generated method stub
		return 0;
	}
}
