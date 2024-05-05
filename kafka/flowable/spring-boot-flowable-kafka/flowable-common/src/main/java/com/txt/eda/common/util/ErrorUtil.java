package com.txt.eda.common.util;

import com.txt.eda.common.constant.Constant;
import com.txt.eda.common.dto.common.ResponseCode;
import com.txt.eda.common.dto.common.*;
import com.txt.eda.common.dto.common.Error;
import org.springframework.http.HttpStatus;

import java.util.*;

public class ErrorUtil {

    public static ObjError createObjectError(String code, String description) {
        ObjError objError = new ObjError();
        objError.setErrorCode(code);
        objError.setErrorDescription(description);
        objError.setErrorTime(DateUtil.dateToString(new Date(), DateUtil.YYYYMMDDHHMMSS));
        return objError;
    }

    public static ObjError createObjectErrorDes(ResponseCode errorCode) {
        ObjError objError = new ObjError();
        objError.setErrorCode(errorCode.getCode());
        objError.setErrorDescription(errorCode.getDescription());
        objError.setErrorTime(DateUtil.dateToString(new Date(), DateUtil.YYYYMMDDHHMMSS));
        return objError;
    }

    public static ObjErrorList createObjectErrorListDes(ResponseCode errorCode, String fieldName) {
        ObjErrorList objErrorList = new ObjErrorList();
        objErrorList.setErrorCode(errorCode.getCode());
        objErrorList.setErrorDescription(errorCode.getDescription());
        objErrorList.setErrorField(fieldName);
        return objErrorList;
    }

    public static ObjErrorList createObjectErrorListDes(String errorCode, String description, String fieldName) {
        ObjErrorList objErrorList = new ObjErrorList();
        objErrorList.setErrorCode(errorCode);
        objErrorList.setErrorDescription(description);
        objErrorList.setErrorField(fieldName);
        return objErrorList;
    }

    public static ObjResult creatErrorResult(ResponseCode errorCode, String exchangeId) {
        ObjResult objResult = new ObjResult();
        objResult.setExchangeId(exchangeId);
        objResult.setStatus(ResponseCode.FAILED.getDescription());
        objResult.setOErrorResult(createObjectErrorDes(errorCode));
        return objResult;
    }

    public static ResponseStatus createResponseStatus(HttpStatus httpStatus) {
        ResponseStatus status = new ResponseStatus();
        status.setCode(httpStatus.value());
        status.setMessage(httpStatus.getReasonPhrase());
        return status;
    }

    public static ResponseStatus createResponseStatus(HttpStatus httpStatus, List<Error> errors) {
        ResponseStatus status = new ResponseStatus();
        status.setCode(httpStatus.value());
        status.setMessage(httpStatus.getReasonPhrase());
        status.setErrors(errors);
        return status;
    }

    public static ResponseStatus createResponseStatus() {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setMessage(Constant.API_MSG_FAILED);
        responseStatus.setCode(Constant.API_CODE_FAILED);
        return responseStatus;

    }

}
