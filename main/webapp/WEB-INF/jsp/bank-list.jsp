<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%-- <spring:message code="label.conductor" var="entity"/> --%>
<%-- <spring:message code="label.entity.listing.title" arguments="${entity}" var="pageTitle"/> --%>

<script>
 function onActivate() {
			    var selId = 1;
			    var batchids = "";
			    
			    var sel = $('#grid').datagrid('getSelected');
			    if (!sel) {
			        showAlert("No record has selected");
			        return;
			    }
			    
			    var selbatchArr = $('#grid').datagrid('getSelections');
			    
			    
			    for (var i = 0; i < selbatchArr.length; i++) {
			        var rec = selbatchArr[i];
			        if(rec.status == 1) //Check Active status
			        {
			        	showAlert("You have selected already activated banks.");
				        return;	
			        }
			        batchids += rec.id + ",";
			    }
			    
			    
			    if (selId) {
			    	 var x = confirm("Confirm to activate banks ?");
				  	 if (!x)
				  	 {
				  		return false;
				  	 }
				  	  
			    	showProgress();
			        $.ajax({
			           url:'activate-' + batchids,
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
			                	alert('Banks successfully activated.');
			                	loadURL("list");
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

 
 function onDeactivate() {
	    var selId = 1;
	    var batchids = "";
	    
	    var sel = $('#grid').datagrid('getSelected');
	    if (!sel) {
	        showAlert("No record has selected");
	        return;
	    }
	    
	    var selbatchArr = $('#grid').datagrid('getSelections');
	    
	    
	    for (var i = 0; i < selbatchArr.length; i++) {
	        var rec = selbatchArr[i];
	        if(rec.status == 2) //Check Deactive status
	        {
	        	showAlert("You have selected already deactivated banks.");
		        return;	
	        }
	        batchids += rec.id + ",";
	    }
	    
	    
	    if (selId) {
	    	 var x = confirm("Confirm to deactivate banks ?");
		  	 if (!x)
		  	 {
		  		return false;
		  	 }
		  	  
	    	showProgress();
	        $.ajax({
	           url:'deactivate-' + batchids,
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
	                	alert('Banks successfully deactivated.');
	                	loadURL("list");
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
	</script>


<jsp:include page="header.jsp">
    <jsp:param name="title" value="${pageTitle}"/>
</jsp:include>

<jsp:include page="list-scripts.jsp"/>

<div region="north" border="false" class="screen-toolbar">
   
    <jsp:include page="bank-tools.jsp">
        <jsp:param name="add"  value="true"/>
        <jsp:param name="edit" value="true"/>
    </jsp:include>
</div>

<div region="center" border="false" class="screen-content">
    <table id="grid" class="easyui-datagrid active-bg"
           url="list.json" pageList="[25,50,100,200]"
           fit="true" fitColumns="true" pagination="true">
        <thead>
        <tr>
            <th width="30" checkbox="true"></th>
            
            <th field="bankName" width="100">Bank Name</th>
            <th field="country" width="100">Country</th>
            <th field="contactperson" width="100">Contact person</th>
            <th field="telephone" width="100">Telephone</th>
            <th field="status" width="100" hidden="true">Status</th>
            <th field="statusdesc" width="100">Status</th>
        </tr>
        </thead>
    </table>
</div>
<jsp:include page="footer.jsp"/>