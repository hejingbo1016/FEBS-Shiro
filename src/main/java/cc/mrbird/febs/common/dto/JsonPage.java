package cc.mrbird.febs.common.dto;

import java.util.Map;

/**
 * JsonPage 保存/修改返回结果json
 */
public class JsonPage {

    private String msg;
    private boolean success;
    private Map<String, Object> extParams;

    public static JsonPage createSuccess() {
        JsonPage jp = new JsonPage();
        jp.setSuccess(true);
        return jp;
    }

    public static JsonPage createSuccess(Map<String, Object> extParams) {
        JsonPage jp = new JsonPage();
        jp.setExtParams(extParams);
        jp.setSuccess(true);
        return jp;
    }

    public static JsonPage createErrorMsg(String msg) {
        JsonPage jp = new JsonPage();
        jp.setMsg(msg);
        return jp;
    }

    public static JsonPage createSuccessMsg(String msg) {
        JsonPage jp = new JsonPage();
        jp.setMsg(msg);
        jp.setSuccess(true);
        return jp;
    }

    public static JsonPage createSuccessMsg(String msg, String type) {
        JsonPage jp = new JsonPage();
        jp.setMsg(msg);
        jp.setSuccess(true);
        return jp;
    }

    public static JsonPage createMsg(boolean success, String msg) {
        JsonPage jp = new JsonPage();
        jp.setMsg(msg);
        jp.setSuccess(success);
        return jp;
    }

    public static JsonPage createSuccess(Boolean success) {
        JsonPage jp = new JsonPage();
        jp.setSuccess(success);
        return jp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, Object> extParams) {
        this.extParams = extParams;
    }

    @Override
    public String toString() {
        return "JsonPage [msg=" + msg + ", success=" + success + ", extParams=" + extParams + "]";
    }
}

