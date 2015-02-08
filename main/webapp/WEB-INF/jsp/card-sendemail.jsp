<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>  
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>


<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Message Detail Report"/>
</jsp:include>

<script type="text/javascript">
   
function onValidate() {
   
    var selId = 1;
    var batchids = "";
    
    var sel = $('#batchId').datagrid('getSelected');
    if (!sel) {
        showAlert("No record has selected");
        return;
    }
    
    var selbatchArr = $('#batchId').datagrid('getSelections');
    
    
    for (var i = 0; i < selbatchArr.length; i++) {
        var rec = selbatchArr[i];
        batchids += rec.id + ",";
    }
    
    if (selId) {
    	 var x = confirm("Confirm to send cash cards ?");
	  	 if (!x)
	  	 {	  		  
	  		return false;
	  	 }
	  	  
    	showProgress();
        $.ajax({
           url:'sendemail-' + batchids,
            type:'POST',
            dataType:'json',
            success:function (saveResponse) {
                var message = getSaveErrorMessage(saveResponse, "Error Occured",
                        "Error Occured");
                if (message) {
                	hideProgress();
                   return false;
                } else {
                	hideProgress();
                	alert('Card sent successfull.');
                	loadURL("sendemail_page");
                	
//                     if(callbackFunc){
//                         callbackFunc();
//                         callbackFunc = null;
//                     }
                    return false;
                }

            },
            error:function (err) {
                hideProgress();
                return false;
            }

        });

    } else {
        showWarning("No record has selected");
    }
}
    
    
    
function onDelete() {
	   
    var selId = 1;
    var batchids = "";
    
    var sel = $('#batchId').datagrid('getSelected');
    if (!sel) {
        showAlert("No record has selected");
        return;
    }
    
    var selbatchArr = $('#batchId').datagrid('getSelections');
    
    
    for (var i = 0; i < selbatchArr.length; i++) {
        var rec = selbatchArr[i];
        batchids += rec.id + ",";
    }
    
    if (selId) {
    	 var x = confirm("Confirm to delete cash cards ?");
	  	 if (!x)
	  	 {	  		  
	  		return false;
	  	 }
	  	  
    	showProgress();
        $.ajax({
           url:'delete-' + batchids,
            type:'POST',
            dataType:'json',
            success:function (saveResponse) {
                var message = getSaveErrorMessage(saveResponse, "Error Occured",
                        "Error Occured");
                if (message) {
                	hideProgress();
                   return false;
                } else {
                	hideProgress();
                	alert('Card deleted successfull.');
                	loadURL("sendemail_page");

                    return false;
                }

            },
            error:function (err) {
                hideProgress();
                return false;
            }

        });

    } else {
        showWarning("No record has selected");
    }
}
    
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

<!--  <div class="screen-toolbar" style="text-align: left; height: 45px"> -->
<%--                     <c:if test="${not empty message}"> --%>
<!--                         <div id="login-error" -->
<!--                              style="font-size: 11px; text-align: center;background-color: #FBEC88; padding: 3px;"> -->
<!--                             asasasasasasasasasasasasas -->
<!--                         </div> -->
<%--                     </c:if> --%>
<!--  </div> -->


<div region="center" border="false" class="screen-content">

    <form:form id="frmSearch" method="POST" modelAttribute="cardfilter">
	
	
		<table id="batchId" class="easyui-datagrid active-bg"
		           url="pendingbatches.json"
		           fit="false" fitColumns="true" pagination="false" showHeader="true">
			        <thead>
				        <tr>
				            <th width="30" checkbox="true"></th>
				            <th field="id" width="100" >Id</th>
				            <th field="merchantName" width="100">Merchant</th>
				            <th field="value" width="100">Card Value</th>
				            <th field="currencyCode" width="100">Currency</th>
				            <th field="noofCards" width="100">No of Cards</th>
				            
				        </tr>
			        </thead>
		</table>
						
						
        <div class="ff-container">
            <table border="0" width="50%">
                
                <tr>
                     <td class="ff-value"></td>
                </tr>
               
                <tr>
                    <td class="ff-value">
	                    <div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="onValidate();">Send Email</button>
							<button type="button" class="btn btn-primary" onclick="onDelete()">Delete Batch</button>
						</div>		
					</td>
                </tr>

            </table>
        </div>
</form:form>      
        
         
        

</div>
