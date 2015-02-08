<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="header-form.jsp">
	<jsp:param name="title" value="Merchant" />
</jsp:include>


<script>
	function onCancel() {
		loadURL('list');
	}
	
	function PrintDoc() {
	    var toPrint = document.getElementById('transactionGrid');
	    var popupWin = window.open('', '_blank', 'width=2000,height=650,location=no,left=200px');
	    popupWin.document.open();
	    popupWin.document.write('<html><title>::Preview::</title><link rel="stylesheet" type="text/css" href="print.css" /></head><body onload="window.print()">')
	    //popupWin.document.write("<table border='1'><tr><td>Name<td><td>Age<td></tr><tr><td>Name<td><td>Age<td></tr></table>");
	    popupWin.document.write('<table>');
	    popupWin.document.write(toPrint.innerHTML);
	    popupWin.document.write('</table></html>');
	    popupWin.document.close();
	}
	
	function onSearch() {
		var table = document.getElementById("transactionGrid");
		for (var i = table.rows.length - 1; i > 0; i--) {
			table.deleteRow(i);
		}
		//alert("Search button");
		$('#btnsearch').attr("disabled", true);
		
		var pin = document.getElementById("transactionPin").value;
		var dateFrom = document.getElementById("dateFrom").value;
		var dateTo = document.getElementById("dateTo").value;
		var merchant = document.getElementById("merchantId").value;

		var str = $("#frmEdit").serialize();

		$
				.ajax({
					url : "apptransactionlist.json-" + pin + "-" + dateFrom
							+ "-" + dateTo + "-" + merchant,
					data : str,
					type : 'GET',
					dataType : 'json',
					success : function(data) {
						//alert(data);
						var count = 0;
						var totalSendAmount = 0;
						var sCurrency = "";
						var totalBenificiaryAmount = 0;
						var bCurrency = "";
						var mCommission = 0;
						var sCommission = 0;
						var cCommission = 0;
						var bCharge = 0;
						var pt = 0;
						
						var tAmount = 0;
						var tCurrency = "";
						
						$.each(data, function(index, val) {
							//alert(val.transactionPin);
							var row = "";
							//"<tr><td>" + val.transactionPin + "</td>";
							if (count % 2 === 0) {
								if(val.wsResponcecode == "000"){
									row += "<tr><td>"
										+ "<a href=transaction-view-page-" + val.id + ">"
										+ val.transactionPin
										+ "</a></td>";
								}else if(val.wsResponcecode == "401" || val.wsResponcecode == "404" || val.wsResponcecode == "405"){
									row += "<tr bgcolor='#FFFF66'><td>"
										+ "<a href=transaction-view-page-" + val.id + ">"
										+ val.transactionPin
										+ "</a></td>";												
								}else{
									row += "<tr bgcolor='#FFAD99'><td>"
										+ "<a href=transaction-view-page-" + val.id + ">"
										+ val.transactionPin
										+ "</a></td>";
								}
							} else {
								if(val.wsResponcecode == "000"){
									row += "<tr bgcolor='#F0F0F0'><td>"
										+ "<a href=transaction-view-page-" + val.id + ">"
										+ val.transactionPin
										+ "</a></td>";
										
								}else if(val.wsResponcecode == "401" || val.wsResponcecode == "404" || val.wsResponcecode == "405"){
									row += "<tr bgcolor='#FFFF66'><td>"
										+ "<a href=transaction-view-page-" + val.id + ">"
										+ val.transactionPin
										+ "</a></td>";		
										
								}else{
									row += "<tr bgcolor='#FFAD99'><td>"
										+ "<a href=transaction-view-page-" + val.id + ">"
										+ val.transactionPin
										+ "</a></td>";
								}
							}

							row += "<td>" + val.senderName	+ "</td>";
							row += "<td style='width:100px'>" + val.senderAmount + " " + val.sendCurrency + "</td>";
							row += "<td>" + val.beneficiaryName	+ "</td>";
							row += "<td style='width:100px'>" + val.beneficiaryAmount + " " + val.benificiaryCurrency + "</td>";
							row += "<td style='width:150px'>" + val.merchantId + "</td>";
							row += "<td style='width:150px'>" + val.submerchant + "</td>";
							row += "<td style='width:150px'>" + val.sendOption	+ "</td>";
							row += "<td>" + val.merchantComission + "</td>";
							row += "<td>" + val.submerchantcommission + "</td>";													
							row += "<td>" + val.companyComission + "</td>";
							row += "<td>" + val.bankCharge + "</td>";
							row += "<td>" + val.profit + "</td>";
							row += "<td style='width:150px'>" + val.receivingMethod	+ "</td>";
							row += "<td style='width:120px'>" + val.datePerformed + "</td>";
							row += "<td style='width:100px'>" + val.trnxAmount + " " + val.trnxCurrency +"</td>";
							row += "<td style='width:320px'>" + val.status + "</td>";
							
							$("#transactionGrid tr:last").after(row);
							
							if(val.wsResponcecode == "000"){
								totalSendAmount += val.senderAmount;
								sCurrency = val.sendCurrency;
								totalBenificiaryAmount += val.beneficiaryAmount;
								bCurrency = val.benificiaryCurrency;
								mCommission += val.merchantComission;
								sCommission += val.submerchantcommission;
								cCommission += val.companyComission;
								bCharge += val.bankCharge;
								pt += val.profit;
								tAmount += val.trnxAmount;
								tCurrency = val.trnxCurrency;
							}
							count++;
							//alert(totalSendAmount);
						});
						
						var tot = "<tr style='font-weight: bold' bgcolor='#C1DAFF'>";
						tot += "<td>TOTAL</td>";
						tot += "<td></td>";
						tot += "<td>" + totalSendAmount.toFixed(2) + " " + sCurrency + "</td>";
						tot += "<td></td>";
						tot += "<td>" + totalBenificiaryAmount.toFixed(2) + " " + bCurrency + "</td>";
						tot += "<td></td>";
						tot += "<td></td>";
						tot += "<td></td>";
						tot += "<td>" + mCommission.toFixed(2) + "</td>";
						tot += "<td> " + sCommission.toFixed(2) + "</td>";
						tot += "<td>"+ cCommission.toFixed(2) +"</td>";
						tot += "<td>" + bCharge.toFixed(2) + "</td>";
						tot += "<td>"+ pt.toFixed(2) +"</td>";
						tot += "<td></td>";
						tot += "<td></td>";
						tot += "<td>" + tAmount.toFixed(2) + " "+ tCurrency +"</td>";
						tot += "<td></td>";
						
						tot += "</tr>";					
						$("#transactionGrid tr:last").after(tot);
						
					}
				});
		$('#btnsearch').removeAttr("disabled");
		
	}

	$(function() {
		$('#dateFrom').datepicker({
			format : 'dd.mm.yyyy'
		});
		$('#dateTo').datepicker({
			format : 'dd.mm.yyyy'
		});
	});
