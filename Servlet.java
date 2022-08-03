package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.EA.eform.log.LogController;
import com.EA.eform.util.http.HttpsUtil;


public class BKGServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2907911116052873332L;
	public static final Integer CONN_WAIT_TIME = 2000; // 2 sec
	
	@Override
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	 }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		 String lang = req.getParameter("lang");
		 String district = req.getParameter("district");
		 String type = req.getParameter("type");
		 
		 resp.setContentType("application/json");
		 PrintWriter out = resp.getWriter();
		 JSONArray sortedArray = new JSONArray();
		 JSONArray sortedJsonArray = new JSONArray();
		 HashMap<String, List<JSONObject>> timeslot = new HashMap<String, List<JSONObject>>();
		 HashMap<String, String> branchCodeMap = new HashMap<String, String>();
		 
		 HttpSession session = req.getSession();
		 
		 if ("district".equalsIgnoreCase(type)) {
			 sortedJsonArray.put("");
			 JSONObject json = readJsonFromUrl("/bkg-api/booking/branch", "GET", null);
			    if (json.has("branchesInfo") && json.get("branchesInfo") instanceof JSONArray) {
			    	JSONArray array = (JSONArray) json.get("branchesInfo");
			    	
			    	List<JSONObject> list = new ArrayList<JSONObject>();
			    	for (int j = 0; j < array.length(); j++) {
			    		list.add(array.getJSONObject(j));
			    	}
			    	
			    	Collections.sort(list, new Comparator() {
						@Override
						public int compare(Object a, Object b) {
							String str1 = null;
			    			String str2 = null;
			    			String str3 = null;
			    			String str4 = null;
			    			
			    			
			    			
			    			JSONObject jsona = (JSONObject) a;
			    			JSONObject jsonb = (JSONObject) b;
			    			
			    			try {
			    				
			    				str3 = (String) jsona.get("branchName");
			    				str4 = (String) jsonb.get("branchName");
			    				
			    				str1 = (String) jsona.get("branchAddress");
			    				str2 = (String) jsonb.get("branchAddress");

			    			} catch (JSONException e) {
			    				e.printStackTrace();
			    			}
			    			return ((str1.toLowerCase().compareTo(str2.toLowerCase()))&(str3.toLowerCase().compareTo(str4.toLowerCase())));
						}
			    		
			    	});
			    	
			    	for (int i = 0; i < array.length(); i++) {
			    		sortedArray.put(list.get(i));
			    	}
			    	
			    	for (int s = 0; s < sortedArray.length(); s++) {
			    		JSONObject branch = sortedArray.getJSONObject(s);
			    		if (district.equalsIgnoreCase(branch.getString("districtCode"))) {
			    			if (lang.equalsIgnoreCase("en")) {
			    				JSONObject jo = new JSONObject();
					    		jo.put(sortedArray.getJSONObject(s).getString("branchCode"), sortedArray.getJSONObject(s).getString("branchName")+" - "+sortedArray.getJSONObject(s).getString("branchAddress")); //obsolet branchName
					    		sortedJsonArray.put(jo);
					    		branchCodeMap.put(sortedArray.getJSONObject(s).getString("branchCode"), sortedArray.getJSONObject(s).getString("branchName")+" - "+sortedArray.getJSONObject(s).getString("branchAddress"));
					    		//jsonObj.put((String) branch.get("branchCode"), (String) branch.get("branchName"));
			    			} else {
			    				JSONObject jo = new JSONObject();
					    		jo.put(sortedArray.getJSONObject(s).getString("branchCode"), sortedArray.getJSONObject(s).getString("localBranchName")+" - "+sortedArray.getJSONObject(s).getString("localBranchAddress"));//obsolet localBranchName
					    		sortedJsonArray.put(jo);
					    		branchCodeMap.put(sortedArray.getJSONObject(s).getString("branchCode"), sortedArray.getJSONObject(s).getString("localBranchName")+" - "+sortedArray.getJSONObject(s).getString("localBranchAddress"));
			    			}
			    		}
			    		
			    		//sortedArray.put(list.get(s));
			    	}
			    	session.setAttribute("branchNameMap", branchCodeMap);
			    }
			    out.print(sortedJsonArray);
			    System.out.println("BKGServlet sortedJsonArray: "+sortedJsonArray.toString());
		 } else if ("branch".equalsIgnoreCase(type)) {
			
			JSONObject json = constructDistrictJson(req);
			
			List<String> dateList = new ArrayList<String>();
			
			JSONArray bookDateList = new JSONArray();
			bookDateList.put("");
			
			JSONObject dateJson = readJsonFromUrl("/bkg-api/booking/info/timeslot", "POST", json);
			
			
			if (dateJson.has("timeslotsInfo") && dateJson.get("timeslotsInfo") instanceof JSONArray) {
				
				JSONArray array = (JSONArray) dateJson.get("timeslotsInfo");
		    	
		    	List<JSONObject> list = new ArrayList<JSONObject>();
		    	
		    	for (int j = 0; j < array.length(); j++) {
		    		list.add(array.getJSONObject(j));
		    	}
		    	
		    	for (int i = 0; i < list.size(); i++) {
		    		//store it into array list for book date drop down
		    		JSONObject bookDate = list.get(i);
		    		JSONObject jo = new JSONObject();
		    		jo.put(bookDate.getString("bookDate"), bookDate.getString("bookDate"));
		    		bookDateList.put(jo);
		    		dateList.add(bookDate.getString("bookDate"));
		    		
		    		JSONArray tsarray = list.get(i).getJSONArray("timeslots");
		    		List<JSONObject> tllist = new ArrayList<JSONObject>();
		    		for (int o = 0; o < tsarray.length(); o++) {
		    			tllist.add(tsarray.getJSONObject(o));
			    	}
		    		timeslot.put((String) bookDate.getString("bookDate"), tllist);
		    	}
		    	
		    	session.setAttribute("avTimeSlot", timeslot);
		    	session.setAttribute("dateList", dateList);
		    	
			}
			
			out.print(bookDateList);
				 
		 } else if ("date".equalsIgnoreCase(type)) {
			 
			 String date = req.getParameter("date");
			 
			 List<String> timeList = new ArrayList<String>();
			 JSONArray timeslotList = new JSONArray();
			 timeslotList.put("");
			 
			 timeslot = (HashMap<String, List<JSONObject>>) session.getAttribute("avTimeSlot");
			 if (timeslot != null && date != null) {
				 List<JSONObject> jarray = timeslot.get(date);
				 if (jarray != null) {
					 System.out.println("jarray.size is: "+jarray.size());
					 for (int i = 0; i < jarray.size(); i++) {
						JSONObject jo = new JSONObject();
			    		//jo.put(jarray.get(i).getString("fromTime") + "-" + jarray.get(i).getString("toTime"), jarray.get(i).getString("fromTime") + "-" + jarray.get(i).getString("toTime"));
						jo.put(String.valueOf(i), jarray.get(i).getString("fromTime") + "-" + jarray.get(i).getString("toTime"));
						timeList.add(jarray.get(i).getString("fromTime") + "-" + jarray.get(i).getString("toTime"));
			    		
						/*
						if(i==jarray.size()){
				    		jo.put(jarray.get(i-1).getString("toTime"), jarray.get(i-1).getString("toTime"));
				    		timeList.add(jarray.get(i-1).getString("toTime"));

			    		}else{
			    			jo.put(jarray.get(i).getString("fromTime"),jarray.get(i).getString("fromTime"));
				    		timeList.add(jarray.get(i).getString("fromTime"));
				    	}
			    		*/
						
						timeslotList.put(jo);
					 }
					 session.setAttribute("timeList", timeList);
				 }
			 }
			 out.print(timeslotList);
			 
		 }/*else if ("health".equalsIgnoreCase(type)) {
			JSONObject json = readJsonFromUrl("/bkg-api/booking/admin/healthcheck", "GET", null);
			if (json.has("responseCode")) {
				out.print("ok");
			}else {
				out.print("");
			}
		 }*/
		
	 }
	
	 private String readAll(Reader rd) throws IOException {
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
		    }
		    return sb.toString();
		 }
	 
	 private JSONObject readJsonFromUrl(String inUrl, String method, JSONObject inJson) throws IOException, JSONException {
		 int count = 1;
		 int maxRetryCount = 4;
			
		 String sDomain = "xxx";
		 if (com.EA.eform.util.CommonUtil.isPROD()) {
			 sDomain = "xxx";
		 }
		 HttpsUtil.getNoCertTrustManager();
		 while (true) {
			 try {
				URL url = new URL("https://" + sDomain + inUrl);
				 
				LogController.writeMessage(LogController.INFO, "", "BKGServlet", "readJsonFromUrl", "url:"+url);
				 
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
				con.setRequestMethod(method); // PUT is another valid option
				con.setConnectTimeout(CONN_WAIT_TIME);
				con.setReadTimeout(CONN_WAIT_TIME);
				con.setDoOutput(true);

				con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				con.connect();
				 		 
				if (inJson != null) {
					OutputStream output = con.getOutputStream();
					byte[] byteOut = inJson.toString().getBytes("utf-8");
					output.write(byteOut, 0, byteOut.length);
				}
				 
				if (con.getResponseCode() == 200) {
					LogController.writeMessage(LogController.INFO, "", "BKGServlet", "response 200");
					BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
					String jsonText = readAll(rd);
					JSONObject json = new JSONObject(jsonText);
					LogController.writeMessage(LogController.INFO, "", "BKGServlet", "readJson:" + json.toString());
					return json;
				} else {
					LogController.writeMessage(LogController.INFO, "", "BKGServlet", "response not 200");
					return new JSONObject("{}");
				}
				 
			 } catch (Exception e) {
				count++;
				if (count == maxRetryCount) {
					LogController.writeMessage(LogController.ERROR, "BKGServlet", "readJsonFromUrl", "exception thrown: " + e.toString());
					LogController.writeMessage(LogController.INFO, "", "BKGServlet", "readJsonFromUrl", "response not 200 occurred max count, JSON empty");
					return new JSONObject("{}");
				} else {
					LogController.writeMessage(LogController.ERROR, "BKGServlet", "readJsonFromUrl", "exception thrown: " + e.toString());
					LogController.writeMessage(LogController.INFO, "", "BKGServlet", "readJsonFromUrl", "response not 200 retry #" + count);
				}
			}
		 }		 
	}
	 
	 private JSONObject constructDistrictJson (HttpServletRequest request) {
		  
			JSONObject postObj = new JSONObject(); 
			postObj.put("requestTimeStamp", "");
			
			JSONObject bookingInfo = new JSONObject();
			bookingInfo.put("branchCode", request.getParameter("branchCode"));

			postObj.put("bookingInfo", bookingInfo);
			
			return postObj;
			  
	}
	 
}
