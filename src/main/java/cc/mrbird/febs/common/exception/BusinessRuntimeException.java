package cc.mrbird.febs.common.exception;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/4/4 14:58
 * @since 0.1.0
 */
public class BusinessRuntimeException extends RuntimeException{

    private String msg;
    private String code;

    public BusinessRuntimeException(String msg){
        super(msg);
        this.msg = msg;
    }

    public BusinessRuntimeException(String msg, String code){
        super(msg);
        this.msg = msg;
        this.msg = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

