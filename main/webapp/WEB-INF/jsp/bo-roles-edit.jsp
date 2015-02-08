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
		$('#frmEdit').form(
				{
					url : 'save',
					onSubmit : function() {
						var screenmode = "${screenMode}";
						
						var permissionIds = "";

						if (screenmode === "edit") {
							var sel = $("#grid").datagrid('getSelected');
							if (!sel) {
								showAlert("No permission has selected");
								return;
							}
						
						
							var selPermissionsArr = $("#grid").datagrid(
									'getSelections');
	
							for (var i = 0; i < selPermissionsArr.length; i++) {
								var rec = selPermissionsArr[i];
								permissionIds += rec.id + ",";
							}
						}
						var str = $("#frmEdit").serialize();
						var validateOk = $(this).form('validate');

						if (validateOk) {
							$.ajax({
								url : "save-" + permissionIds,
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
					var permissionIds = '${permmissionIds}';
					var ids_1 = permissionIds.replace("[", "");
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
	});
</script>

<form:form id="frmEdit" method="post" modelAttribute="boroles"
	style="width:100%">
	<table width="80%" border="0">
		<tr>
			<td width="50%" style="vertical-align: top;">
			<form:hidden path="id" />

				<div class="form-group">
					<label class="control-label">Role Name</label>
					<form:input path="name" class="form-control" type="text"
						required="true" size="40" />
				</div>

				<div class="form-actions">
					<button type="submit" class="btn btn-primary">Save changes</button>
					<button type="button" class="btn" onclick="onCancel()">Cancel</button>
				</div>
				</td>
			<td style="vertical-align: top;">
			<c:if test="${screenMode == 'edit'}">
					<table border="0" width="100%">
						<tr>
							<td><label class="control-label">Role Permissions</label></td>
						</tr>
						<tr>
							<td height="670">
								<table id="grid" class="easyui-datagrid active-bg"
									url="permissions.json" fit="true" fitColumns="true"
									showHeader="false" showFooter="false" width="100%">
									<thead>
										<tr>
											<th width="30" checkbox="true"></th>
											<th:hidden field="id" width="100" />
											<th field="displayName" width="100"></th>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
					</table>
				</c:if>
			</td>
		</tr>
	</table>
</form:form>
