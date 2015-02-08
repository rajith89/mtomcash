<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="header-form.jsp">
	<jsp:param name="title" value="Merchant" />
</jsp:include>
<script>
	function onCancel() {
		loadURL('list');
	}
	
	

	$(function() {
		$('#frmEdit')
				.form(
						{
							onSubmit : function() {

								var screenmode = "${screenMode}";

								if (screenmode === "add") {
									var username = document
											.getElementById('username').value;
									var password = document
											.getElementById('password').value;
									var passwordConfirm = document
											.getElementById('passconfirm').value;
									
									var passwordPin = document
											.getElementById('passwordPin').value;
							
									var passwordPinConfirm = document
											.getElementById('passPinconfirm').value;
									
									if (!username || !password || !passwordPin) {
										return;
									}
									if (password != passwordConfirm) {
										return;
									}
									if (passwordPin != passwordPinConfirm) {
										return;
									}
									
									
									
								}
								

								var fname = document.getElementById('fname').value;
								var lname = document.getElementById('lname').value;
								var tel = document.getElementById('tel').value;

								if (!fname || !lname || !tel) {
									return;
								}
								
								var email = document
									.getElementById('email').value;
							
																
								if(!validateEmail(email)){
									return;
								}

								var roleIds = "";
								var roleIdNames = "";

								var sel = $("#grid").datagrid('getSelected');
								if (!sel) {
									showAlert("No role has selected");
									return;
								}

								var selRoleArr = $("#grid").datagrid(
										'getSelections');

								for (var i = 0; i < selRoleArr.length; i++) {
									var rec = selRoleArr[i];
									roleIds += rec.id + ",";
									roleIdNames += rec.name + ",";
								}

								var str = $("#frmEdit").serialize();

								var validateOk = $(this).form('validate');

								//alert(validateOk);
								if (validateOk) {
									$.ajax({
										url : "save-" + roleIds
												+ "-" + roleIdNames,
										data : str,
										type : 'POST',
										dataType : 'json',
										success : function(saveResponse) {
											var message = getSaveErrorMessage(
													saveResponse, "", "");
											if (message) {
												hideProgress();
												showWarning(message);
											} else {
												loadURL("list");
												return false;
											}
										}
									});
									showProgress();
									return true;
								}
								return false;
							}
						});

		$(function() {
			$('#grid').datagrid({
				onLoadSuccess : function(data) {
					var roleIds = '${rolesusers}';
					var ids_1 = roleIds.replace("[", "");
					var ids_2 = ids_1.replace("]", "");
					var ids = ids_2.split(",");
					var rows = $('#grid').datagrid('getRows');

					for (i = 0; i < rows.length; ++i) {
						for (j = 0; j < ids.length; ++j) {
							if (rows[i]['id'] == ids[j])
								$('#grid').datagrid('checkRow', i);
						}
					}
				}
			});
		});
		$(function() {
			$("#frmEdit").validate({
				rules : {
					passconfirm : {
						equalTo : "#password"
					},
					passPinconfirm : {
						equalTo : "#passwordPin"
					}
				}
			});
		});
	});
	
	 
    function checkIt(evt) {
        evt = (evt) ? evt : window.event
        var charCode = (evt.which) ? evt.which : evt.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            status = "This field accepts numbers only."
            return false;
        }
        status = "";
        return true;
    }
    
	function validateEmail(email) {
		
		var reg = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
		if (reg.test(email)){
			
	 		return true;
	 	}
		else{
			
	 		return false;
	 	}
	} 
    
</script>
<form:form id="frmEdit" method="post" modelAttribute="bouser"
	style="width:100%">
	<table width="80%" border="0">
		<tr>
			<td width="50%" style="vertical-align: top;"><form:hidden
					path="id" /> <c:if test="${screenMode == 'edit'}">
					<form:hidden path="userName" />
					<form:hidden path="passwordEnc" />
					<form:hidden path="createdDate" />
					<form:hidden path="passwordPinEnc" />
				</c:if> <c:if test="${screenMode == 'edit'}">
					<form:hidden path="lastUpdateDate" />
				</c:if> <form:hidden path="createdUser" /> <form:hidden path="status" />
				<c:if test="${screenMode == 'add'}">
					<div class="form-group">
						<label class="control-label">User Name</label>
						<form:input path="userName" class="form-control" type="text"
							id="username" required="true" size="40" />
					</div>

					<div class="form-group">
						<label class="control-label">Password</label>
						<form:input path="passwordEnc" class="form-control"
							type="password" id="password" name="password" required="true"
							size="40" />
					</div>
					<div class="form-group">
						<label class="control-label">Confirm Password</label>
						<form:input path="" class="form-control" type="password"
							id="passconfirm" name="passconfirm" size="40" />
					</div>
					
					<div class="form-group">
						<label class="control-label">Password Pin</label>
						<form:input path="passwordPinEnc" class="form-control"
							type="password" id="passwordPin" name="passwordPin" required="true"
							size="40" />
					</div>

					<div class="form-group">
						<label class="control-label">Confirm Password Pin</label>
						<form:input path="" class="form-control" type="password"
							id="passPinconfirm" name="passPinconfirm" required="true" size="40" />
					</div>
				</c:if>
				<div class="form-group">
					<label class="control-label">First Name</label>
					<form:input path="firstName" class="form-control" type="text"
						id="fname" required="true" size="40" />
				</div>

				<div class="form-group">
					<label class="control-label">Last Name</label>
					<form:input path="lastName" class="form-control" type="text"
						id="lname" required="true" size="40" />
				</div>

				<div class="form-group">
					<label class="control-label">Email</label>
					<form:input path="email" class="form-control" type="email"
						id="email" required="true" size="40" />
				</div>
				
				<div class="form-group">
					<label class="control-label">Telephone</label>
					<form:input path="telephone" class="form-control" type="text"
						id="tel" required="true" size="40" onKeyPress="return checkIt(event)"/>
				</div>

				<div class="form-actions">
					<button type="submit" class="btn btn-primary">Save changes</button>
					<button type="button" class="btn" onclick="onCancel()">Cancel</button>
				</div></td>
			<td style="vertical-align: top;">
				<table border="0" width="100%">
					<tr>
						<td><label class="control-label">User Roles</label></td>
					</tr>
					<tr>
						<td height="160">
							<table id="grid" class="easyui-datagrid active-bg"
								url="boRoles.json" fit="true" fitColumns="true"
								showHeader="false" showFooter="false" width="100%">
								<thead>
									<tr>
										<th width="30" checkbox="true"></th>
										<th:hidden field="id" width="100" />
										<th field="name" width="100"></th>
									</tr>
								</thead>
							</table>
						</td>
					</tr>
				</table>

			</td>
		</tr>
	</table>
</form:form>


