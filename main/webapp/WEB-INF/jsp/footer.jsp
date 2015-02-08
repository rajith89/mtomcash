<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${param.noCopyright != true}">
    <%--<div region="south" border="false" class="screen-toolbar"
         style="padding: 5px; height: 25px; font-size: 11px; border-bottom: none;text-align: right;">
        <fmt:formatNumber value="<%=Calendar.getInstance().get(Calendar.YEAR)%>" var="formattedYear"
                          groupingUsed="false"/>
        <spring:message code="label.copyright" arguments="${formattedYear}"/>${noCopyright}
    </div>--%>
</c:if>
</body>
</html>