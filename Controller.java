package com.EA.eform.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.EA.eform.bean.ParameterBean;
import com.EA.eform.db.model.Cache;
import com.EA.eform.handler.BKGHandler;
import com.EA.eform.log.LogController;
import com.EA.eform.object.BKGObject;
import com.EA.eform.service.FormService;
import com.EA.eform.step.AbstractStepList;
import com.EA.eform.step.SixStepsList;
import com.EA.eform.util.CommonUtil;
import com.EA.eform.util.Validator;
import com.EA.eform.util.http.HttpsUtil;
import com.EA.eform.util.object.FormParameter;

public class BKGController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5974331516266694212L;

	private static final String DEFAULT_STEP = "1";
	private static final String DEFAULT_SERVER_NAME = "0";
	private static final String SERVLET_PATH = "/hk/bkg/form";
	private static final String PATH = "/WEB-INF/efm/bkg/";
	private static final String FORM_GROUP_TYPE = "BKG";
	private static final String BKG_KEY_SESS = "BKGSESS";
	public static final Integer CONN_WAIT_TIME = 2000; // 2 sec
	private BKGHandler handler;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		boolean newService = false;
		String location = "application.jsp";
		ParameterBean pb = new ParameterBean();

		HttpSession session = request.getSession(false);
		// Session expired
		if (session == null) {
			newService = true;
			session = request.getSession();
			session.setAttribute(CommonUtil.STEP, DEFAULT_STEP);
		}

		Object serviceObj = session.getAttribute("serviceSessBKG");
		Object key = session.getAttribute("BkgkeySess");

		FormParameter fp = initParam(request, session);
		FormService service = getService(serviceObj, key, session, newService);
		handler = (BKGHandler)service.getHandler();

		service.setIsDesktop(fp.getDevice().isDesktop());
		service.setLang(fp.getLanguage());

		pb.setLanguage(service.getLang());
		pb.setDesktop(service.getIsDesktop());
		pb.setApp(fp.isApp());
		
		pb.setType(fp.getType());
		
		// Get Cache
		Cache cache = (Cache) getServletContext().getAttribute("Cache");
		List<String> holidayList= cache.getHolidayList();
		
		String step = (String) session.getAttribute(CommonUtil.STEP);
		
		HashMap<String, String> bkgFormList = new HashMap<String, String>();
		bkgFormList.put("u06", "u06");
		bkgFormList.put("u07", "u07");
		
		//boolean correctType = service.formTypeCheck(CommonUtil.nullFilter(request.getParameter("type")), bkgFormList);
		boolean correctType = bkgFormList.containsKey(CommonUtil.nullFilter(request.getParameter("type")));

		/**
		 * If Session step not equal to Request step Refresh or abnormal behavior
		 * detected
		 */
		
		System.out.println("##########newService: "+newService);
		System.out.println("##########step: "+step);
		System.out.println("##########fp.getStep(): "+fp.getStep());
		if (!newService && step != null && !step.equals(fp.getStep())) {
			LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost",
					"Session step not equal to Request step (Refresh or hacking), Form action is clear.");
			fp.setAction("");
		}

		// Get ServerName from websphere JVM argument
		String serverName = CommonUtil.nullFilter(System.getProperty("ServerName"));
		if (serverName.isEmpty()) {
			LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost",
					"Cannot retireve ServerName from websphere JVM argument.");
			serverName = DEFAULT_SERVER_NAME;
		}
		session.setAttribute("serverBit", serverName);
		
		if (correctType) {
			if (CommonUtil.NEXT.equals(fp.getAction())) {
				if (!service.csrfCheck(request, response, session))
					return;
	
				HashMap<String, Object> result = service.submit(request, session, fp.getStep());
				
				String afterSubmitJsonErrCode = "";
				
				if ("u06".equals(fp.getType())) {
					afterSubmitJsonErrCode = "2";
					
					if (Validator.ckErrorCode((String) result.get("ERROR_CODE"))) {
						JSONObject json = constructBookingJson(request);
						afterSubmitJsonErrCode = getSubmitResponseCode(json, request);
					}
				}
	
				pb.setStep((String) result.get("STEP"));
				pb.setError((String) result.get("ERROR_CODE")+afterSubmitJsonErrCode);

				LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "afterSubmitJsonErrCode: " + afterSubmitJsonErrCode);
				if ("u06".equals(fp.getType())) {
					if(afterSubmitJsonErrCode == "0"){
						LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "afterSubmitJsonErrCode=0, trigger storeDB function");
						LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "pb.getStep(): " + pb.getStep());
						handler.storeToDB(Integer.valueOf(pb.getStep())-1,1);
					}
					else{
						LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "afterSubmitJsonErrCode!=0");
					}
				}
				
				
				
				
	
			} else if (CommonUtil.EDIT.equals(fp.getAction())) {
				if (!service.csrfCheck(request, response, session))
					return;
	
				boolean result = Validator.ckJspValidEditAction(fp.getEdit(), new SixStepsList());
				pb.setStep((result) ? fp.getEdit() : DEFAULT_STEP);
	
			} else if (CommonUtil.BACK.equals(fp.getAction())) {
				if (!service.csrfCheck(request, response, session))
					return;
	
				pb.setStep(service.getValidStep(fp.getStep(), -1));
	
			} else {
				LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "No action is set");
				service = getService(null, null, null, true);
				service.setLang(fp.getLanguage());
				pb.setStep(DEFAULT_STEP);
			}
			
			pb.setLastStep(service.isLastStep(pb.getStep()));
		
		}

		
		session.setAttribute("BkgkeySess", BKG_KEY_SESS);
		session.setAttribute("serviceSessBKG", service);
		session.setAttribute(CommonUtil.STEP, pb.getStep());

		pb.setServerBit(serverName);

		request.setAttribute("data", service.getHandler().getObject());
		request.setAttribute("parameter", pb);
		request.setAttribute("holidays", holidayList);
		

				
		if (pb.isLastStep()) {
			// request.setAttribute("refNum", service.getHandler().getObject().getRefNum());
			
			if ("u07".equals(fp.getType())) {
				service.sendEmail(CommonUtil.getSubmitDateTime());
				request.setAttribute("refNum", service.getHandler().getObject().getRefNum());
			}

			session.removeAttribute("serviceSessBKG");
			session.removeAttribute("BkgkeySess");
		}

		/*location += service.getParameter();

		RequestDispatcher RequetsDispatcherObj = getServletContext().getRequestDispatcher(PATH + location);
		RequetsDispatcherObj.forward(request, response);*/
		
		if (fp.isOldApp() && !fp.isApp()) {
			location = "maintenance.jsp";
		}
		location += service.getParameter();
		if (correctType) {
			RequestDispatcher RequetsDispatcherObj =null;
			if(pb.isDesktop()){
				 RequetsDispatcherObj = getServletContext().getRequestDispatcher(PATH + location);	
			}else{
				//mobile
				if("u06".equalsIgnoreCase(pb.getType())){
					LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "SG forward to: "+ "step"+pb.getStep()+"_mb_sg.jsp");
					RequetsDispatcherObj = getServletContext().getRequestDispatcher(PATH + "step"+pb.getStep()+"_mb_sg.jsp");
					
				}else{
					LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "SGP forward to: "+ "step"+pb.getStep()+"_mb_sgp.jsp");
					RequetsDispatcherObj = getServletContext().getRequestDispatcher(PATH + "step"+pb.getStep()+"_mb_sgp.jsp");
	            }
			}
			
			RequetsDispatcherObj.forward(request, response);
		} else {
			LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "before redirect to error page");
			response.sendRedirect("/html/error/error.html");
			LogController.writeMessage(LogController.INFO, "", "BKGController", "doPost", "after redirect to error page");
		}
		
	}

	private FormService getService(Object serviceObj, Object key, HttpSession session, boolean newService) {
		if (!newService) {
			if (serviceObj != null && serviceObj instanceof FormService
					&& session.getAttribute(CommonUtil.STEP) != null) {
				if (key != null) {
					return (FormService) serviceObj;
				}
			}
		}
		return new FormService(AbstractStepList.TWO_STEPS_LIST, FORM_GROUP_TYPE);
	}

	private JSONObject constructBookingJson(HttpServletRequest request) {

		JSONObject postObj = new JSONObject();
		postObj.put("requestTimeStamp", "");
		postObj.put("captchaCode", "");

		JSONObject bookingInfo = new JSONObject();
		bookingInfo.put("bookingRefId", "");
		bookingInfo.put("bookingStatus", "");
		bookingInfo.put("phoneNumber", request.getParameter("countryCode") + "-" + request.getParameter("mobile"));
		bookingInfo.put("id", request.getParameter("hkId"));
		bookingInfo.put("email", request.getParameter("email"));
		bookingInfo.put("district", request.getParameter("district"));
		bookingInfo.put("branchCode", request.getParameter("branch"));
		bookingInfo.put("bookingDate", request.getParameter("date"));
		bookingInfo.put("bookingTime", request.getParameter("timeslot"));
		bookingInfo.put("referralCode", request.getParameter("referralCode"));
		//bookingInfo.put("bookingType", String.join(";", request.getParameterValues("productTypes")));
		bookingInfo.put("bookingType","");

		if ("en".equalsIgnoreCase(request.getParameter("language"))) {
			bookingInfo.put("lastName","");
			bookingInfo.put("title", request.getParameter("titles"));
			bookingInfo.put("firstName", request.getParameter("name"));
			bookingInfo.put("localLastName", "");
			bookingInfo.put("localTitle", "");
			bookingInfo.put("localFirstName", "");
			
		} else {
			bookingInfo.put("lastName", "");
			bookingInfo.put("title", "");
			bookingInfo.put("firstName", "");
			bookingInfo.put("localLastName", "");
			bookingInfo.put("localTitle", request.getParameter("titles"));
			bookingInfo.put("localFirstName", request.getParameter("name"));
			
		}

		postObj.put("bookingInfo", bookingInfo);

		return postObj;

	}

	private String getSubmitResponseCode(JSONObject json, HttpServletRequest request)
			throws ServletException, IOException {

		int count = 1;
		int maxRetryCount = 4;
		
		HttpsUtil.getNoCertTrustManager();
		String returnCode = "";
		String sDomain = "xxx";
		if (com.EA.eform.util.CommonUtil.isPROD()) {
			sDomain = "xxx";
		}
		
		while(true) {
			try {
				URL url = new URL("https://" + sDomain + "/bkg-api/booking/info");
				String tempCode = "";
				JSONObject response = null;
				//URLConnection con = url.openConnection();
				
				LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "Send contact URL " + url + " count # " + count + "start");
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
				con.setRequestMethod("POST"); // PUT is another valid option
				con.setConnectTimeout(CONN_WAIT_TIME);
				con.setReadTimeout(CONN_WAIT_TIME);
				con.setDoOutput(true);
		
				con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				con.connect();
		
				OutputStream os = con.getOutputStream();
				//System.out.println(json.toString());
				LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "json to BQS: " + json);
				
				byte[] out = json.toString().getBytes("utf-8");
				os.write(out, 0, out.length);
				
				if (con.getResponseCode() == 200) {
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
					StringBuilder responses = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						responses.append(responseLine.trim());
					}
					response = new JSONObject(responses.toString());
					tempCode = response.getString("responseCode");
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "200 responses: "+responses.toString());
				} else {
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
					StringBuilder responses = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						responses.append(responseLine.trim());
					}
					response = new JSONObject(responses.toString());
					tempCode = response.getString("responseCode");
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "!200 responses: "+responses.toString());
				}
				
				
				if ("00".equalsIgnoreCase(tempCode)) {
					request.setAttribute("refNum", response.getJSONObject("bookingInfo").getString("bookingReference"));
					BKGObject object = (BKGObject)handler.getObject();
					object.setBQSRefNum(request.getAttribute("refNum").toString());
					returnCode = "0";
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "returnCode = 0");
				} else if ("01".equalsIgnoreCase(tempCode)) {
					request.setAttribute("refNum", "");
					returnCode = "2";
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "refNum=empty, returnCode = 2");
				} else {
					request.setAttribute("refNum", "");
					returnCode = "";
					
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "refNum=empty, returnCode=empty");
				}

			} catch (Exception e) {
				count++;
				if (count == maxRetryCount) {
					request.setAttribute("refNum", "");
					returnCode = "";
					LogController.writeMessage(LogController.ERROR, "BKGController", "getSubmitResponseCode", "exception thrown: " + e.toString());
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "refNum=empty, returnCode=empty due to ConnectException");
				} else {
					LogController.writeMessage(LogController.ERROR, "BKGController", "getSubmitResponseCode", "exception thrown: " + e.toString());
					LogController.writeMessage(LogController.INFO, "", "BKGController", "getSubmitResponseCode", "ConnectException retry #" + count);
				}	
			}

			return returnCode;
		}
	}

	private FormParameter initParam(HttpServletRequest request, HttpSession session) {
		FormParameter fp = new FormParameter();

		fp.setChangeURL(false);
		// TODO type? m01?
		fp.setType(CommonUtil.nullFilter(request.getParameter("type")));
		fp.setAction(CommonUtil.nullFilter(request.getParameter("action")));
		fp.setEdit(CommonUtil.nullFilter(request.getParameter("edit")));
		fp.setStep(CommonUtil.nullFilter((String) request.getParameter("step")));
		fp.setLanguage(CommonUtil.nullFilter(request.getParameter("language")));
		fp.setUaHeader(CommonUtil.nullFilter(request.getHeader("user-agent")));
		fp.setWebview(CommonUtil.nullFilter(request.getParameter("webview")));
		fp.setAppType(CommonUtil.nullFilter(request.getParameter("AppType")));
		fp.setDevice(CommonUtil.getDeviceType(request.getHeader("user-agent")));
		fp.setRoot("Y".equals(request.getParameter("isRoot")));
		fp.setApp("Y".equals(request.getParameter("fromApp")));
		fp.setOldApp("Y".equals(request.getParameter("webview")));

		return fp;
	}

}