</script>
<style>
input.form-control {
	width: 215px;
}

select.form-control {
	width: 450px;
}

button.btn-primary {
	width: 100px;
}
</style>
<form:form class="form-horizontal" id="frmEdit" method="post"
	modelAttribute="appTransaction">

	<table border="0" width="500">
		<tr>
			<td>
				<%
					java.text.DateFormat df = new java.text.SimpleDateFormat(
								"dd.MM.yyyy");
				%>
				<div class="form-group">
					<label class="col-sm-2 control-label">From</label>
					<div class="col-sm-10">
						<form:input path="" class="form-control" type="text"
							required="true" size="40" id="dateFrom"
							value="<%=df.format(new java.util.Date())%>" />
					</div>
				</div>
			</td>
			<td>
				<div class="form-group">
					<label class="col-sm-2 control-label">To</label>
					<div class="col-sm-10">
						<form:input path="" class="form-control" type="text"
							required="true" size="40" id="dateTo"
							value="<%=df.format(new java.util.Date())%>" />
					</div>
				</div>
			</td>
		</tr>

		<tr>
			<td>
				<div class="form-group">
					<label class="col-sm-2 control-label">Pin</label>
					<div class="col-sm-10">
						<form:input path="transactionPin" class="form-control" type="text"
							required="true" size="40" id="transactionPin" />
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="form-group">
					<label class="col-sm-2 control-label">Merchant</label>
					<div class="col-sm-10">
						<form:select path="merchantId.id" class="form-control"
							id="merchantId" onchange="onComboselect(this.text)">
							<option value="0">Select</option>
							<c:forEach var="merchants" items="${merchantList}">
								<option value="${merchants.id}"
									label="${merchants.firstName} ${merchants.lastName}" />
							</c:forEach>
						</form:select>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div class="form-actions">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="button" class="btn btn-primary" onclick="onSearch()" id="btnsearch">Search</button>
						<button type="button" class="btn btn-primary" onclick="PrintDoc()">Print</button>
					</div>
				</div>
			</td>
		</tr>

	</table>

	
	<br />
	<br />

	<table id="transactionGrid" width="2500" border="0">
		<thead>
			<tr bgcolor='#C1DAFF'>
				<th width="20">Transaction Pin</th>
				<th width="100">Sender Name</th>
				<th width="50">Send Amount</th>
				<th width="100">Beneficiary Name</th>
				<th width="50">Beneficiary Amount</th>
				<th width="100">Merchant</th>
				<th width="100">Sub Merchant</th>
				<th width="100">Option</th>
				<th width="50">Merchant Commission</th>
				<th width="50">Sub Merchant Commission</th>
				<th width="50">Company Commission</th>
				<th width="50">Bank Charge</th>
				<th width="50">Profit</th>
				<th width="100">Receiving Method</th>
				<th width="70">Date Performed</th>
				<th width="50">Bank Amount</th>
				<th width="130">Status</th>
			</tr>
		</thead>
	</table>

</form:form>
