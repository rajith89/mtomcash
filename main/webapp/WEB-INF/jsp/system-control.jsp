<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<meta http-equiv="refresh" content="5" >

<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Merchant"/>
</jsp:include>

<script type="text/javascript">

function onValidate(status) {
	   
   	  var x = confirm("Confirm to change Transaction status ?");
  	  if (!x)
  	  {	  		  
  		return false;
  	  }
    	
    	showProgress();
        $.ajax({
            url:'changestatus-' + status,
            type:'POST',
            dataType:'json',
            success:function (saveResponse) {
                var message = getSaveErrorMessage(saveResponse, "", "");
                if (message) {
                	hideProgress();
                	alert(message);
                	loadURL("system-status");
                	return false;
                } else {
                	hideProgress();
                	alert('Transaction status successfully changed.');
                	loadURL("system-status");
                    if(callbackFunc){
                        callbackFunc();
                        callbackFunc = null;
                    }
                    return true;
                }
            },
            error:function (err) {
                hideProgress();
                return false;
            }

        });

}
	
</script>

	
<form:form id="frmEdit" method="post" >
			<c:if test="${systemstatus.transactionStatus == 1}"> 
               	<h3>Transaction Status : <font color='Green'>Started</font> </h3> 
            	<button type="button" class="btn btn-primary btn-large" onclick="onValidate(2)">Stop</button>
            </c:if>
			<c:if test="${systemstatus.transactionStatus == 2}"> 
               	<h3>Transaction Status : <font color='Red'> Stopped </font></h3>
               	<button type="button" class="btn btn-primary btn-large" onclick="onValidate(1)">Start</button>
             </c:if>
					
</form:form>



<!-- <html> -->
<!-- <body> -->
<%-- 	<h1>Transaction Status : ${systemstatus.transactionStatus}</h1>	 --%>
<!-- </body> -->
<!-- </html> -->