<%@ page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="header-menu.jsp" />

<script>
	function onClick(url, title) {
		//alert(url);
		loadURL(url, window.frames[0]);
		$("#panelCenter").panel("setTitle", title);
		$("#panelCenter").panel({
			iconCls : node.iconCls
		});
	}
	
	 function currencyrates() {
		  //alert("Start 1");
		  $.ajax({
			url: 'currencyrates/get-currency-rates.json', 
			data : "",
			type : 'GET',
			dataType : 'json',
		    success: function(data) {
		      if(data){
		    	  	var row = "";
					$.each(data, function(index, val) {
						row = row + "<font color='#009900'> " + val.currencycode + "</font>:<font color='#FF0000'>" +  val.rate + "</font> &nbsp";
					});
		    	    document.getElementById("marqueCurrency").innerHTML = "<Strong>" + row + "</Strong>";
		      }
		      	
		    } 
		  });
		};
		
		$( document ).ready(function() {
			currencyrates();
		});
		
		
</script>

<style>
.marquee {
  width: 300px;
  overflow: hidden;
  border: 1px solid #ccc;
  background: #ccc;
}
</style>


<div region="north" class="top-gradient gradient"
	style="height: 50px; vertical-align: middle; text-align: right; background-color: #c6d5ed;"
	border="false">

<table border="0" style="height:50px; width:100%;">
	<tr>
		<td style="width:400px;"><marquee onMouseover="this.scrollAmount=0" onMouseout="this.scrollAmount=5" id="marqueCurrency"></marquee></td>
		
		<td style="width:400px;">
				<div style="float: right; padding-top: 4px;">
					<sec:authentication property="principal.username" />
					<a class="easyui-linkbutton" iconCls="icon-user-loggedout"
						plain="true" href="<spring:url value="/back/bo/auth/logout"/>">
						Sign Out</a> <a class="easyui-linkbutton" iconCls="icon-password-change"
						plain="true" href="javascript:void(0)"
						onclick="onClick('/back/bo/auth/password_change_page','Password Change')">
						Password Change</a>
				</div>

		</td>
		
		
	</tr>

</table>	
	
	

	
</div>


<div region="west" split="true" style="width: 200px;" title="Menu">
	<!--     <div class="easyui-accordion" fit="true" border="false" style="background: none repeat scroll 0 0 #E0ECFF"> -->
	<!--         <div title="Menu" iconCls="icon-menu" -->
	<!--              style="overflow:auto;background: none repeat scroll 0 0 white;"> -->
	<!--              			<li> -->
	<!--             				 <a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/dashboard','Dashboard')"> Dashboard -->
	<!-- 		    				 </a> -->
	<!-- 		    			</li> -->




	<div id='cssmenu'>
		<ul>
			<c:forEach var="items" items="${permissionsList}" varStatus="theCount">
				

				<c:if test="${empty items.parentId && theCount.index == '0'}">
					
					<li><a href="#" plain="true"><span>${items.displayName}</span></a>
						<ul>

				</c:if>
				<c:if test="${(empty items.parentId) && (theCount.index > '0')}">
						</ul>
						</li>
					<li><a href="#" plain="true" ><span>${items.displayName}</span></a>
						<ul>

				</c:if>
				<c:if test="${not empty items.parentId}">

					<li><a href="javascript:void(0)" plain="true"
						onclick="onClick('${items.url}','${items.headerName}')">${items.displayName}</a></li>
				</c:if>
			</c:forEach>
			
				</ul>
			</li>
<!-- 											   <li><a href='#'><span>User Management</span></a> -->
<!-- 											      <ul> -->
<!-- 													<li><a href="javascript:void(0)" plain="true" onclick="onClick('/back/bo/role/list','User Role')"> User Role</a></li>		    			 -->
<!-- 						    						<li><a href="javascript:void(0)" plain="true" onclick="onClick('/back/bo/user/list','User')"> User</a></li> -->
<!-- 											      </ul> -->
<!-- 											   </li> -->
			
		</ul>
		
		






<!-- 											<ul> -->
<!-- 											    <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/dashboard','Dashboard')"><span>Dashboard</span></a></li> -->

