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
<fmt:message key="mpf.dobPopover" bundle="${bundleMpf}" var="dobPopover"/>
<%@page import="com.EA.eform.service.FormService"%>
<%
FormService service = (FormService)session.getAttribute("serviceSessMpf"); 
String csrfToken = service.getCSRFToken(request, session);
%>

<script type="text/javascript">
if(/MSIE \d|Trident.*rv:/.test(navigator.userAgent))
{
	document.write('<script src="/js/eform/esignature_IE.js?20191112A"><\/script>');
	console.log("IE");
}
else
{
	document.write('<script src="/js/eform/esignature.js?20191112A"><\/script>');
	console.log("non-IE");
}
	
</script>

<link href="/css/esignature.css?20190716c" rel="stylesheet">
<script>
$(window).bind("load", function() {
	var stepId = '${parameter.step}';
	var error = '${parameter.error}';
	var formId = '${parameter.type}';
	var oriAccType = '${data.oriAccType}';
	if(formId == "m08"||formId == "m09"){
		$(':radio:not(:checked)').attr('disabled', true);
		$("#remain").attr('disabled', false);
		$("#cessation").attr('disabled', false);
		step_3b_validation(error,formId,oriAccType);
	}else{
		step_3a_validation(error,formId);
	}
});

$(function() {
		$("input, select").popover().keydown(function () {
            $(this).popover('hide');
        });
		
		var maxYear = 65;
		var minYear = 18;
		//populateDatePicker(minYear, maxYear);
		populateDatePickerReasonOfTransferDate();
		
	});

	
function populateDatePickerReasonOfTransferDate() {
	 $("input[name^='reasonOfTransferDate']").datepicker({
		changeMonth: true,
		changeYear: true,
		minDate: new Date(),
		maxDate: "+1m",
		defaultDate: "+0"
	});
	}
</script>
<%
String lan = request.getParameter("language");
Fmt fmt = new Fmt(lan, "Language_mpf");
Fmt commonFmt = new Fmt(lan, "Language_common");
MPFAppObject data = (MPFAppObject)request.getAttribute("data");
%>

<style>
.checkbox.checkbox-circle label::before{
    width: 24px;
    height: 24px;
    margin-left: -30px;
}

.checkbox.checkbox-circle label::after {
    left: -7px;
    top: 3px;
}


@media (max-width: 414px) {
	.panelLabel p{
		display: block !important;
		padding-left: 0px;
		padding-right: 0px;
		margin: 0px 0px 10px 0px;
	}
}

.panelLabel p{
	display: inline;
	color: #696969;
	font-family: 'OpenSans';
	font-weight: normal;
}

<c:if test="${parameter.type != 'm01'}">
	@media (max-width: 378px) {
		.creditCard > div{
		    margin-left: 22px;
		}
	}
</c:if>

#accountTable td {
    padding: 5px 0px 5px 0px;
    vertical-align: middle;
}
	
</style>

<form autocomplete="off" id="theForm" name="theForm" method="post">
	<input name="formCSRFToken" id="formCSRFToken" type="hidden" value="<%=csrfToken%>" />
	<input type="hidden" id="edit" name="edit" value=""/>
	<input type="hidden" id="action" name="action" value=""/>
	<input type="hidden" id="step" name="step" value="3"/>
	<input type="hidden" id="DDASign" name="DDASign" value="${data.contributeESign}"/>	
	
	    <!-- StepBar -->
	    <div class="text-center marginTop20 marginBottom20 creditCard">
	    <c:choose>
			<c:when test="${parameter.type == 'm01'||parameter.type == 'm08'||parameter.type == 'm09'}">
		        <div>
					<span onclick="edit(1);">1</span>
				 </div>
				 <div>
					 <span onclick="edit(2);">2</span>
				 </div>
				 <div class="active">
					 <span>3</span>
				 </div>
				 <div>
					 <span>4</span>
				 </div>
				 <div>
					 <span>5</span>
				 </div>
			</c:when>
			<c:otherwise>
				<div>
					<span onclick="edit(1);">1</span>
				 </div>
				 <div>
					 <span onclick="edit(2);">2</span>
				 </div>
				 <div class="active">
					 <span>3</span>
				 </div>
				 <div>
					 <span>4</span>
				 </div>
				 <div>
					 <span>5</span>
				 </div>
				 <div>
					 <span>6</span>
				 </div>
			</c:otherwise>
	    </c:choose>
	    </div>
		
		<c:choose>
			<c:when test="${parameter.type == 'm08' || parameter.type == 'm09'}">
				<jsp:include page="./step-${parameter.step}b.jsp"/>
			</c:when>
			<c:otherwise>
				<jsp:include page="./step-${parameter.step}a.jsp"/>
			</c:otherwise>
		</c:choose>
		
	    <div class="footer-padding"></div>
	
	    <!-- EA Btn -->
	
	    <div class="row full-width btnfullWidth eabtn-row">
			<div class="col-xs-6">
				<input id="Back" class="eaBtn btnfullWidth eaLeftBtnColor" type="button" value="<fmt:message key="common.Back" bundle="${bundleCommon}"/>" onclick="back();" />
			</div>
			<div class="col-xs-6">
				<input id="Next" class="eaBtn btnfullWidth" type="button" value="<fmt:message key="common.Next" bundle="${bundleCommon}"/>" onclick="formValidation(3,'${parameter.type}','${data.oriAccType}');" />
			</div>
	    </div>
</form>

<script>
$('#accName').on('input', function (e) {
	var $that = $(this);
	var limit = 75;
	$that.attr('maxlength', limit);
	setTimeout(function(){
		var value =  $that.val(),
		reg = /[\u4e00-\u9fa5]{1}/g,             
		notReg = /[A-Za-z 0-9.,&/(/)/-]{1}/g;
		var chinese = value.match(reg);
		var english = value.match(notReg);
		
		if(chinese)
		{
			limit = limit - (chinese.length*3);
		}
		
		if(english)
		{
			limit = limit - english.length;
		}
		
		if(limit<0)
		{
			value = value.substring(0, value.length-1);
			$that[0].value = value;
		}
	}, 0);
});

	$('#DDA-sign').on('load', function() {
		$('.signDecaration').show();
	});
</script>