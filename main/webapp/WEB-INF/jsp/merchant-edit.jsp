<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Merchant"/>
</jsp:include>

	
	<script>
		
		function onCancel() {
			//alert($('#currencyList').val());
	        loadURL('list');
	    }
		
		function callComboChange() {
			$('#countrylist').change();
	    }
		
		 $(function() {
		    	$('#frmEdit').form({
		            url:'save',
		            onSubmit:function() {
// 		            	if( $('#creditLimit').val() <= 0){
// 							showMessage("Invalid amount.");
// 				    		return false;
// 				    	}
		            	if( $('#countrylist').val() <= 0){
							showMessage("Select country.");
				    		return false;
				    	}
						if( $('#currencyList').val() <= 0){
							showMessage("Select currency.");
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
		   
		 
		 $("#countrylist").change(function() {
						var e = document.getElementById("countrylist");
						var countryId = e.options[e.selectedIndex].value;
	
						if (!countryId) {
							var methodCombo = document.getElementById("currencyList");
							var numberOfOptions = methodCombo.options.length;

							for (i = 0; i < numberOfOptions; i++) {
								methodCombo.remove(0);
							}
							return;
						}

						$.ajax({
							url : "../currency/curency.json-"
									+ countryId,
							data : countryId,
							type : 'GET',
							dataType : 'json',
							success : function(data) {
								var methodCombo = document.getElementById("currencyList");
								var numberOfOptions = methodCombo.options.length;

								for (var i = 0; i < numberOfOptions; i++) {
									methodCombo.remove(0);
								}

								$.each(data, function(index, val) {
									var receivingMethod = document.createElement("option");
									receivingMethod.textContent = val.currencyName;
									receivingMethod.value = val.id;
									methodCombo.appendChild(receivingMethod);
								});
							}
						});
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
			
			function showMessage(message){
				//$("span" ).text(message).show().fadeOut(3000);
				alert(message);			
				
			}
		 
 
	</script>


<div class="alert alert-danger" id="message" hidden="false" ></div>				   
	
<!-- 		TODO have to implement dates -->
<form:form id="frmEdit" method="post" modelAttribute="merchant">
			<form:hidden path="id"/>
			<form:hidden path="status"/>
			<form:hidden path="createdBy"/>
<%-- 			<form:hidden path="createdDate"/> --%> 
			<form:hidden path="lastUpdateUser"/>
<%-- 			<form:hidden path="lastUpdateDate"/> --%>
			<form:hidden path="balance"/>

<table width="100%" height="100%" border="1">						
			<div class="form-group">
				<label class="control-label">First Name</label>
				<form:input path="firstName" class="form-control" type="text" required="true" size="40" />
			</div>
			
			<div class="form-group">
				<label class="control-label">Last Name</label>
				<form:input path="lastName" class="form-control" type="text" required="true" size="40" />
			</div>
			
			<div class="form-group">
				<label class="control-label">Email</label>
				<form:input path="email" class="form-control" type="email" required="true" size="40" />
			</div>
			
			<div class="form-group">
				<label class="control-label">Telephone</label>
				<form:input path="telephone" class="form-control" type="text" required="true" size="40" onKeyPress="return checkIt(event)" placeholder="94 XXXXXXXXXX" />
			</div>
			
			<div class="form-group">
				<label class="control-label">Address</label>
				<form:input path="addressLine1" class="form-control" type="text" required="true" size="40" />
				<form:input path="addressLine2" class="form-control" type="text" required="true" size="40" />
				<form:input path="addressLine3" class="form-control" type="text" required="true" size="40" />
			</div>
			
			<div class="form-group">
				<label class="control-label">Credit Limit</label>
				<form:input path="creditLimit" id="creditLimit" class="form-control" type="text" required="true" size="40" onKeyPress="return checkIt(event)" maxlength="7"/>
			</div>
			
			<c:if test="${screenMode == 'add'}"> 
				<div class="form-group">
					<label class="control-label">Country</label>
					
					<form:select path="countryCode.id" id="countrylist" class="form-control">
	                          <option value="">Select</option>
	                          <form:options items="${countryList}" itemValue="id" itemLabel="countryDesc" />
	                </form:select>
				</div>
			</c:if>
			<c:if test="${screenMode == 'edit'}"> 
				<div class="form-group">
					<label class="control-label">Country</label>
						<form:hidden path="countryCode.id" />
						<input id="countrydesc" class="form-control" value="${countryList}" type="text" size="40" readonly="true"/>

				</div>
			</c:if>
			
			<c:if test="${screenMode == 'add'}"> 
               <div class="form-group">
					<label class="control-label">Currency</label>
					<form:select path="countryCurrency.id" class="form-control" id="currencyList">
						<option value="id">Select</option>
					</form:select>
				</div>
            </c:if>
           
            <c:if test="${screenMode == 'edit'}"> 
               	<div class="form-group">
						<label class="control-label">Currency</label>
						<form:hidden path="countryCurrency.id" />
						<input id="countryCurrencyDesc" class="form-control" value="${currencyList}" type="text" size="40" readonly="true"/>
				</div>
				
				<form:hidden path="createdDate"/> 
			 	<form:hidden path="lastUpdateDate"/>				
            </c:if>
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Save changes</button>
				<button type="button" class="btn" onclick="onCancel()">Cancel</button>
			</div>
	</table>		
</form:form>
