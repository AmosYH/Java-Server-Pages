<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${parameter.language}" />  
<fmt:setBundle basename="resource.language.Language_common" var="bundleLan"/>

<!DOCTYPE html>
<html lang="en">
	<head>
		<%@include file="./common/metadata.jsp"%>		
		<script src="/js/eform/bkg.js?20210317"></script>
		<script src="/js/eform/component.js"></script>
		<style>
			@media only screen and (max-width: 400px){			
				.font-adjust{
					font-size:10px;
				}
				.col-form-label {
					padding-left: 15px !important;
				}
			}
			
			.eFormContainer input.bkg-control[type="text"], .eFormContainer select.bkg-control {
				border-width: 1px !important;
				border-style: solid !important;
				border-color: #CCCCCC !important;
				height: calc(1.5em + 0.75rem + 2px) !important;
				border-radius: .25rem !important;
				padding: .375rem .75rem !important;
			}
			
			.col-form-label {
				padding-left: 15px !important;
			}
			
			.holiday span{
				color: red !important;
				font-weight:900 !important;
			}

			<c:if test="${parameter.desktop}">
				.col-form-label {
					padding-left: 2rem !important;
				}
			</c:if>

			<c:if test="${parameter.app}">
				.col-xs-8.text-center.title{
					padding-bottom: 0px !important;
				}
				#cardHeaderDefault{
					display: block !important;
				}
				.cardCarousel.row.visible-xs{
					padding-top: 0px !important;
				}
				
				.app.mobile.iphoneX .mainContainer{
					padding-top: 100px !important;
				}
				
				.app.mobile .mainContainer{
					padding-top: 70px !important;
				}
				
				.footer-padding.fromApp{
					height: 50px;
				}
				
				.row.full-width.btnfullWidth.eabtn-row{
					position: fixed;
					bottom: 0;
					left: 0;
				}
				
				.fix-top-bar2{
					display: -webkit-flex;
					position: fixed;
					width: 100%;
					z-index: 9999;
					top: 0;
					align-items: center;
					display: flex;
					flex-direction: row;
					-webkit-align-items: center;
					padding-top: env(safe-area-inset-top);
				}
				
			</c:if>
		</style>
		
		<script>
		$(function(){
			$(".header-bar > div > a > span").click(function(){
				eaApp.popPage();
			});
			
			/* if('${parameter.step}' == '6'){
				$('.scrollTop').attr('style','bottom: 10px !important;');
			} */
		});
		</script>
	</head>
	<body class="${parameter.language}">
		<input name="serverBit" id="serverBit" type="hidden" value="${parameter.serverBit}">
		<input name="formId" id="formId" type="hidden" value="${parameter.type}">
		<div class="outer inner outer-grid-1-2" >
			<!-- Rainbow background -->
			<c:if test="${parameter.desktop}">
				<div class="rainbowBg"></div>
			</c:if>
			
			<c:if test="${!parameter.app}">
				<div id="header"></div>
			</c:if>
			
			<!-- Main Content -->
			<div id="master" class="mainContainer eFormContainer">
				<!-- Header -->
				<div id="cardHeaderDefault" class="container-fluid custom-content" style="display:none;">
				<c:choose>
					<c:when test="${!parameter.app}">	
					<div class="row paddingTop20 custom-bg">
					</c:when>
					<c:otherwise>
					<div class="row header-bar fix-top-bar2">
					</c:otherwise> 
				</c:choose>
						<div class="col-xs-2">
						<c:if test="${parameter.app}">
							<a href="#" class="pointer">
								<span class="header-color-black"></span>
							</a>
						</c:if>
						</div>
						<div class="col-xs-8 text-center title">
							<span class="header-color-black f16"><fmt:message key="servlet.application.title" bundle="${bundleLan}" /></span>
						</div>
					</div>
					
					
					<!-- Preload -->
				</div>

				<div class="container no-padding">
					<jsp:include page="./step-${parameter.step}.jsp"/>				
					<a class="scrollTop" href="javascript:void(0);"><fmt:message key="common.BackToTop" bundle="${bundleLan}" /></a>
				</div>
			</div> <!-- end Main content -->
		</div> <!-- end outer inner -->
		<!-- </form> --> <!--end form -->

		<form autocomplete="off" id="editForm" name="editForm" method="post" target="_self">
			<input name="edit" id="edit" type="hidden" value="" />	
			<input name="type" id="type" type="hidden" value="${parameter.type}">
		</form>

		<script>
			if('${parameter.app}' == 'true'){
				eaApp.setIsFromApp();	
			}
			
			$(document).ready($(function ($) {        
				if('${!parameter.app}' == 'true'){
					
					$(document).ready(function() { 
						if('${parameter.type}' == 'u07'){
							$('#header').load('/header_footer/${parameter.language}/header_gold.shtml'); 
						}
						else {
							$('#header').load('/header_footer/${parameter.language}/header.shtml');	
								
						}
						$('#footer').load('/header_footer/${parameter.language}/footer.shtml'); 
					});
				}
				
				if((!isIphoneXSMax() && !isIphoneX()) && '${parameter.app}' == 'true'){
					$('.row.header-bar.fix-top-bar2').attr('class', 'row header-bar fix-top-bar');
				}

				/* $('.selectpicker').selectpicker({
					style: '',
					size: 4
				}); */
				
				function isIOS(){
					return (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent));
				}
				
				function isIphoneXSMax(){
					return isIOS() && window.screen.width == 414 && window.screen.height == 896;
				}
				
				function isIphoneX(){
					return isIOS() && window.screen.width == 375 && window.screen.height == 812;
				}
				
			}));
			
			//Overriding change lang function
			sitemap_changeLang = function(fromLangId, toLangId){
				if(document.location.search.indexOf('language=' + fromLangId) == -1){
					document.location.search = document.location.search + "&language=" + toLangId;
				}else{
					document.location.search = document.location.search.replace('language=' + fromLangId,'language=' + toLangId);
				}
			}
		</script>
		<link rel="stylesheet" href="../../css/footer.css">
		<script src="/js/eform/app/changeTheme.js"></script>
		<script src="/js/eform/app/postScript.js"></script>
		<div id="footer"></div>
	</body>
</html>