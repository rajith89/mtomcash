<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%-- <spring:message code="label.conductor" var="entity"/> --%>
<%-- <spring:message code="label.entity.listing.title" arguments="${entity}" var="pageTitle"/> --%>


<script>
	function onEditExtended() {

		//alert("Edit....");

		var selArr = $('#grid').datagrid('getSelections');
		if (selArr.length > 1) {
			showAlert("Only one record can be edited");
			return;
		}
		var sel = $('#grid').datagrid('getSelected');
		if (sel.editable === 1) {
			showAlert("This record cannot be edited");
			return;
		}

		var sel = $('#grid').datagrid('getSelected');
		if (sel) {
			loadURL("edit-" + sel.id);
		} else {
			showAlert("No record has selected");
		}
	}

	function onActivate() {
		var selId = 1;
		var batchids = "";

		var sel = $('#grid').datagrid('getSelected');
		if (!sel) {
			showAlert("No record has selected");
			return;
		}

		var sel = $('#grid').datagrid('getSelected');
		if (sel.editable === 1) {
			showAlert("This record cannot be edited");
			return;
		}
		
		var selbatchArr = $('#grid').datagrid('getSelections');

		for (var i = 0; i < selbatchArr.length; i++) {
			var rec = selbatchArr[i];
			if (rec.status === "Active") //Check Active status
			{
				showAlert("You have selected already a activated user.");
				return;
			}
			batchids += rec.id + ",";
		}

		if (selId) {
			var x = confirm("Confirm to activate user ?");
			if (!x) {
				return false;
			}

			showProgress();
			$.ajax({
				url : 'activate-' + batchids,
				type : 'POST',
				dataType : 'json',
				success : function(saveResponse) {
					var message = getSaveErrorMessage(saveResponse,
							"Error Occured", "Error Occured");
					if (message) {
						hideProgress();
						return false;
					} else {
						hideProgress();
						alert('User successfully activated.');
						loadURL("list");
						return false;
					}

				},
				error : function(err) {
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

		var sel = $('#grid').datagrid('getSelected');
		if (sel.editable === 1) {
			showAlert("This record cannot be edited");
			return;
		}
		
		var selbatchArr = $('#grid').datagrid('getSelections');

		for (var i = 0; i < selbatchArr.length; i++) {
			var rec = selbatchArr[i];
			if (rec.status === "Inactive") //Check Deactive status
			{
				showAlert("You have selected already a deactivated user.");
				return;
			}
			batchids += rec.id + ",";
		}

		if (selId) {
			var x = confirm("Confirm to deactivate user ?");
			if (!x) {
				return false;
			}

			showProgress();
			$.ajax({
				url : 'deactivate-' + batchids,
				type : 'POST',
				dataType : 'json',
				success : function(saveResponse) {
					var message = getSaveErrorMessage(saveResponse,
							"Error Occured", "Error Occured");
					if (message) {
						hideProgress();
						return false;
					} else {
						hideProgress();
						alert('User successfully deactivated.');
						loadURL("list");
						return false;
					}

				},
				error : function(err) {
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
	<jsp:param name="title" value="${pageTitle}" />
</jsp:include>

<jsp:include page="list-scripts.jsp" />

<div region="north" border="false" class="screen-toolbar">

	<jsp:include page="bo-user-tools.jsp">
		<jsp:param name="add" value="true" />
		<jsp:param name="edit" value="true" />
	</jsp:include>
</div>

<div region="center" border="false" class="screen-content">
	<table id="grid" class="easyui-datagrid active-bg" url="list.json"
		pageList="[25,50,100,200]" fit="true" fitColumns="true"
		pagination="true" data-options="singleSelect:true">
		<thead>
			<tr>
				<th width="30" checkbox="true"></th>
				<th field="userName" width="100">User Name</th>
				<th field="firstName" width="100">First Name</th>
				<th field="lastName" width="100">Last Name</th>
				<th field="telephone" width="100">Telephone</th>
				<th field="status" width="100">Status</th>
			</tr>
		</thead>
	</table>
</div>
<jsp:include page="footer.jsp" />