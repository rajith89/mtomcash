<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%-- <spring:message code="label.conductor" var="entity"/> --%>
<%-- <spring:message code="label.entity.listing.title" arguments="${entity}" var="pageTitle"/> --%>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="${pageTitle}"/>
</jsp:include>

<jsp:include page="list-scripts.jsp"/>

<div region="north" border="false" class="screen-toolbar">
   
    <jsp:include page="currencycode-tools.jsp">
        <jsp:param name="add"  value="true"/>
        <jsp:param name="edit" value="true"/>
    </jsp:include>
</div>

<div region="center" border="false" class="screen-content">
    <table id="grid" class="easyui-datagrid active-bg"
           url="list.json" pageList="[25,50,100,200]"
           fit="true" fitColumns="true" pagination="true" data-options="singleSelect:true">
        <thead>
        <tr>
            <th width="30" checkbox="true"></th>
            <th field="currencyCode" width="100">Code</th>
            <th field="currencyDesc" width="100">Description</th>
            
        </tr>
        </thead>
    </table>
</div>
<jsp:include page="footer.jsp"/>