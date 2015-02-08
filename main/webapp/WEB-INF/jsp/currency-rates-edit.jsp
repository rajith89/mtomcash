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

	$(function() {
		$('#frmEdit').form({
			url : 'save',
			onSubmit : function() {
							
				if( $('#corporateBanklist').val() <= 0){
	        		alert("Please select corporate bank.");
	        		return false;
	        	}
				var x = confirm("Confirm to upload currency rate ?");
		   	  	if (!x)
		   	  	{
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

<form:form id="frmEdit" method="post" modelAttribute="currencyRatesUpload" enctype="multipart/form-data">

	<div class="form-group">
		<label class="control-label">Corporate Bank</label>
		<form:select path="id"
			class="form-control" id="corporateBanklist">
			<option value="">Select</option>
			<form:options items="${corporateBankList}" itemValue="id"
				itemLabel="bankName" />
		</form:select>
	</div>

	<div class="form-group">
		<label class="control-label">Select CSV file</label>
		<form:input path="file" type="file" class="form-control"
			required="true" size="20" />
	</div>

	<div class="form-actions">
		<button type="submit" class="btn btn-primary">Save changes</button>
	</div>
</form:form>
