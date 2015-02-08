<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Merchant"/>
</jsp:include>


	<script>
		function onCancel() {
	        loadURL('list');
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
		                	loadURL("list");
		                }
		            }
		        });
		    });
 
	</script>

	
	
		<form:form id="frmEdit" method="post" modelAttribute="currencycode">
			<form:hidden path="id"/>
			<div class="form-group">
				<label class="control-label">Code</label>
				<form:input path="currencyCode" class="form-control" type="text" required="true" size="40" maxlength="3"/>
			</div>
			
			<div class="form-group">
				<label class="control-label">Description</label>
				<form:input path="currencyDesc" class="form-control" type="text" required="true" size="40" />
			</div>
			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Save changes</button>
				<button type="button" class="btn" onclick="onCancel()">Cancel</button>
			</div>		
		</form:form>
