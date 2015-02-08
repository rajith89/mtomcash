<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>  
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>


<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Message Detail Report"/>
</jsp:include>

<script type="text/javascript">

function onCancel() {
    loadURL('history');
}

</script>
<body>												   


<form id="frmEdit" method="post">

<table border="0" width="450px" >
	<tr>
		<td>
			<table border="0" width="100%">
			
				<tr>
					<td colspan="2"><h3>Transaction Details</h3></td>
				</tr>
				
				<tr>
					<td bgcolor="#ADD6FF"><strong>Transaction PIN</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.transactionPin}"></c:out></td>
				</tr>

				<tr>
					<td><strong>Bank Amount</strong></td>
					<td><c:out value="${appTransaction.trnxAmount} ${appTransaction.trnxCurrency}"></c:out></td>
				</tr>	
				
				<tr>
					<td bgcolor="#ADD6FF"><strong>Transaction Date</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.datePerformed}"></c:out></td>
				</tr>	
				
				<tr>
					<td><strong>Bank Charge</strong></td>
					<td><c:out value="${appTransaction.bankCharge}"></c:out></td>
				</tr>
				
				<tr>
					<td bgcolor="#ADD6FF"><strong>Merchant Commission</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.merchantComission}"></c:out></td>
				</tr>
				
				<tr>
					<td><strong>Company Commission</strong></td>
					<td><c:out value="${appTransaction.companyComission}"></c:out></td>
				</tr>
				
				<tr>
					<td bgcolor="#ADD6FF"><strong>Sender Name</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.senderName}"></c:out></td>
				</tr>
				
				<tr>
					<td><strong>Sender Country</strong></td>
					<td><c:out value="${appTransaction.senderCountry}"></c:out></td>
				</tr>
					
				<tr>	
					<td bgcolor="#ADD6FF"><strong>Sender Contact No</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.senderContactNo}"></c:out></td>
				</tr>
					
				<tr>	
					<td><strong>Beneficiary Name</strong></td>
					<td><c:out value="${appTransaction.beneficiaryName}"></c:out></td>
				</tr>
					
				<tr>	
					<td bgcolor="#ADD6FF"><strong>Beneficiary Contact No</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.beneficiaryContactNo}"></c:out></td>
				</tr>
				
				<tr>	
					<td><strong>Transaction Type</strong></td>
					<td><c:out value="${appTransaction.receivingMethod}"></c:out></td>
				</tr>
				
				<tr>	
					<td bgcolor="#ADD6FF"><strong>Account No</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.accountno}"></c:out></td>
				</tr>
				
				<tr>	
					<td><strong>Bank</strong></td>
					<td><c:out value="${appTransaction.bank}"></c:out></td>
				</tr>
				
				<tr>	
					<td bgcolor="#ADD6FF"><strong>Bank Branch</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.bankbranch}"></c:out></td>
				</tr>
				
				<tr>
					<td><strong>Merchant</strong></td>
					<td><c:out value="${appTransaction.merchantId}"></c:out></td>
				</tr>
				
				<tr>	
					<td bgcolor="#ADD6FF"><strong>Transaction Status</strong></td>
					<td bgcolor="#ADD6FF"><c:out value="${appTransaction.status}"></c:out></td>
				</tr>
				
				
			</table>
		</td>
		
		
		<td>
		</td>
	</tr>

</table>

	<div class="form-group">
		<label class="control-label">&nbsp</label>
	</div>
		<div class="form-actions">
			<button type="button" class="btn btn-primary" id="btnBack" onclick="javascript:history.go(-1)">Back</button>
			
	</div>
	
</form>

</body>