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

function onVerify() {
	
	if( $('#merchantlist').val() <= 0){
		alert("Please select merchant.");
		return false;
	}
	
	if( $('#currencylist').val() <= 0){
		alert("Please select currency.");
		return false;
	}
	
	if(!$.isNumeric($('#cardvalue').val())){
		alert("Invalid Card value.");
		return false;	        		
	}
	if(!$.isNumeric($('#noofcard').val())){
		alert("Invalid No of cards.");
		return false;    		
	}
	
	
	if($('#cardvalue').val() <= 0){
		alert("Invalid Card value.");
		return false;
	}
	
	if( $('#noofcard').val() <= 0){
		alert("Invalid No of cards.");
		return false;
	}

	var x = confirm("Confirm to generate Cards ?");
  	if (!x)
  	{	  		  
  		return false;
  	}
	$.ajax({
		url : "generate-" + $('#merchantlist').val() + "-" + $('#cardvalue').val() + "-" + $('#noofcard').val(),			
		data : $('#merchantlist').val() + "-" + $('#cardvalue').val() + "-" + $('#noofcard').val(),		
		type : 'POST',
		dataType : 'json',
		success : function(data) {
				if(data){
					if(data.code == "00") { //Success response
						alert("Card successfully generated.");
						$('#cardvalue').val("0");
						$('#noofcard').val("0");
						$('#merchantlist').val("0");
						$('#currencycode').val("");
					}else{
						alert(data.message);
					}
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

    <form:form id="frmGenerate" method="POST" modelAttribute="cardfilter" onsubmit="onVerify()">

        <div class="ff-container">
					
					<div class="form-group">
							<label class="control-label">Merchant</label>
	                        <form:select path="merchantId" class="form-control" id="merchantlist">
	                          <option value="">Select</option>
	                          <c:forEach var="merchants" items="${merchantList}" >
						        <option value="${merchants.id}" label="${merchants.firstName} ${merchants.lastName}"/>
						      </c:forEach>
	                        </form:select>
					</div>
  
                    <div class="form-group">
                    	<label class="control-label">Currency</label>
                    	<input class="form-control" type="text" onKeyPress="return checkIt(event)" id="currencycode" readonly="true"/> 
<%--                         <form:select path="currencyCode" class="form-control" id="currencylist" onchange="onComboselect(this.text)"> --%>
<!--                           <option value="">Select</option> -->
<%--                           <form:options items="${currenyList}" itemValue="id" itemLabel="currencyDesc" /> --%>
<%--                         </form:select> --%>
                    </div>

                    <div class="form-group">
                    	<label class="control-label">Card Value</label>
                    	<form:input path="cardvalue" class="form-control" type="text" onKeyPress="return checkIt(event)" id="cardvalue" maxlength="7"/>
                    </div>	              

                    <div class="form-group">
                    	<label class="control-label">No of Cards</label>
                    	<form:input path="noofcard" class="form-control" type="text" onKeyPress="return checkIt(event)" id="noofcard" maxlength="7"/> 
                    </div>	                 

                	<div class="form-actions">
						<button type="button" class="btn btn-primary" onclick="onVerify()">Generate Cards</button>
						<button type="reset" class="btn" >Reset</button>
					</div>		

        </div>
</form:form>      
        
         
        

</div>
