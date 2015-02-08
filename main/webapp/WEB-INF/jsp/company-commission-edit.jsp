<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form"%>


<jsp:include page="header-form.jsp">
	<jsp:param name="title" value="Company Commission edit" />
</jsp:include>


<script>

	function onCancel() {
		loadURL('list');
	}

	 function showMessage(message){
			//$("span" ).text(message).show().fadeOut(3000);
			$("#message" ).text(message).show().fadeOut(3000);
			
			
		}
	
	$(function() {
		$('#frmEdit').form({
			url : 'save',
			onSubmit : function() {
				
				if( $('#merchantlist').val() <= 0){
					showMessage("Select Merchant.");
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
	});
	
</script>


<div class="alert alert-danger" id="message" hidden="false" ></div>		
<form:form id="frmEdit" method="post" modelAttribute="companyCommision">

	<div class="form-group">
		<label class="control-label">Merchant</label>

		<form:select path="merchantId.id" id="merchantlist"
			class="form-control">
			<form:option value="0">Select</form:option>
			<c:forEach var="merchants" items="${merchantList}">
				<form:option value="${merchants.id}"
					label="${merchants.firstName} ${merchants.lastName}" />
			</c:forEach>
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
		<form:input path="amount" class="form-control" type="nummeric"
			required="true" size="40" />
	</div>

	<div class="form-actions">
		<button type="submit" class="btn btn-primary">Save changes</button>
		<button type="button" class="btn" onclick="onCancel()">Cancel</button>
	</div>
</form:form>


