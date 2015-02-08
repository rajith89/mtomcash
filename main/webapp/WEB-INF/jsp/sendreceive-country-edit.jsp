<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Merchant"/>
</jsp:include>


	<script>
		function onCancel() {
	        loadURL('list-sendreceive-country');
	    }
		
		 $(function() {
		    	$('#frmEdit').form({
		            url:'save',
		            onSubmit:function() {
		            	var validateOk = $(this).form('validate');
		                if (validateOk) {
		                    showProgress();
		                    return true;
		                }
		                return false;
		            },
		            success:function(saveResponse) {
		                var message = getSaveErrorMessage(saveResponse, "","");
	
		                if (message) {
		                    hideProgress();
		                    showWarning(message);
		                } else {
		                	loadURL("list-sendreceive-country");
		                }
		            }
		        });
		    });
		 
		
 
	</script>

	
	
		<form:form id="frmEdit" method="post" modelAttribute="country">
			<form:hidden path="id"/>
			<form:hidden path="status"/>
			
			<div class="form-group">
				<label class="control-label">Country</label>
				
				<form:select path="countryId.id" id="currencylist" class="form-control">
                          <option value="">Select</option>
                          <form:options items="${countryList}" itemValue="id" itemLabel="countryDesc" />
                </form:select>
			</div>
			
			<div class="form-group">
				<label class="control-label">Type</label>

				<form:select path="type" id="type" class="form-control">
                          <option value="">Select</option>
                          <option value="1">Sending</option>
                          <option value="2">Receiving</option>
                </form:select>
			</div>
			
			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Save changes</button>
				<button type="button" class="btn" onclick="onCancel()">Cancel</button>
			</div>		
		</form:form>
