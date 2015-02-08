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
		            	
		            	if( $('#countryListlist').val() <= 0){
		            		alert("Please select country.");
		            		return false;
		            	}
		            	
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

	
	
		<form:form id="frmEdit" method="post" modelAttribute="corporatebank">
			<form:hidden path="id"/>
			<form:hidden path="status"/>
			
			<div class="form-group">
				<label class="control-label">Country</label>
				 <form:select id="countryListlist" path="countrySendReceiveId.id" class="form-control">
                          <option value="0">Select</option>
                          <c:forEach var="country" items="${countryList}" >
					        <form:option value="${country.id}" label="${country.countryname}"/>
					      </c:forEach>
                 </form:select>
						                
			</div>
			
			<div class="form-group">
				<label class="control-label">Bank Name</label>
				<form:input path="bankName" class="form-control" type="text" required="true" size="40" />
			</div>
			
			<div class="form-group">
				<label class="control-label">Address</label>
				<form:input path="addressLine1" class="form-control" type="text" required="true" size="40" />
			</div>
			<div class="form-group">
				<form:input path="addressLine2" class="form-control" type="text" required="true" size="40" />
			</div>
			<div class="form-group">
				<form:input path="addressLine3" class="form-control" type="text" required="true" size="40" />
			</div>
			<div class="form-group">
				<label class="control-label">Telephone</label>
				<form:input path="telephone" class="form-control" type="text" required="true" size="40" onKeyPress="return checkIt(event)"/>
			</div>
			<div class="form-group">
				<label class="control-label">Contact Person</label>
				<form:input path="contactPerson" class="form-control" type="text" required="true" size="40" />
			</div>
			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Save changes</button>
				<button type="button" class="btn" onclick="onCancel()">Cancel</button>
			</div>		
		</form:form>
