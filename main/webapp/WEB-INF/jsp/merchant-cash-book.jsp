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
	    var popupWin = window.open('', '_blank', 'width=650,height=	650,location=no,left=200px');
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
		//alert("Search button");

		var merchant = document.getElementById("merchantId").value;

		var str = $("#frmEdit").serialize();

		$.ajax({
			url : "merchantcashbook.json-" + merchant,
// 			url : "/bo/bo/merchanttransaction/merchantcashbook.json-" + merchant,
			data : str,
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				//alert(data);

				
				var count = 0;
				$.each(data, function(index, val) {
					//alert(val.transactionPin);

					var row = "";
					if (count % 2 === 0) {
						
						row += "<tr>";
					
					}else{
					
						row += "<tr bgcolor='#F0F0F0'>";
					}
					
					if (val.crdr==="CR") {
						row += "<td>" + val.description + "</td>";
						row += "<td>" + val.dateCreated + "</td>";
						row += "<td>" + val.trnxAmount + "</td>";
						row += "<td>" + " - " + "</td>";
						row += "<td>" + val.balance + "</td>";
					} else {
						row += "<td>" + val.description + "</td>";
						row += "<td>" + val.dateCreated + "</td>";
						row += "<td>" + " - " + "</td>";
						row += "<td>" + val.trnxAmount + "</td>";
						row += "<td>" + val.balance + "</td>";
					}

					$("#transactionGrid tr:last").after(row);
					count++;
				});
			}
		});
		
		$('#btnsearch').removeAttr("disabled");
	}
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

	<div class="form-actions">
		<div class="col-sm-offset-2 col-sm-10">
			<button type="button" class="btn btn-primary" onclick="onSearch()" id="btnsearch">Search</button>
			<button type="button" class="btn btn-primary" onclick="PrintDoc()">Print</button>
		</div>
	</div>

	<br />
	<br />
	<br />
	<br />

	<table id="transactionGrid" width="1000" border="0">
		<thead>
			<tr bgcolor='#C1DAFF'>
				<th>Description</th>
				<th>Date</th>
				<th width="80">CR</th>
				<th width="80">DR</th>
				<th width="80">Balance
				</td>
			</tr>
		</thead>
		<tbody class="table-striped"></tbody>
	</table>

</form:form>