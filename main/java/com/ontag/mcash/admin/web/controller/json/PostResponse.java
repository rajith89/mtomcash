
package com.ontag.mcash.admin.web.controller.json;

import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.List;

public class PostResponse {
    private boolean hasErrors;
    private String[] errorList;

    public PostResponse() {

    }

    public PostResponse(List<ObjectError> allErrors) {
        setErrors(allErrors);
    }

    public void setErrors(String[] errors) {
        this.errorList = errors;

        if (errors.length > 0) {
            hasErrors = true;
        }
    }

    public void setErrors(List<ObjectError> allErrors) {
        if (!allErrors.isEmpty()) {

            errorList = new String[allErrors.size()];
            int c = 0;
            for (ObjectError allError : allErrors) {
                errorList[c++] = allError.getDefaultMessage();
            }

            hasErrors = true;
        }
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public String[] getErrorList() {
        return errorList;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "hasErrors=" + hasErrors +
                ", errorList=" + Arrays.toString(errorList) +
                '}';
    }
}
