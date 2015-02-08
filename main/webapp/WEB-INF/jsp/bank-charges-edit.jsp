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
	
	function showMessage(message){
		$("#message" ).text(message).show().fadeOut(3000);
	}
	
	$(function() {
		$('#frmEdit').form({
			url : 'save',
			onSubmit : function() {
				
				if( $('#corporateBanklist').val() <= 0){
					showMessage("Select corporate bank.");
		    		return false;
		    	}
				if( $('#bankcurrencylist').val() <= 0){
					showMessage("Select currency.");
		    		return false;
		    	}
				if( $('#receivingMethodslist').val() <= 0){
					showMessage("Select receiving method.");
		    		return false;
		    	}
				if( $('#amountSlablist').val() <= 0){
					showMessage("Select amount slab.");
		    		return false;
		    	}
				if( $('#amount').val() <= 0){
					showMessage("Invalid amount.");
		    		return false;
		    	}
				
				var validateOk = $(this).form('validate');
				if (validateOk) {
					showProgress();
					return true;
				}
				return false;
			},
			success : function(saveResponse) {
				var message = getSaveErrorMessage(saveResponse, "", "");

				if (message) {
					hideProgress();
					showWarning(message);
				} else {
					loadURL("list");
				}
			}
		});

		$("#corporateBanklist").change(
				function() {
					
					var e = document.getElementById("corporateBanklist");
					var bankId = e.options[e.selectedIndex].value;
					document.getElementById("amount").value = 0;
					
					//alert(bankId);
					if (!bankId) {

						var methodCombo = document
								.getElementById("receivingMethodslist");
						var numberOfOptionsMethods = methodCombo.options.length;

						var currencyCombo = document
								.getElementById("bankcurrencylist");
						var numberOfOptionsCurrency = currencyCombo.options.length;

						for (i = 0; i < numberOfOptionsMethods; i++) {
							methodCombo.remove(0);
						}
						for (j = 0; j < numberOfOptionsCurrency; j++) {
							currencyCombo.remove(0);
						}
						return;
					}

					$.ajax({
						url : "receivingmethods.json-"
								+ bankId,
						data : bankId,
						type : 'GET',
						dataType : 'json',
						success : function(data) {
							//alert(data);
							var methodCombo = document
									.getElementById("receivingMethodslist");
							var numberOfOptions = methodCombo.options.length;

							for (i = 0; i < numberOfOptions; i++) {
								methodCombo.remove(0);
							}

							$.each(data, function(index, val) {
								//alert(val.methodName);
								var receivingMethod = document
										.createElement("option");
								receivingMethod.textContent = val.methodName;
								receivingMethod.value = val.id;
								methodCombo.appendChild(receivingMethod);
							});
						}
					});
					
					$.ajax({
						url : "bankcurrency.json-"
								+ bankId,
						data : bankId,
						type : 'GET',
						dataType : 'json',
						success : function(data) {
							//alert(data);
							var currencyCombo = document
								.getElementById("bankcurrencylist");
							var numberOfOptionsCurrency = currencyCombo.options.length;

							for (i = 0; i < numberOfOptionsCurrency; i++) {
								currencyCombo.remove(0);
							}

							$.each(data, function(index, val) {
								//alert(val.methodName);
								var bankCurrency = document
										.createElement("option");
								bankCurrency.textContent = val.currencyCodeId;
								bankCurrency.value = val.id;
								currencyCombo.appendChild(bankCurrency);
							});
						}
					});
					
					
				});
		
				$("#bankcurrencylist").change(function() {
					getBankCharge();
				});
				
				$("#receivingMethodslist").change(function() {
					getBankCharge();
				});
				
				$("#amountSlablist").change(function() {
					getBankCharge();
				});
	});
	
	
	function getBankCharge(){
			
			document.getElementById("amount").value = 0;

			var objcurrencycode = document.getElementById("bankcurrencylist");
			var currencycode = objcurrencycode.options[objcurrencycode.selectedIndex].value;
			
			var objreceivingmethod = document.getElementById("receivingMethodslist");
			var receivingmethod = objreceivingmethod.options[objreceivingmethod.selectedIndex].value;
	
			var objAmountslab = document.getElementById("amountSlablist");
			var amountslab = objAmountslab.options[objAmountslab.selectedIndex].value;
			
	
			if(currencycode == "" || receivingmethod == "" || amountslab == ""){
				return false;
			}
			
			$.ajax({
				url : "getbankcharge-" + currencycode + "-" + receivingmethod + "-" + amountslab,
				data : currencycode,
				type : 'GET',
				dataType : 'json',
				success : function(data) {
					if(data)
					{
						document.getElementById("amount").value = data.amount;
					}
				}
			});
		}
	
	
	function checkIt(evt) {
        evt = (evt) ? evt : window.event
        var charCode = (evt.which) ? evt.which : evt.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            status = "This field accepts numbers only."
            return false
        }
        status = ""
        return true
    }
	
</script>
<div class="alert alert-danger" id="message" hidden="false" ></div>				   

<form:form id="frmEdit" method="post" modelAttribute="bankcharges">
	<form:hidden path="id" />
	<c:if test="${screenMode == 'edit'}">
		<form:hidden path="lastUpdateDate" />
	</c:if>
	<div class="form-group">
		<label class="control-label">Corporate Bank</label>
		<form:select path="" class="form-control" id="corporateBanklist">
			<option value="">Select</option>
			<form:options items="${corporateBankList}" itemValue="id"
				itemLabel="bankName" />
		</form:select>
	</div>

	<div class="form-group">
		<label class="control-label">Currency</label>
		<form:select path="bankCurrency.id" class="form-control" id="bankcurrencylist">
			<option value="id">Select</option>
		</form:select>
	</div>

	<div class="form-group">
		<label class="control-label">Receiving Method</label>
		<form:select path="receivingMethodId.id" class="form-control"
			id="receivingMethodslist">
			<option value="id">Select</option>
		</form:select>
	</div>

	<div class="form-group">
		<label class="control-label">Amount Slab</label>
		<form:select path="amountSlabId.id" class="form-control"
			id="amountSlablist" onchange="onComboselect(this.text)">
			<option value="">Select</option>
			<c:forEach var="amountslab" items="${amountSalbList}">
				<option value="${amountslab.id}"
					label="${amountslab.lowValue} - ${amountslab.highValue}" />
			</c:forEach>
		</form:select>
	</div>

	<div class="form-group">
		<label class="control-label">Amount</label>
		<form:input path="amount" id="amount" class="form-control" type="text"
			required="true" size="40" onKeyPress="return checkIt(event)" maxlength="7"/>
	</div>

	<div class="form-actions">
		<button type="submit" class="btn btn-primary">Save changes</button>
		<button type="button" class="btn" onclick="onCancel()">Cancel</button>
	</div>
</form:form>
