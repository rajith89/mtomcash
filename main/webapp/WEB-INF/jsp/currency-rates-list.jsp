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
   
    <jsp:include page="currency-rates-tools.jsp">
        <jsp:param name="add"  value="true"/>
    </jsp:include>
</div>

<div region="center" border="false" class="screen-content">
    <table id="grid" class="easyui-datagrid active-bg"
           url="list.json" pageList="[25,50,100,200]"
           fit="true" fitColumns="true" pagination="true" data-options="singleSelect:true">
        <thead>
        <tr>
            <th field="bankCurrencyCodeId" width="100">Corporate Bank</th>
            <th field="currencyCode" width="100">CurrencyCode</th>
            <th field="ttBuying" width="100">T/T Buying</th>
            <th field="odBuying" width="100">O/D Buying</th>
            <th field="ttSelling" width="100">T/T Selling</th>
            <th field="amount" width="100">HS Rate</th>
            <th field="lastUpdateDate" width="120">Date Updated</th>
            <th field="status" width="100">Status</th>
     
        </tr>
        </thead>
    </table>
</div>
<jsp:include page="footer.jsp"/>