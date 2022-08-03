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


				
		<!-- CollapsePanel -->
		<div class="eaPanel">
		
			<div class="container-fluid">
				<div style="margin:0px; padding:0px;"> 
				<!-- Sub Title -->
				<br>
				<h1 style="padding-left:0px;"><fmt:message key="mpf.accInfoInOriginalScheme" bundle="${bundleMpf}" /></h1>
					<!-- Name of Original Trustee-->
					<div class="input-group-item" style="${((data.formID=="m09")&&(data.oriAccType=="A"))?'display:none;':''}">
						<div class="input-item">
							<div class="panelLabel"><label for="originalTrustee"><fmt:message key="mpf.originalTrustee" bundle="${bundleMpf}" /></label></div>
							<div class="relative custom-select">
								<span class="caret"></span>
								<select type="select" size="1" name="originalTrustee" id="originalTrustee" onchange="removeMessage('originalTrustee_error');matchSchemePosition();clearSchemePosition();">
									<option value="" class="placeholder" ${data.originalTrustee  == ""?"selected":""}><fmt:message key="mpf.trustee_placeholder" bundle="${bundleMpf}"/></option>
									<c:forEach items="${trusteeList}" var="list" varStatus="loop">
									<c:set value="${list.key}" var="listKey"/>
									<c:set value="${list.value}" var="listVal"/>
									<option value="${listKey}" class="" ${data.originalTrustee  == listKey? "selected":""}>${listVal}</option>
									</c:forEach>
								</select>
							</div>
							<div id="originalTrustee_error">
								<div id="originalTrustee_error1" class="hiddenTxt"><fmt:message key="mpf.originalTrustee_error" bundle="${bundleMpf}" /></div>
							</div>
						</div>
					</div>		
					
					<!-- Name of Original Scheme-->
					<div class="input-group-item" >
						<div class="input-item">
							<div class="panelLabel"><label for="originalScheme">
							<c:choose>
								<c:when test="${data.formID=='m09'}">
									<fmt:message key="mpf.originalSchemem09" bundle="${bundleMpf}" />
								</c:when>
								<c:otherwise>
									<fmt:message key="mpf.originalScheme" bundle="${bundleMpf}" />
								</c:otherwise>
							</c:choose>
							</label></div>
							<div class="relative custom-select">
								<span class="caret"></span>
								<select type="select" size="1" name="originalScheme" id="originalScheme" onchange="removeMessage('originalScheme_error');">
									<option value="" class="placeholder" ${data.originalScheme  == ""?"selected":""}><fmt:message key="mpf.scheme_placeholder" bundle="${bundleMpf}"/></option>
									<c:forEach items="${schemeList}" var="list" varStatus="loop">
									<c:set value="${list.key}" var="listKey"/>
									<c:set value="${list.value}" var="listVal"/>
									<option value="${listKey}" class="" ${data.originalScheme  == listKey? "selected":""}>${listVal}</option>
									</c:forEach>
								</select>
							</div>
							<div id="originalScheme_error">
								<div id="originalScheme_error1" class="hiddenTxt"><fmt:message key="mpf.originalScheme_error" bundle="${bundleMpf}" /></div>
							</div>
						</div>
					</div>
					
					
					<!-- Type of MPF Account-->
					<div class="input-group-item" style="${((data.formID=="m09")&&(data.oriAccType=="A"))?'display:none;':''}">
						<div class="panelLabel"><label for="accType_opt"><fmt:message key="mpf.accType" bundle="${bundleMpf}" /></label></div>
						<div class="row">
							<div class="col-sm-6 col-xs-12">
								<div class="radio">
									<input type="radio" onchange="removeMessage('personal_error');" name="accType_opt" id="personal" ${data.accType_opt == "personal"?"checked":""} value="personal" ${(data.oriAccType=="A"&&data.formID=="m08")?'checked':''}/>
									<label for="personal">&nbsp;<fmt:message key="mpf.personal" bundle="${bundleMpf}" /><sup style="font-size:55%;"><fmt:message key="mpf.personal.note4" bundle="${bundleMpf}" /></sup></label>
								</div>
							</div>
							<div class="col-sm-6 col-xs-12">
								<div class="radio">
									<input type="radio" onchange="removeMessage('personal_error');" name="accType_opt" id="contribution" ${data.accType_opt == "contribution"?"checked":""} value="contribution" ${(data.oriAccType=="B"||data.oriAccType=="C")?'checked':''}/>
									<label for="contribution">&nbsp;<fmt:message key="mpf.contribution" bundle="${bundleMpf}" /><sup style="font-size:55%;"><fmt:message key="mpf.contribution.note5" bundle="${bundleMpf}" /></sup></label>
								</div>
							</div>
						</div>
						<div id="personal_error">
							<div id="personal_error1" class="hiddenTxt"><fmt:message key="mpf.accType_error" bundle="${bundleMpf}" /></div>
						</div>
					</div>
					
					<!-- Scheme's Member Account Number -->
					<div class="input-group-item">
						<div class="input-item">
							<div class="panelLabel">
								<label for="oriSchemeMemberAccNo">
								<c:choose>
									<c:when test="${data.formID=='m09'}">
										<fmt:message key="mpf.oriSchemeMemberAccNom09" bundle="${bundleMpf}" />
									</c:when>
									<c:otherwise>
										<fmt:message key="mpf.oriSchemeMemberAccNo" bundle="${bundleMpf}" />
									</c:otherwise>
								</c:choose>								
								</label>
							</div>
							<fmt:message key="mpf.oriSchemeMemberAccNo_placeholder" bundle="${bundleMpf}" var="first_name_placeholder"/>
							<input name="oriSchemeMemberAccNo" type="text" id="oriSchemeMemberAccNo" placeholder="<fmt:message key="mpf.oriSchemeMemberAccNo_placeholder" bundle="${bundleMpf}" />" size="30" maxlength="30" onkeydown="removeMessage('oriSchemeMemberAccNo_error');" value ="${data.oriSchemeMemberAccNo}"
								data-trigger="focus" 
								data-placement="top" 
								data-toggle="popover" 
								data-content='${namePopover}'
								data-html="true" />
							<div id="oriSchemeMemberAccNo_error">
								<div id="oriSchemeMemberAccNo_error1" class="hiddenTxt"><fmt:message key="mpf.oriSchemeMemberAccNo_error1" bundle="${bundleMpf}" /></div>
								<div id="oriSchemeMemberAccNo_error2" class="hiddenTxt"><fmt:message key="mpf.oriSchemeMemberAccNo_error2" bundle="${bundleMpf}" /></div>
							</div>
						</div>	
					</div>
					
					<!-- Name of former employer -->
					<div class="input-group-item" style="${((data.formID=="m08")&&(data.oriAccType=="C"))?'':'display:none;'}">
						<div class="input-item">
							<div class="panelLabel">
								<label for="oriEmployerName"><fmt:message key="mpf.oriDetailOfEmployerName" bundle="${bundleMpf}" /></label>
							</div>
							<div class="panelLabel">
								<label for="oriEmployerName"><fmt:message key="mpf.oriEmployerName" bundle="${bundleMpf}" /></label>
							</div>
							<fmt:message key="mpf.oriEmployerName_placeholder" bundle="${bundleMpf}" var="first_name_placeholder"/>
							<input name="oriEmployerName" type="text" id="oriEmployerName" placeholder="${first_name_placeholder}" size="13" maxlength="13" onkeydown="removeMessage('oriEmployerName_error');" value ="${data.oriEmployerName}"
								data-trigger="focus" 
								data-placement="top" 
								data-toggle="popover" 
								data-content='${namePopover}'
								data-html="true" />
							<div id="oriEmployerName_error">
								<div id="oriEmployerName_error1" class="hiddenTxt"><fmt:message key="cardsLoans.please_enter" bundle="${bundleCommon}" /></div>
								<div id="oriEmployerName_error2" class="hiddenTxt"><fmt:message key="common.input_incorrect" bundle="${bundleCommon}" /></div>
							</div>
						</div>	
					</div>
					
					<!-- Employer’s Identification Number  -->
					<div class="input-group-item" style="${(((data.formID=="m09")&&(data.oriAccType=="A"))||((data.formID=="m08")&&(data.oriAccType=="C")))?'':'display:none;'}">
						<div class="input-item">
							<div class="panelLabel">
								<label for="oriEmployerIdNo">
								<c:choose>
									<c:when test="${data.formID=='m09'}">
										<fmt:message key="mpf.oriEmployerIdNom09" bundle="${bundleMpf}" />
									</c:when>
									<c:otherwise>
										<fmt:message key="mpf.oriEmployerIdNo" bundle="${bundleMpf}" />
									</c:otherwise>
								</c:choose>						
								</label>
							</div>
							<fmt:message key="mpf.oriEmployerIdNo_placeholder" bundle="${bundleMpf}" var="first_name_placeholder"/>
							<input name="oriEmployerIdNo" type="text" id="oriEmployerIdNo" placeholder="${first_name_placeholder}" size="13" maxlength="13" onkeydown="removeMessage('oriEmployerIdNo_error');" value ="${data.oriEmployerIdNo}"
								data-trigger="focus" 
								data-placement="top" 
								data-toggle="popover" 
								data-content='${namePopover}'
								data-html="true" />
							<div id="oriEmployerIdNo_error">
								<div id="oriEmployerIdNo_error1" class="hiddenTxt"><fmt:message key="cardsLoans.please_enter" bundle="${bundleCommon}" /></div>
								<div id="oriEmployerIdNo_error2" class="hiddenTxt"><fmt:message key="mpf.oriEmployerIdNo_error2" bundle="${bundleMpf}" /></div>
							</div>
						</div>	
					</div>
					
					<!-- Reason of transfer-->				
					<div class="input-group-item" style="${((data.formID=="m08")&&(data.oriAccType=="B"))?'':'display:none;'}">
						<div class="panelLabel"><label for="reasonOfTransfer"><fmt:message key="mpf.reasonOfTransfer" bundle="${bundleMpf}" /></label></div>
						<div class="row">
							<div class="col-sm-12 col-xs-12">
								<div class="radio">
									<input type="radio" onchange="removeMessage('cessation_error');" onclick="show('DateA');hide('DateB');" name="reasonOfTransfer" id="cessation" ${data.reasonOfTransfer == "cessation"?"checked":""} value="cessation" />
									<label for="cessation">&nbsp;<fmt:message key="mpf.cessation" bundle="${bundleMpf}" /></label>
								</div>
								<!-- Date -->
								<div id="DateA" style="display:none;">
								<fmt:message key="mpf.reasonOfTransferDate2" bundle="${bundleMpf}" var="dobPlaceholder"/>
								<input name="reasonOfTransferDate" type="text" id="reasonOfTransferDate"  onfocus="blur();" class="mobileLongField" placeholder="${dobPlaceholder}" value="${data.reasonOfTransferDate}" size="35" style="text-transform: uppercase" maxlength="20" onchange="removeMessage('reasonOfTransferDate_error')"
									data-trigger="focus" 
									data-placement="top" 
									data-toggle="popover" 
									data-content='${dobPopover}' 
									data-html="true"	
								/>
								<div id="reasonOfTransferDate_error" class="hiddenTxt">
									<div id="reasonOfTransferDate_error1" class="hiddenTxt"><fmt:message key="mpf.reasonOfTransferDate2" bundle="${bundleMpf}" /></div>
									<div id="reasonOfTransferDate_error2" class="hiddenTxt"><fmt:message key="mpf.input_incorrect" bundle="${bundleMpf}" /></div>
									<div id="reasonOfTransferDate_error3" class="hiddenTxt"><fmt:message key="mpf.birthday_invaildDate" bundle="${bundleMpf}" /></div>
								</div>  
							</div>
						</div>
							
						</div>
						<div class="row">
							<div class="col-sm-12 col-xs-12">
								<div class="radio">
									<input type="radio" onchange="removeMessage('cessation_error');" onclick="show('DateB');hide('DateA');show('openDivRemarkBoxB');" name="reasonOfTransfer" id="remain" ${data.reasonOfTransfer == "remain"?"checked":""} value="remain" />
									<label for="remain">&nbsp;<fmt:message key="mpf.remain" bundle="${bundleMpf}" /></label>
								</div>
								<!-- Date -->		
								
								<div id="DateB" style="display:none;">
									<fmt:message key="mpf.reasonOfTransferDate2" bundle="${bundleMpf}" var="dobPlaceholder"/>
									<div class="panelLabel"><label><fmt:message key="mpf.reasonOfTransferDate" bundle="${bundleMpf}" /></label></div>
									<input name="reasonOfTransferDate2" type="text" id="reasonOfTransferDate2"  onfocus="blur();" class="mobileLongField" placeholder="${dobPlaceholder}" value="${data.reasonOfTransferDate2}" size="35" style="text-transform: uppercase" maxlength="20" onchange="removeMessage('reasonOfTransferDate2_error')"
										data-trigger="focus" 
										data-placement="top" 
										data-toggle="popover" 
										data-content='${dobPopover}' 
										data-html="true"	
									/>
									<div id="reasonOfTransferDate2_error" class="hiddenTxt">
										<div id="reasonOfTransferDate2_error1" class="hiddenTxt"><fmt:message key="mpf.reasonOfTransferDate2" bundle="${bundleMpf}" /></div>
										<div id="reasonOfTransferDate2_error2" class="hiddenTxt"><fmt:message key="mpf.input_incorrect" bundle="${bundleMpf}" /></div>
										<div id="reasonOfTransferDate2_error3" class="hiddenTxt"><fmt:message key="mpf.birthday_invaildDate" bundle="${bundleMpf}" /></div>
									</div>  
								</div>	
							</div>
						</div>
						<div id="cessation_error">
							<div id="cessation_error1" class="hiddenTxt"><fmt:message key="mpf.accType_error" bundle="${bundleMpf}" /></div>
						</div>
					</div>
						
													
					<div style="${(data.formID=="m08")?'':'display:none;'}">
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM08Note1" bundle="${bundleMpf}" /></p>
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM08Note2" bundle="${bundleMpf}" /></p>
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM08Note3" bundle="${bundleMpf}" /></p>
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM08Note4" bundle="${bundleMpf}" /></p>
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM08Note5" bundle="${bundleMpf}" /></p>
					</div>
					
					<div style="${(data.formID=="m09")?'':'display:none;'}">
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM09Note1" bundle="${bundleMpf}" /></p>
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM09Note2" bundle="${bundleMpf}" /></p>
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM09Note3" bundle="${bundleMpf}" /></p>
					</div>
					
					<div style="${((data.formID=="m08")&&(data.oriAccType=="C"))?'':'display:none;'}">
						<p style="padding: 0px;"><fmt:message key="mpf.remarksM08Note6" bundle="${bundleMpf}" /></p>
					</div>
				</div>
			</div>
		</div>
		
<script>
    $(document).ready($(function ($) {        
        $('.selectpicker').selectpicker({
            style: '',
            size: 4
        });
    }));
	
	$(window).bind("load", function() {
		populateDatePicker();
	});
	
    function populateDatePicker() {
        $("input[name^='reasonOfTransferDate']").datepicker({
	        todayHighlight: false,
	        autoclose:true,
	        clearBtn:true,
	        format: "dd/mm/yyyy",
			language: 'en',
			startDate: '01/01/1900',
        });		
 	}
</script>