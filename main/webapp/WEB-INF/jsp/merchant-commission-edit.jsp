<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>  
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>


<jsp:include page="header-form.jsp">
    <jsp:param name="title" value="Card Generator"/>
</jsp:include>

<script type="text/javascript">

	$(function() {
		$('#frmGenerate').form({
	        url:'save-commission',
	        onSubmit:function() {
	        	
	        	if( $('#merchantlist').val() <= 0){
	        		alert("Please select merchant.");
	        		return false;
	        	}
	        	
	        	if( $('#amountSlablist').val() <= 0){
	        		alert("Please select amount slab.");
	        		return false;
	        	}
	        	
	        	if(!$.isNumeric($('#amount').val())){
	        		alert("Invalid commission amount.");
	        		return false;	        		
	        	}
	        	
	        	
	        	
	        	if($('#amount').val() <= 0){
	        		alert("Invalid commission amount.");
	        		return false;
	        	}
	        	
	        	
	        	var x = confirm("Confirm to save commission ?");
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
// 	            if (message) {
// 	            	hideProgress();
// 	                showWarning(message);
// 	            } else {
// 	            	hideProgress();
// 	            	alert("Card successfully generated.");
// 	            	loadURL("list");
// // 	         }
				hideProgress();
				$('#amount').val("0");
				$('#merchantlist').val("0");
				$('#amountSlablist').val("0");
				alert("Merchant commission successfully saved.");
	        }
	    });
		
		
		$("#merchantlist").change(function() {
			
			var e = document.getElementById("merchantlist");
			var merchantId = e.options[e.selectedIndex].value;
			document.getElementById("currencycode").value = "";
			
			getCurrentCommission();
			
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
		
		
		 $("#amountSlablist").change(function() {			
			getCurrentCommission();
		 });

	});
	
	function getCurrentCommission(){
		
		document.getElementById("amount").value = 0;
		
		var objMerchant = document.getElementById("merchantlist");
		var merchantId = objMerchant.options[objMerchant.selectedIndex].value;

		var objAmountslab = document.getElementById("amountSlablist");
		var amountslab = objAmountslab.options[objAmountslab.selectedIndex].value;
		

		if(merchantId == "" || amountslab == ""){
			return false;
		}
		
		$.ajax({
			url : "../merchant/getmerchantcommission-" + merchantId + "-" + amountslab,
			data : merchantId,
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				if(data)
				{
					document.getElementById("amount").value = data.amount;
				}
			}
		});
	}
   
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
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        alerrt(charCode);
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            status = "This field accepts numbers only.";
            return false;
        }
        status = "";
        return true;
    }


</script>



<div region="center" border="false" class="screen-content">

    <form:form id="frmGenerate" method="POST" modelAttribute="commission">

        <div class="ff-container">
            <table border="0">
               
                <tr>
                    <div class="form-group">
                    <label class="control-label">Merchant</label>
                        <form:select path="merchantId.id" class="form-control" id="merchantlist">
	                          <option value="">Select</option>
	                          <c:forEach var="merchants" items="${merchantList}" >
						        <form:option itemValue="${merchants.id}" value="${merchants.id}" label="${merchants.firstName} ${merchants.lastName}"/>
						      </c:forEach>
                        </form:select>
                        
<%--                         <form:select path="merchantId.id" class="form-control" id="merchantlist" onchange="onComboselect(this.text)"> --%>
<!-- 	                          <option value="">Select</option> -->
<%-- 	                          <form:options items="${merchantList}" itemValue="id" itemLabel="firstName" /> --%>
<%--                         </form:select> --%>
                   </div>
                </tr>
                
                <div class="form-group">
                    	<label class="control-label">Currency</label>
                    	<input class="form-control" type="text" id="currencycode" readonly="true"/> 
              	</div>
                
                
                 <tr>
                 <div class="form-group">
                    <label class="control-label">Amount Slab</label>
                        <form:select path="amountSlab.id"  class="form-control" id="amountSlablist" >
                          <option value="">Select</option>
                          <c:forEach var="amountslab" items="${amountSalbList}" >
					        <option value="${amountslab.id}" label="${amountslab.lowValue} - ${amountslab.highValue}"/>
					      </c:forEach>
                        </form:select>
                 </div>
                 </tr>
                                 
              
              <tr>
	              <div class="form-group">
						<label class="control-label">Amount</label>
						<form:input path="amount" id="amount" class="form-control" type="text" required="true" size="40" onKeyPress="return checkIt(event)" maxlength="7"/>
				  </div>
			  </tr>
			
		        
               
                <tr>
                   <div class="form-actions">
						<button type="submit" class="btn btn-primary">Update Commission</button>
						<button type="reset" class="btn">Reset</button>
					</div>	
                </tr>

            </table>
        </div>
</form:form>      
        
         
        

</div>
