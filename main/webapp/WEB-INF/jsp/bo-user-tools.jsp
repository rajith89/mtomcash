<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<a class="easyui-linkbutton" iconCls="icon-add" plain="true"
	<c:if test="${param.add != true}"> disabled="true" </c:if>
	onclick="onAdd()" href="add">Add</a>

<a class="easyui-linkbutton" iconCls="icon-edit" plain="true"
	<c:if test="${param.edit != true}"> disabled="true" </c:if>
	onclick="onEditExtended()" href="#">Edit</a>
	
<a class="easyui-linkbutton" iconCls="icon-activate" plain="true"
	<c:if test="${param.add != true}"> disabled="true" </c:if>
	onclick="onActivate()" href="#">Activate</a>

<a class="easyui-linkbutton" iconCls="icon-deactivate" plain="true"
	<c:if test="${param.add != true}"> disabled="true" </c:if>
	onclick="onDeactivate()" href="#">Deactivate</a>