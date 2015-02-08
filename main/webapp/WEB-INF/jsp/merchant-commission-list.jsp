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
           url="list-merchant-commision.json" pageList="[25,50,100,200]"
           fit="true" fitColumns="true" pagination="true" data-options="singleSelect:true">
        <thead>
                
        <tr>
            <th field="id" hidden="true" width="100">Id</th>
            <th field="merchant" width="100">Merchant</th>
            <th field="amountslab" width="100">Amount SLab</th>
            <th field="amount" width="100">Amount</th>
            <th field="dateupdated" width="100">Date Updated</th>
            <th field="status" width="100">Status</th>
            
            
             
            
        </tr>
        </thead>
    </table>
</div>
<jsp:include page="footer.jsp"/>