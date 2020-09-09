package cc.mrbird.febs.common.dto;

import java.io.Serializable;

public enum ResponseStatusEnum implements Serializable {

    SUCCESS(1, "成功", "操作成功"),
    FAILED(-1, "失败", "操作失败"),
    VALID_ERROR(1000, "校验失败", "数据校验失败"),
    NOT_FOUND(404, "未找到", null),
    TOKEN_EXPRIE(9999, "token过期", null);

    Integer code;
    String message;
    String details;

    ResponseStatusEnum(final Integer code, final String message, final String details){
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public Integer getCode(){
        return code;
    }

    public void setCode(final Integer code){
        this.code = code;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(final String message){
        this.message = message;
    }

    public String getDetails(){
        return details;
    }

    public void setDetails(final String details){
        this.details = details;
    }
}
