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

	$(function() {
		$('#frmEdit').form({
	        url:'topup',
	        onSubmit:function() {
	        	
	        	if( $('#merchantlist').val() <= 0){
	        		alert("Please select merchant.");
	        		return false;
	        	}
	        	
	        	
	        	if(!$.isNumeric($('#credit').val())){
	        		alert("Invalid amount.");
	        		return false;	        		
	        	}
	        	
	        	
	        	if($('#credit').val() <= 0){
	        		alert("Invalid amount.");
	        		return false;
	        	}
	        		        	
	        	var x = confirm("Confirm to top up amount ?");
		   	  	if (!x)
		   	  	{
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
	            	alert(message);
	            } else {
	            	hideProgress();
					$('#credit').val("0");
					$('#note').val("");
					$('#merchantlist').val("0");
					alert("Successfully toppup amount.");
	            }				
	        }
	    });
		
		
		$("#merchantlist").change(function() {
			
			var e = document.getElementById("merchantlist");
			var merchantId = e.options[e.selectedIndex].value;
			
			
			$.ajax({
				url : "../merchant/getmerchantcurrency-" + merchantId,
				data : merchantId,
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					if(data)
					{
						document.getElementById("currencycode").value = data.currencycode;
					}
				}
			});
		 });
		
		
		
		
	});

   
    function onCancel() {
        loadURL('list');
    }
    
    function onGenerate() {
        loadURL('list');
    }
    
    function onDelete() {
        var selArr = $('#id')[0].value;
        if (selArr) {
            showConfirmDialog('Confirm to dele', function(r) {
                if (r) {
                    alert("Implement Delete all - " + selArr);
                }
            });
        } else {
            showWarning("No records selected");
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



<div region="center" border="false" class="screen-content">

		<form:form id="frmEdit" method="post" modelAttribute="merchantFilter">
			
			<div class="form-group">
				<label class="control-label">Merchant</label>
				
				<form:select path="merchantId" id="merchantlist" class="form-control">
                          <form:option value="0">Select</form:option>
                          <c:forEach var="merchants" items="${merchantList}" >
					        <form:option value="${merchants.id}" label="${merchants.firstName} ${merchants.lastName}"/>
					      </c:forEach>
               </form:select>
			</div>
			
		 	 <div class="form-group">
                   	<label class="control-label">Currency</label>
                   	<input class="form-control" type="text" id="currencycode" readonly="true"/> 
             </div>
			
			<div class="form-group">
				<label class="control-label">Amount</label>
				<form:input path="credit" class="form-control" type="nummeric" required="true" size="40" onKeyPress="return checkIt(event)" maxlength="7"/>
			</div>
						
			<div class="form-group">
				<label class="control-label">Description</label>
				<form:textarea path="note" class="form-control" type="text" required="true" size="40" />
			</div>
			
						
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Top Up</button>
				<button type="reset" class="btn">Reset</button>
			</div>		
		</form:form>
	     

</div>
