package cc.mrbird.febs.common.dto;

/**
 * JsonObjectPage 通过条件查询返回实体类
 *
 * @param <E>
 */
public class JsonObjectPage<E> extends JsonPage {

    private E data;
    private Boolean suspend;

    public static <E> JsonObjectPage<E> createJsonObjectPage(E data) {
        JsonObjectPage<E> jp = new JsonObjectPage<E>();
        jp.setSuccess(true);
        jp.setData(data);
        return jp;
    }

    public static <E> JsonObjectPage<E> createJsonObjectPage(E data, Boolean suspend) {
        JsonObjectPage<E> jp = new JsonObjectPage<E>();
        jp.setSuccess(true);
        jp.setData(data);
        jp.setSuspend(suspend);
        return jp;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Boolean getSuspend() {
        return suspend;
    }

    public void setSuspend(Boolean suspend) {
        this.suspend = suspend;
    }
}

