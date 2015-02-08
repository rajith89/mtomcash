<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<script type="text/javascript">
    function onList() {
        loadURL("list");
    }
    function onAdd() {
        loadURL("add");
    }
    function onEdit() {
        var selArr = $('#grid').datagrid('getSelections');
        if (selArr.length > 1) {
            showAlert("Only one record can be edited");
            return;
        }
        var sel = $('#grid').datagrid('getSelected');
        if (sel) {
            loadURL("edit-" + sel.id);
        } else {
            showAlert("No record has selected");
        }
    }
    function onDelete() {
        var selArr = $('#grid').datagrid('getSelections');

    }
    
    function onSearch() {
        loadURL("searchPage");
    }
    function onUpload() {
        loadURL("uploadPage");
    }


</script>