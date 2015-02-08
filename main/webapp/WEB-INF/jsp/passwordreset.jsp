<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="java.util.Calendar" %>

<%-- <spring:message code="label.login.title" var="pageTitle"/> --%>
<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="${pageTitle}"/>
</jsp:include>

<script type="text/javascript">

    function clearError() {
        $("#login-error")[0].style.visibility = "hidden";
    }

    $(document).ready(function () {
        $("#j_username").focus();
    });
	    
    
     	$(function() {
		    	$('#frmEdit').form({
		            url:'resetpassword',
		            onSubmit:function() {
		            	makeRequired("#username");
		            	
		            	var x = confirm("Confirm to reset the password ?");
		              	if (!x)
		              	{	  		  
		              		return false;
		              	}
		            	var validateOk = $(this).form('validate');
		                if (validateOk) {
		                    //showProgress();
		                    return true;
		                }
		                return false;
		            },
		            success:function(saveResponse) {
		            	alert("Your password has been sent to your email address.");
		            	loadURL("login");
		            }
		        });
		});
    
    function onCancel() {
    	loadURL('login');
    }
</script>

		<style>

			form {
				width: 500px;
				padding: 0px;
			}

			input.form-control {
				width: 275px;
			}
			button.btn btn-primary {
				width: 275px;
			}
			
</style>




<style>
  html, body {
    height: 100%;
    bgcolor= #E6E6FA;
  }
  #tableContainer-1 {
    height: 100%;
    width: 100%;
    display: table;
  }
  #tableContainer-2 {
    vertical-align: middle;
    display: table-cell;
    height: 100%;
  }
  #myTable {
    margin: 0 auto;
  }
</style>

<div id="tableContainer-1">
  <div id="tableContainer-2">
  
  
    <table id="myTable" border="0">
      <tr>
      	<td align="center">
      		<img src="/back/static/images/logo.png" alt="avatar" width="110" height="65"/>
      		<img src="/back/static/images/sampath_bank_logo.png" alt="avatar" width="150" height="70"/>
      	</td>
      </tr>
      
     <tr><td>&nbsp</td></tr>
	 <tr><td>&nbsp</td></tr>
      
      <tr>
      	<td>
      		<table border="0" >
				<form:form id="frmEdit" method="post" modelAttribute="bouser" >
				
					 <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
				     
				     <tr>
						<td align="center">
				      		<img src="/back/static/images/user_circle.PNG" alt="avatar" width="50" height="50"/>
				      	</td>
					 </tr>
					 
					 <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
				     
				     <tr><td>
					 
                	</td></tr>
			
				     <tr>
						<td>
							<div class="form-group">
								<label class="control-label">Username</label>
								<form:input path="userName" id="username" class="form-control" type="text" required="true" size="40" />
							</div>
							<div class="form-actions">
								<button type="submit" class="btn btn-primary">Reset Password</button>
								<button type="button" class="btn btn-primary" onclick="onCancel()">Cancel</button>
							</div>
					 </tr>
					 
					 <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
					
				</form:form>
			</table>
      	</td>
      
      </tr>
      
      <tr><td align="center"> <h6><font color="#0072c6">The Fastest Way</font> to send money around the world</h6> </td></tr>
      </table>
    
    
  </div>
</div>

