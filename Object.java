package com.EA.eform.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.EA.eform.util.AppSummary;

public class BKGObject extends FormObject {
	
	// Step 1
	private String titles, lastName, firstName, fullName, chineseName, countryCode, mobile, hkId, email, district, branch, date, timeslot, referralCode, idNo, idType, idDigit, passportNo,  promotionCode, preSelectDistrict;
	

	// Step 2
	private String branchName;
	
	// Step3
	private String confirmCkBox1;
	
	// system
	private String channel;
	
	private List<String> dateList;
	private List<String> timeList;
	private String productTypes;
	private HashMap<String, String> branchCodeMap;
	private String BQSRefNum;

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getHkId() {
		return hkId;
	}

	public void setHkId(String hkId) {
		this.hkId = hkId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}
	
	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getIdDigit() {
		return idDigit;
	}

	public void setIdDigit(String idDigit) {
		this.idDigit = idDigit;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPreSelectDistrict() {
		return preSelectDistrict;
	}

	public void setPreSelectDistrict(String preSelectDistrict) {
		this.preSelectDistrict = preSelectDistrict;
	}

	public String getConfirmCkBox1() {
		return confirmCkBox1;
	}

	public void setConfirmCkBox1(String confirmCkBox1) {
		this.confirmCkBox1 = confirmCkBox1;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String getSummaryType() {
		return AppSummary.DAY_TYPE;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(ArrayList<String> dateList) {
		this.dateList = dateList;
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(ArrayList<String> timeList) {
		this.timeList = timeList;
	}
	
	public String getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(String productTypes) {
		this.productTypes = productTypes;
	}
	
	public HashMap<String, String> getBranchCodeMap() {
		return branchCodeMap;
	}

	public void setBranchCodeMap(HashMap<String, String> branchCodeMap) {
		this.branchCodeMap = branchCodeMap;
	}
	
	public String getBQSRefNum() {
		return BQSRefNum;
	}

	public void setBQSRefNum(String BQSRefNum) {
		this.BQSRefNum = BQSRefNum;
	}
	
}

