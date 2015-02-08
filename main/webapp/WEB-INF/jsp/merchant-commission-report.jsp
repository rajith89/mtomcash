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
		$('#btnsearch').attr("disabled", true);
		var dateFrom = document.getElementById("dateFrom").value;
		var dateTo = document.getElementById("dateTo").value;

		var str = $("#frmEdit").serialize();

		$
				.ajax({
					//url : "/bo/bo/merchanttransaction/merchantcommissionsummarylist.json-"+ dateFrom + "-" + dateTo,
					url : "merchantcommissionlist.json-"+ dateFrom + "-" + dateTo,
					
					data : str,
					type : 'GET',
					dataType : 'json',
					success : function(data) {
						//alert(data);
						
						var count = 0;
						var totalAmount = 0;
						var currencyCode = "";

						$.each(data, function(index, val) {
							//alert(val.transactionPin);
							
							var row = "";
							if (count % 2 === 0) {
							
								row += "<tr>";
							
							}else{
							
								row += "<tr bgcolor='#F0F0F0'>";
							}
							row += "<td>" + val.merchant + "</td>";
							row += "<td>" + val.date + "</td>";
							row += "<td>" + val.commission + " " + val.currency + "</td>";

							$("#transactionGrid tr:last").after(row);
							totalAmount += val.commission;
							currencyCode = val.currency;
							count++;
						});
						
						var tot = "<tr style='font-weight: bold' bgcolor='#C1DAFF'>";
						tot += "<td>TOTAL</td>";
						tot += "<td></td>";
						tot += "<td>"+ totalAmount + " " + currencyCode + "</td>";
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
	modelAttribute="merchantTransaction" style="width: 960px">
	<table>
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
				<div class="form-actions">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="button" class="btn btn-primary" onclick="onSearch()" id="btnsearch">Search</button>
						<button type="button" class="btn btn-primary" onclick="PrintDoc()">Print</button>
					</div>
				</div>
			</td>
			<td></td>
		</tr>
	</table>

	<br />
	<br />
	<br />
	<br />

	<table id="transactionGrid" width="900" border="0">
		<thead>
			<tr bgcolor='#C1DAFF'>
				<th>Merchant</th>
				<th>Date</th>
				<th>Total Commission</th>
			</tr>
		</thead>
	</table>

</form:form>