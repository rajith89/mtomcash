<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%-- <spring:message code="label.conductor" var="entity"/> --%>
<%-- <spring:message code="label.entity.listing.title" arguments="${entity}" var="pageTitle"/> --%>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="${pageTitle}"/>
</jsp:include>

<jsp:include page="list-scripts.jsp"/>


<div region="center" border="false" class="screen-content">
    <table id="grid" class="easyui-datagrid active-bg"
           url="merchant-balance-list.json" pageList="[25,50,100,200]"
           fit="true" fitColumns="true" pagination="true"
           data-options="singleSelect:true">
        <thead>
        <tr>
            <th field="firstName" width="100">First Name</th>
            <th field="lastName" width="100">Last Name</th>
            <th field="telephone" width="100">Telephone</th>
            <th field="merchantbalance" width="100">Merchant Balance</th>    
            <th field="submerchantbalance" width="100">Sub Merchant Balance</th>     
            <th field="totalbalance" width="100">Total Balance</th>       
        </tr>
        </thead>
    </table>
</div>
<jsp:include page="footer.jsp"/>