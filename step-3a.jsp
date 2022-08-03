<%@page language="java" import="java.io.*,java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.EA.eform.util.Fmt"%>
<%@page import="com.EA.eform.object.MPFAppObject"%>

<c:choose>
	<c:when test="${parameter.type == 'm01'}">
		<c:set var="cache" value="${component['m01Cache']}" scope="request" />
	</c:when>
	<c:when test="${parameter.type == 'm02'}">
		<c:set var="cache" value="${component['m02Cache']}" scope="request" />
	</c:when>
	<c:when test="${parameter.type == 'm03'}">
		<c:set var="cache" value="${component['m03Cache']}" scope="request" />
	</c:when>
	<c:when test="${parameter.type == 'm04'}">
		<c:set var="cache" value="${component['m04Cache']}" scope="request" />
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${parameter.language == 'en'}">
		<c:set var="text_lang" value="text_en" scope="request" />
	</c:when>
	<c:when test="${parameter.language == 'tc'}">
		<c:set var="text_lang" value="text_tc" scope="request" />
	</c:when>
	<c:when test="${parameter.language == 'sc'}">
		<c:set var="text_lang" value="text_sc" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="text_lang" value="text_en" scope="request" />
	</c:otherwise> 
</c:choose>

<fmt:setLocale value="${parameter.language}" /> 
<fmt:setBundle basename="resource.language.Language_common" var="bundleCommon"/>
<fmt:setBundle basename="resource.language.Language_mpf" var="bundleMpf"/>
<fmt:message key="mpf.nonDisPopover" bundle="${bundleMpf}" var="nonDisPopover"/>
<fmt:message key="common.mandatoryStar" bundle="${bundleCommon}" var="star"/>
<%@page import="com.EA.eform.service.FormService"%>
<%
FormService service = (FormService)session.getAttribute("serviceSessMpf"); 
String csrfToken = service.getCSRFToken(request, session);
%>

	
	    <!-- CollapsePanel -->
	    <div class="container">
			<div class="panel-group">
				<c:if test="${parameter.type == 'm02' || parameter.type == 'm03' || parameter.type == 'm04'}">
					<div class="panel panel-default">
		                <div class="panel-heading">
		                    <h4 class="panel-title"><c:out value="${cache['mpf.paymentTypeHeader'].attribute['0'][text_lang]}"/></h4>
		                </div>
					</div>
		            <div class="panel panel-default">
		            	<div class="panel-heading" id="paymentTypeLabel">
							<p><c:out value="${cache['mpf.paymentTypeLabel'].attribute['0'][text_lang]}"/></p>
							<div id="paymentTypeLabel_error" class="input-group-item paddingLeft15 paddingRight15">
								<div id="paymentTypeLabel_error1" class="hiddenTxt"><c:out value="${cache['mpf.paymentType_error1'].attribute['0'][text_lang]}"/></div>
							</div>
		            	</div>
		            	
		                <div class="panel-heading">
		                    <h4 class="panel-title">
								<div class="radio">
									<input type="radio" name="payment" id="payment1" value="${cache['mpf.paymentType'].attribute['0'].value}" ${data.paymentType == "R"?"checked":""} onchange="removeMessage('paymentTypeLabel_error');" class="toggle-root" toggle-parent="pType1" />
									<label for="payment1"><c:out value="${cache['mpf.paymentType'].attribute['0'][text_lang]}"/></label>
								</div>
		                    </h4>
		                </div>
		                <div class="pType1 panel-heading" style="${(data.paymentType=='R')?'':'display:none;'}" toggle-child="pType1">
							<div>
								<c:choose>
									<c:when test="${parameter.type == 'm02'}">
										<h3>${cache['mpf.autopayShemeType_is'].attribute['0'][text_lang]}</h3>
										<p><c:out value="${cache['mpf.autopayShemeType_is2'].attribute['0'][text_lang]}"/></p>
										<h3>${cache['mpf.autopayMpfAcct'].attribute['0'][text_lang]}</h3>
										<p><c:out value="${cache['mpf.accNumber_IS'].attribute['0'][text_lang]}"/></p>
									</c:when>
									<c:when test="${parameter.type == 'm03'}">
										<h3>${cache['mpf.autopayShemeType_mt'].attribute['0'][text_lang]}</h3>
										<p><c:out value="${cache['mpf.autopayShemeType_mt2'].attribute['0'][text_lang]}"/></p>
										<h3>${cache['mpf.autopayMpfAcct'].attribute['0'][text_lang]}</h3>
										<p><c:out value="${cache['mpf.accNumber_MT'].attribute['0'][text_lang]}"/></p>
									</c:when>
									<c:when test="${parameter.type == 'm04'}">
										<h3>${cache['mpf.autopayShemeType_vs'].attribute['0'][text_lang]}</h3>
										<p><c:out value="${cache['mpf.autopayShemeType_vs2'].attribute['0'][text_lang]}"/></p>
										<h3>${cache['mpf.autopayMpfAcct'].attribute['0'][text_lang]}</h3>
										<p><c:out value="${cache['mpf.accNumber_VS'].attribute['0'][text_lang]}"/></p>
									</c:when>
								</c:choose>
							</div>
							<div class="paddingLeft15 paddingRight15">
								<div class="input-group-item">
									<div class="input-item">
										<div class="panelLabel">
											<label>${cache['mpf.autopayBankInfo'].attribute['0'][text_lang]}</label>
										</div>
									</div>
								</div>
								<table id="accountTable">
									<tbody>
										<tr>
											<td colspan="2">
												<div class="row">
													<c:forEach items="${cache['mpf.autopayAcctType'].attribute}" var="accType" varStatus="loop">
														<div class="col-xs-6">
															<div class="radio">
																<input type="radio" name="accType" id="accType${loop.index}" onchange="removeMessage('accType1_error');" value="${accType.value}" ${data.contributeAccType == accType.value?"checked":""}/>
																<label for="accType${loop.index}"><c:out value="${accType[text_lang]}"/></label>
															</div>
														</div>
													</c:forEach>
												</div>
												<div id="accType1_error" class="input-group-item">
													<div id="accType1_error1" class="hiddenTxt"><c:out value="${cache['mpf.accTypeLabel'].attribute['0'][text_lang]}"/></div>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<c:out value="${cache['mpf.autopayBankNo'].attribute['0'][text_lang]}"/>
											</td>
											<td style="width: 100%; line-height: 140%">
												<fmt:message key="cardsLoans.please_enter" bundle="${bundleCommon}" var="accNumber_placeholder"/>
												<input style="" name="accNumber" pattern="[0-9]*" placeholder="${accNumber_placeholder}" type="text" id="accNumber" value="${data.contributeAccNo}" onkeydown="removeMessage('accNumber_error');" size="12" maxlength="12">
											</td>
										</tr>
									</tbody>
								</table>
								<div id="accNumber_error" class="input-group-item">
									<div id="accNumber_error1" class="hiddenTxt"><c:out value="${cache['mpf.accNumber_error1'].attribute['0'][text_lang]}"/></div>
									<div id="accNumber_error2" class="hiddenTxt"><c:out value="${cache['mpf.accNumber_error2'].attribute['0'][text_lang]}"/></div>
								</div>
								<div class="input-group-item">
									<div class="input-item">
										<div class="panelLabel">
											<label for="accName"><c:out value="${cache['mpf.autopayAcctNameLabel'].attribute['0'][text_lang]}"/></label>
										</div>
										<fmt:message key="cardsLoans.please_enter" bundle="${bundleCommon}" var="accName_placeholder"/>
										<input name="accName" type="text" id="accName" placeholder="${accName_placeholder}" value="${data.contributeAccName}" onkeydown="removeMessage('accName_error');" size="40" maxlength="40"/>
									</div>
									<div id="accName_error">
										<div id="accName_error1" class="hiddenTxt"><c:out value="${cache['mpf.accName_error1'].attribute['0'][text_lang]}"/></div>
										<div id="accName_error2" class="hiddenTxt"><c:out value="${cache['mpf.accName_error2'].attribute['0'][text_lang]}"/></div>
									</div>
								</div>
							</div>
							<div class="input-group-item paddingLeft15 paddingRight15">
								<div class="input-item">
									<div class="panelLabel">
										<label><c:out value="${cache['mpf.autopayRemarks1'].attribute['0'][text_lang]}"/>&nbsp;:</label>
										<p>${cache['mpf.autopayRemarks2'].attribute['0'][text_lang]}</p>
									</div>
									<div class="panelLabel">
										<label for="contributeAmt"><c:out value="${cache['mpf.autopayAmt'].attribute['0'][text_lang]}"/>&nbsp;:&nbsp;<c:out value="${cache['common.hkdollarLabel'].attribute['0'][text_lang]}"/></label>
										<p><c:out value="${cache['mpf.autopayAmtRemark'].attribute['0'][text_lang]}"/></p>
									</div>
									<c:set var="contributeAmt_placeholder" value="${cache['common.hkdollarLabel'].attribute['0'][text_lang]}" />
									<input name="contributeAmt" type="text" pattern="[0-9]*" id="contributeAmt" placeholder="${contributeAmt_placeholder}" onkeydown="removeMessage('contributeAmt_error');" value="${data.contributeAmt}" size="6" maxlength="6"/>
									<div id="contributeAmt_error">
										<div id="contributeAmt_error1" class="hiddenTxt"><c:out value="${cache['mpf.autopayAmt_error1'].attribute['0'][text_lang]}"/></div>
										<div id="contributeAmt_error2" class="hiddenTxt"><c:out value="${cache['mpf.contributionAmt_error2'].attribute['0'][text_lang]}"/></div>
										<div id="contributeAmt_error3" class="hiddenTxt"><c:out value="${cache['mpf.contributionAmt_error1'].attribute['0'][text_lang]}"/></div>
										<div id="contributeAmt_error4" class="hiddenTxt"><c:out value="${cache['mpf.autopayAmtMaxInp'].attribute['0'][text_lang]}"/></div>
									</div>
								</div>
								<div class="input-item">
									<div class="panelLabel">
										<label for="contributeDay"><c:out value="${cache['mpf.contributionDayLabel'].attribute['0'][text_lang]}"/>&nbsp;:</label>
										<p>${cache['mpf.contributionDayLabel3'].attribute['0'][text_lang]}</p>
									</div>
									<div class="relative custom-select">
										<span class="caret"></span>
										<select type="select" size="1" name="contributeDay" id="contributeDay" onchange="removeMessage('contributeDay_error');">
										<option value="" class="placeholder"><fmt:message key="mpf.error_placeholder" bundle="${bundleMpf}" /></option>
										
										<c:forEach items="${cache['mpf.autopayDate'].attribute}" var="autopayDate">
											<c:set value="${autopayDate[text_lang]}" var="autopayDateName" />
											<c:set value="${autopayDate.value}" var="autopayDateValue" />
											<option value="${autopayDateValue}" ${data.contributeDay == autopayDateValue?"selected":""}>${autopayDateName}</option>
										</c:forEach>
										</select>
									</div>
									<div id="contributeDay_error">
										<div id="contributeDay_error1" class="hiddenTxt"><c:out value="${cache['mpf.autopayDate_error1'].attribute['0'][text_lang]}"/></div>
									</div>
								</div>
							</div>
							
							<div class="input-group-item paddingLeft15 paddingRight15 formLine">
								<div class="panelLabel">
									<label>${cache['mpf.autopayDDARemarks1'].attribute['0'][text_lang]}</label>
								</div>
							</div>
							
							<div class="paddingLeft15"> <!-- update DB -->
								${cache['mpf.autopayDDARemarks2'].attribute['0'][text_lang]}
								<br>
								<br>
								<div class="paddingLeft5 paddingRight15">
									${cache['mpf.autopayDDAChkbox1'].attribute['0'][text_lang]}
								</div>
							</div>
							<div class="paddingLeft25 paddingRight15">
								<div>
									<div class="checkbox checkbox-danger checkbox-circle">
										<input type="checkbox" name="paymentCheckBoxFor8" id="paymentCheckBoxFor8" onchange="removeMessage('paymentCheckBoxFor8_error');" ${data.contributeCkBox == "on"?"checked":""}/>
										<label for="paymentCheckBoxFor8"><c:out value="${cache['mpf.autopayDDAChkbox2'].attribute['0'][text_lang]}"/></label>
									</div>
								</div>
							</div>
							<div id="paymentCheckBoxFor8_error" class="input-group-item paddingLeft15 paddingRight15">
								<div id="paymentCheckBoxFor8_error1" class="hiddenTxt"><c:out value="${cache['mpf.stmtConfirm_error1'].attribute['0'][text_lang]}"/></div>
							</div>

	                    	<h3 class="panel-title">${cache['mpf.autopayEsignNotes1'].attribute['0'][text_lang]}</h3>
		                    	
	                		<div id='e-signature' class="paddingLeft15 paddingRight15">
								<div class="reviewContent">
									<button type="button" class="btn-change item-button button button-ios sign-open" id="DDASignature" onclick="removeMessage('DDASignature_error');sign_open('DDA-sign', 'DDASign');">
										<span class="button-inner"><c:out value="${cache['common.eSignbuttonLabel'].attribute['0'][text_lang]}"/></span>
										<div class="button-effect"></div>
									</button>
									<div class="elect-sign">
										<div class="sign-close">x</div>
										<canvas id="canvas" width="315" height="315" style="touch-action: none"></canvas>
										<div>
											<button type="button" class="button-ios sign-reset"><span class="button-inner"><c:out value="${cache['common.clearbuttonLabel'].attribute['0'][text_lang]}"/></span></button>
											<button type="button" class="button-ios sign-save"><span class="button-inner"><c:out value="${cache['common.okbuttonLabel'].attribute['0'][text_lang]}"/></span></button>
										</div>
									</div>
									<div id="DDASignature_error" class="input-group-item">
										<div id="DDASignature_error1" class="hiddenTxt"><c:out value="${cache['mpf.autopayEsign_error1'].attribute['0'][text_lang]}"/></div>
										<div id="DDASignature_error2" class="hiddenTxt"><fmt:message key="mpf.input_incorrect" bundle="${bundleMpf}" /></div>
									</div>
								</div>
									
								<div class="reviewContent text-center">
									<c:set var="DDASign-alt" value="${cache['common.eSignbuttonLabel'].attribute['0'][text_lang]}" />
									<img alt="${DDASign-alt}" id="DDA-sign" src="${data.contributeESign}" style="${(not empty data.contributeESign)?'':'display:none'}">
								</div>
								<div class="signDecaration" style="${(empty data.contributeESign)?'display:none;':''}">
									<div class="paddingLeft10">
										<div class="checkbox checkbox-danger checkbox-circle ">
											<input type="checkbox" name="DDAsignatureCheckBox" id="DDAsignatureCheckBox" onChange= "removeMessage('DDAsignatureCheckBox_error')" ${(data.contributeESignCkBox == 'on')?'checked':''}/>
											<label for="DDAsignatureCheckBox">
												<c:out value="${cache['mpf.autopayEsignNotes3'].attribute['0'][text_lang]}"/>
											</label>
										</div>
									</div>
									<div id="DDAsignatureCheckBox_error" class="input-group-item">
										<div id="DDAsignatureCheckBox_error1" class="hiddenTxt"><c:out value="${cache['mpf.stmtConfirm_error1'].attribute['0'][text_lang]}"/></div>
									</div>
								</div>
							</div>
						</div>
	            	</div> 
	            	<div class="panel panel-default">
		                <div class="panel-heading">
		                    <h4 class="panel-title">
								<div class="radio">
									<input type="radio" name="payment" id="payment2" value="${cache['mpf.paymentType'].attribute['1'].value}" ${data.paymentType == "I"?"checked":""} onchange="removeMessage('payment1_error');" class="toggle-root" toggle-parent="pType2" />
									<label for="payment2"><c:out value="${cache['mpf.paymentType'].attribute['1'][text_lang]}"/></label>
								</div>
		                    </h4>
		                </div>
						<div class="pType2 panel-heading" style="${(data.paymentType=='I')?'':'display:none;'}" toggle-child="pType2">
							<p>
							${cache['mfp.irregularPayRemark1'].attribute['0'][text_lang]}
							</p>
							${cache['mfp.irregularPayRemark2'].attribute['0'][text_lang]}
							<p>
							${cache['mfp.irregularPayRemark3'].attribute['0'][text_lang]}
							</p>
						</div>
	            	</div> 
				</c:if>
	            </div>
	            </div>
	
