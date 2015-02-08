<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Message Detail Report"/>
</jsp:include>

<script type="text/javascript">
	$( document ).ready(function() {
		//alert($("password").val());
		$("#password").val("");
		$("#newpassword").val("");
		$("#passwordpin").val("");
		$("#newpasswordpin").val("");
	});
	
	$(function() {
		$('#frmEdit').form({
	        url:'changepassword',
	        onSubmit:function() {
	        	
	        	if($("#password").val() != $("#newpassword").val()){
	            	alert("Passwords do not match");
	            	return false;
	            }
	        	
	        	if($("#passwordpin").val() != $("#newpasswordpin").val()){
	             	alert("Password PIN do not match");
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
	
	            var message = getSaveErrorMessage(saveResponse, "Error Occured",
	                    "Error Occured");
	
	            if (message) {
	                hideProgress();
	                showWarning(message);
	            } else {
	            	hideProgress();
	            	alert("Password changed successfully.");
	                loadURL("password_change_page");
	            }
	        }
	    });
	});


    function clearError() {
        $("#login-error")[0].style.visibility = "hidden";
    }

    $(document).ready(function () {
        $("#j_username").focus();
    });

    function onSubmit() {
        makeRequired("#j_username");
        makeRequired("#j_password");

        if($("password") != $("newpassword")){
        	alert("Passwords do not match");
        	return false;
        }

        var validateOk = $('#ff').form('validate');
        if (validateOk) {
            showProgress();
            return true;
        }
        return false;
    }
</script>


<div region="center" border="false" class="screen-content">

		 <form:form id="frmEdit" method="post" modelAttribute="user">
		 <form:hidden path="id"/>
			
			
			
			<div class="form-group">
				<label class="control-label">User Name</label>
				<form:input path="userName" class="form-control" type="text" required="true"  size="40" readonly="true"/>
			</div>
						
			<div class="form-group">
				<label class="control-label">New Password</label>
			     <form:input path="passwordEnc" class="form-control" type="password" required="false"  size="40" id="password"/>
			</div>
			
			<div class="form-group">
				<label class="control-label">Confirm Password</label>
			    <input path="password" class="form-control" type="password" required="false"  size="40" id="newpassword"/>
			</div>
			
			<div class="form-group">
				<label class="control-label">New Password PIN</label>
			     <form:input path="passwordPinEnc" class="form-control" type="password" required="false"  size="40" id="passwordpin"/>
			</div>
			
			<div class="form-group">
				<label class="control-label">Confirm Password PIN</label>
			    <input path="password" class="form-control" type="password" required="false"  size="40" id="newpasswordpin"/>
			</div>
						
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Change Password</button>
				<button type="reset" class="btn" >Reset</button>
			</div>		
		</form:form>
	     

</div>

<div region="south" border="false" class="screen-toolbar">
 
</div>

</body>
</html>
