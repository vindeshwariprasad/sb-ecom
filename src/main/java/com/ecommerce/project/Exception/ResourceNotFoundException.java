package com.ecommerce.project.Exception;

public class ResourceNotFoundException extends RuntimeException {
    String recoucename;
    String filed;
    String fieldname;
    Long fieldId;



    public ResourceNotFoundException(String recoucename, String filed, String fieldname) {
        super(String.format("%s not found with %s:%s", recoucename, filed,fieldname));
        this.recoucename = recoucename;
        this.filed = filed;
        this.fieldname = fieldname;
    }

    public ResourceNotFoundException(String recoucename, String filed, Long fieldId) {
//        super(message, cause, enableSuppression, writableStackTrace);
        super(String.format("%s not found with %s:%d", recoucename, filed,fieldId));
        this.recoucename = recoucename;
        this.filed = filed;
        this.fieldId = fieldId;
    }


}
