<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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
	
    function validatePasswordPin(){
		var validateresult = 0;

		 $.ajax({  
		        type : 'GET',  
		        url : "validatePasswordPin",  
		        data : { "username":$("#j_username").val(), "password":$("#passwordPin").val()}, 
		        async: false,
		        success : function(data) {  
	 				if(data){
						if(data.code != "00") {
							validateresult = 0;
							return validateresult;
						}else{
							validateresult = 1;
							return validateresult;
						}
					}
		        }  
		    });  		
		return validateresult;
	}
    
    function onSubmit() {

        makeRequired("#j_username");
        makeRequired("#j_password");
        
        var result = validatePasswordPin();
        if(result == 0){
        	alert("Invalid username or password");
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

               <form id="frmEdit" method="post" form action="../../j_spring_security_check" onsubmit="return onSubmit()" >
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
					  <div class="screen-toolbar" style="text-align: left; height: 45px">
                    	<c:if test="${not empty error}">
<!--                     	<div class="alert alert-danger" id="message" hidden="false" ></div>				    -->
                    	<div class="alert alert-danger" id="login-error" style="padding: 3px;">
                            Invalid username or password
                        </div>
                    	</c:if>
                	</div>
                	</td></tr>
			
				     <tr>
						<td>
							<div class="form-group">
								<label class="control-label">Username</label>
								<input id="j_username" name="j_username"  class="form-control" type="text" required="true" width="40" onkeydown="clearError()"/>
							</div>
							
							<div class="form-group">
								<label class="control-label">Password</label>
								<input id="j_password" name="j_password"  class="form-control" type="password" required="true" size="40" onkeydown="clearError()"/>
							</div>
							
							<div class="form-group">
								<label class="control-label">Password PIN</label>
								<input id="passwordPin" name="passwordPin"  class="form-control" type="password" required="true" size="40"  onkeydown="clearError()"/>
							</div>
							
							<div class="form-actions">
								<button type="submit" class="btn btn-primary">Sign In</button>
								&nbsp&nbsp
								<a href="resetpassword-page">Lost you password ?</a>
							</div>
					 </tr>
					 
					 <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
				     <tr><td>&nbsp</td></tr>
					
				</form>
			</table>
      	</td>
      
      </tr>
      
      <tr><td align="center"> <h6><font color="#0072c6">The Fastest Way</font> to send money around the world</h6> </td></tr>
      </table>
    
    
  </div>
</div>

