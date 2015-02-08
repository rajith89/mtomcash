function loadURL(url, frm) {
    if (frm) {

        if (frm.showProgress) {
            frm.showProgress();
        }

        frm.location.href = url;
    } else {

        //showProgress();
        window.location.href = url;
    }
}
function showProgress(text, title) {
    if (!text) {
        text = "Processing, Please wait ...";
    }
    if (!title) {
        title = "MtoMCash";
    }
    $.messager.progress({
        title:title,
        msg:text
    });
}
function hideProgress() {
    $.messager.progress('close');
}
function showHTTPError(err, fn, title) {
    showWarning("Problem with server call.<br>Please try again.<br>Technical details: " + err.status + ':' + err.statusText);
}
function showWarning(msg, fn, title) {
    if (!title) {
        title = "MtoMCash";
    }
    if (!msg) {
        msg = "{{Please enter message}}";
    }

    $.messager.alert(title, msg, 'warning', fn);
}
function showAlert(msg, fn, title) {
    if (!title) {
        title = "MtoMCash";
    }
    if (!msg) {
        msg = "{{Please enter message}}";
    }

    $.messager.alert(title, msg, "info", fn);
}
function showConfirmDialog(msg, fn, title) {
    if (!title) {
        title = "MtoMCash";
    }
    if (!msg) {
        msg = "{{Please enter message}}";
    }

    $.messager.confirm(title, msg, fn);
}
/**
 * Returns the save errors an an Array
 * @param saveResponse
 */
function getSaveErrors(saveResponse) {

    var dataJSON;
    if (typeof saveResponse == 'object') {
        dataJSON = saveResponse;
    } else {
        dataJSON = jQuery.parseJSON(saveResponse);
    }

    var errors = [];
    if (!dataJSON.hasErrors) {
        return errors;
    }
    $.each(dataJSON.errorList, function (index, err) {
            errors[index] = err;
        }
    );
    return errors;
}
/**
 * Returns a formatted save error message to display in a Alert.
 * Returns null if no errors exist
 *
 * @param saveResponse
 * @param header
 * @param footer
 */
//function getSaveErrorMessage(saveResponse, header, footer) {
//
//    var errors = getSaveErrors(saveResponse);
//    if (errors.length == 0) {
//        return null;
//    }
//    var message = "<div class='save-errors'>" + header + "<ul>";
//    $.each(errors, function (index, err) {
//            if (err) {
//                message += "<li>" + err + "</li>";
//            }
//        }
//    );
//    message += "</ul>" + footer + "</div>";
//
//    return message;
//}

function getSaveErrorMessage(saveResponse, header, footer) {

    var errors = getSaveErrors(saveResponse);
    if (errors.length == 0) {
        return null;
    }
    var message = "";
    $.each(errors, function (index, err) {
            if (err) {
                message += err;
            }
        }
    );
    
    return message;
}
function dateStr(date) {
    var d = date.getDate();
    var m = date.getMonth() + 1;
    var y = date.getFullYear();
    return '' + y + '/' + (m <= 9 ? '0' + m : m) + '/' + (d <= 9 ? '0' + d : d);
}

$.fn.datebox.defaults.formatter= dateStr;

function makeRequired(selector) {
    $(esc(selector)).validatebox({
        required:true

    });
    $(esc(selector)).attr("required", true);
}

function makeEmail(selector) {
    $(esc(selector)).validatebox({
        rules: {
            field: {
                required: true,
                email: true
            }
        }


    });
    $(esc(selector)).attr("email", true);
}


function makeOptional(selector) {
    $(esc(selector)).validatebox({
        required:false
    });
    $(esc(selector)).attr("required", false);
}
function makeDateTime(selector) {
    //$(selector).css("width","90px !important;")
    $(esc(selector)).datetimebox({
        showSeconds:false
    });
}

function makeDate(selector) {
    //$(selector).css("width","90px !important;")
    $(esc(selector)).datebox({
    });
}


function makeNumeric(selector, precision) {
    var p = 2;
    if(precision){
        p = precision;
    }
    $(esc(selector)).numberbox({
        min:0,
        precision:p
    });
}

function makeInteger(selector) {


    $(esc(selector)).numberbox({
        min:0

    });
}

//escapes an id having invalid chars
function esc(myid) {
    return myid.replace(/(:|\.)/g, '\\$1');
}

function isValidDateTime(s) {
    // format D(D)/M(M)/(YY)YY
    var dateFormat = /^\d{1,4}[/]\d{1,2}[/]\d{1,2}[ ]\d{1,2}[:]\d{1,2}[:]\d{1,2}$/;

    if (dateFormat.test(s)) {
        // remove any leading zeros from date values
        //s = s.replace(/0*(\d*)/gi,"$1");
        var dateArray = s.split(/[\.|\/|: -]/);

        // correct month value
        dateArray[1] = dateArray[1] - 1;
        // correct year value

        var testDate = new Date(dateArray[0], dateArray[1], dateArray[2], dateArray[3], dateArray[4], dateArray[5]);
        if (testDate.getDate() != dateArray[2] || testDate.getMonth() != dateArray[1] || testDate.getFullYear() != dateArray[0] || testDate.getHours() != dateArray[3]
            || testDate.getMinutes() != dateArray[4] || testDate.getSeconds() != dateArray[5]) {

            return false;
        } else {

            return true;
        }
    } else {
        return false;
    }

}