<!-- 											   <li><a href='http://google.com'><span>Mater Information</span></a> -->
<!-- 											      <ul>	      	 -->
<!-- 											      	 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/currency/list','Currency')"> Currency</a></li> -->
<!-- 							            			 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/country/list-sendreceive-country','Currency')"> Send Receive Country</a></li>  -->
<!-- 											      	 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/bank/list','Bank')"> Bank</a></li> -->
<!-- 													 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/bankcharges/list','Bank Charges')"> Bank Charges</a></li> -->
<!-- 											      </ul> -->
<!-- 											   </li> -->


<!-- 											   <li><a href='#'><span>User Management</span></a> -->
<!-- 											      <ul> -->
<!-- 													<li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/role/list','User Role')"> User Role</a></li>		    			 -->
<!-- 						    						<li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/user/list','User')"> User</a></li> -->
<!-- 											      </ul> -->
<!-- 											   </li> -->

<!-- 											    <li><a href='#'><span>Cash Card Management</span></a> -->
<!-- 											      <ul> -->
<!-- 													 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/card/generate_page','Topup')"> Card Generate</a></li> -->
<!-- 									    			 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/card/sendemail_page','Topup')"> Card Email</a></li> -->
<!-- 									    			 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/card/activate_page','Topup')"> Card Activate</a></li> -->

<!-- 											      </ul> -->
<!-- 											   </li> -->

<!-- 											   <li><a href='#'><span>Merchant Management</span></a> -->
<!-- 											      <ul> -->
<!-- 													 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchant/list','Merchant')"> Merchant</a></li> -->
<!-- 						    						 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchant/topup-page','Topup')"> Merchant Topup</a></li> -->
<!-- 						    						 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchant/commission-add','Topup')"> Merchant Commission</a></li> -->
<!-- 						    						 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchant/list-merchant-commission','Merchant Commission')"> Merchant Commission History</a></li> -->
<!-- 									    		</ul> -->
<!-- 											   </li> -->

<!-- 											    <li><a href='#'><span>Report Center</span></a> -->
<!-- 											      <ul> -->
<!-- 													 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/apptransaction/transaction-search-page','Merchant')"> Transaction Search</a></li> -->
<!-- 						    						 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchanttransaction/merchant-topup-report','Topup')"> Merchant Topup Report</a></li> -->
<!-- 						    						 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchanttransaction/merchant-cash-book','Topup')"> Merchant Cash book</a></li> -->
<!-- 						    						 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchanttransaction/merchant-topup-summary-report','Merchant Commission')"> Merchant Toup Summary</a></li> -->
<!-- 									    			 <li><a href="javascript:void(0)" plain="true" onclick="onClick('merchanttransaction/merchant-income-summary-report','Merchant Balances')"> Merchant Income Summary</a></li> -->
<!-- 											     	 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/merchant/balance','Merchant Balances')"> Merchant Balances</a></li> -->
<!-- 											     	 <li><a href="javascript:void(0)" plain="true" onclick="onClick('merchanttransaction/merchant-profit-summary-report','Merchant Profit Summary')"> Merchant Profit Summary</a></li> -->
<!-- 											      </ul> -->
<!-- 											   </li> -->

<!-- 											   <li><a href='#'><span>Control Panel</span></a> -->
<!-- 											      <ul> -->
<!-- 													 <li><a href="javascript:void(0)" plain="true" onclick="onClick('/bo/bo/system/system-status','User')"> System Status</a></li> -->

<!-- 											      </ul> -->
<!-- 											   </li> -->


<!-- 											</ul> -->
<!-- 	</div> -->



	<!--         </div> -->
	<!--         <div title="Settings" iconCls="icon-settings" -->
	<!--              style="overflow:auto;background: none repeat scroll 0 0 white"> -->

	<!--         </div> -->
	    </div>


</div>


<div id="panelCenter" region="center" title="Dashboard"
	class="easyui-layout iframe-container" iconCls="icon-dashboard">
	<div region="center" border="false" fit="true">
		<iframe id="frmScreen"
			<%--                 src="<c:url value="/bo/dashboard	" />" --%>
                src=""
			style="width: 100%; height: 100%" frameborder="0" scrolling="auto">
		</iframe>
	</div>
</div>

<jsp:include page="footer.jsp">
	<jsp:param name="noCopyright" value="true" />
</jsp:include>