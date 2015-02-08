<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<meta http-equiv="refresh" content="15" >

<jsp:include page="header.jsp">
    <jsp:param name="title" value="${pageTitle}"/>
</jsp:include>

<table width="100%" height="100%" border="0">
	
				<tr>
					<td width="50%" height="50%" align="left">
						<div id="p" class="easyui-panel" title="Merchants Balances" style="width:600px;height:300px;padding:1px;">
							<table id="grid" class="easyui-datagrid active-bg"
						           url="merchant/list.json" pageList="[25,50,100,200]"
						           fit="true" fitColumns="true" pagination="true"
						           data-options="singleSelect:true">
						        <thead>
						        <tr>
						            <th field="firstName" width="100">First Name</th>
						            <th field="lastName" width="100">Last Name</th>
						            <th field="telephone" width="100">Telephone</th>
						            <th field="balance" width="100">Balance</th>            
						        </tr>
						        </thead>
						    </table>
					 	</div>
					 </td>
					 
					 
					 
					<td align="left">
						<div id="p" class="easyui-panel" title="Merchants Commision rates" style="width:600px;height:300px;padding:1px;">
							<table id="grid" class="easyui-datagrid active-bg"
						           url="merchant/list-merchant-commision.json" pageList="[25,50,100,200]"
						           fit="true" fitColumns="true" pagination="true" data-options="singleSelect:true">
						        <thead>
						                
						        <tr>
						            <th field="id" hidden="true" width="100">Id</th>
						            <th field="merchant" width="200">Merchant</th>
						            <th field="amountslab" width="100">Amount SLab</th>
						            <th field="amount" width="100">Amount</th>
						        </tr>
						        </thead>
						    </table>
					 	</div>
					
					 </td>
				</tr>
				

				<tr>
					<td width="100%" height="50%" align="left" colspan="2">
						<div id="p" class="easyui-panel" title="Latest Merchants Top-ups" style="width:600px;height:300px;padding:1px;">
							<table id="grid" class="easyui-datagrid active-bg"
						           url="transaction/list-topup-transaction.json" pageList="[25,50,100,200]"
						           fit="true" fitColumns="true" pagination="true"
						           data-options="singleSelect:true">
						        <thead>
						        <tr>							        
						            <th field="merchantname" width="200">Merchant</th>
						            <th field="description" width="200">Description</th>
						            <th field= "trnxamount" width="100">Amount</th>
						            <th field="date" width="100">Date</th>            
						        </tr>
						        </thead>
						    </table>
					 	</div>
					 </td>
					 
					 
					 
<!-- 					<td align="center"> -->
						
					
<!-- 					 </td> -->
				</tr>

</table>

