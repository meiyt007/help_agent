package com.zfsoft.microservice.platform.gateway.response;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/20 15:38
 */
public class WsResponse<T> {

    private MessageCode status;
    private String message;
    private T result;

    public WsResponse() {
        message = "";
    }

    public WsResponse(MessageCode status, T result) {
        message = "";
        this.status = status;
        this.result = result;
    }

    public MessageCode getStatus() {
        return status;
    }

    public void setStatus(MessageCode status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "error code:" + status + " result:" + result;
    }

    public static WsResponse failure(String msg) {
        WsResponse resp = new WsResponse();
        resp.status = MessageCode.COMMON_FAILURE;
        resp.setMessage(msg);
        return resp;
    }

    public static WsResponse failure(MessageCode messageCode) {
        WsResponse resp = new WsResponse();
        resp.status = messageCode;
        resp.setMessage(messageCode.getMsg());
        return resp;
    }

    public static WsResponse failure(MessageCode messageCode, String message) {
        WsResponse resp = new WsResponse();
        resp.status = messageCode;
        if (StringUtils.isNotBlank(messageCode.getMsg())) {
            resp.setMessage(messageCode.getMsg());
        }
        if (StringUtils.isNotBlank(message)) {
            resp.setMessage(message);
        }
        return resp;
    }

    public static WsResponse success() {
        WsResponse resp = new WsResponse();
        resp.status = MessageCode.COMMON_SUCCESS;
        resp.setMessage(MessageCode.COMMON_SUCCESS.getMsg());
        return resp;
    }

    public static <K> WsResponse<K> success(K t) {
        WsResponse<K> resp = new WsResponse<>();
        resp.status = MessageCode.COMMON_SUCCESS;
        resp.setMessage(MessageCode.COMMON_SUCCESS.getMsg());
        resp.result = t;

        return resp;
    }

    /**
     * 判断字符串是否已经是 WsResponse返回格式
     *
     * @param json
     * @return
     */
    public static boolean isWsResponseJson(String json) {
        if (json != null && json.indexOf("\"status\":") != -1
                && json.indexOf("\"message\":") != -1
                && json.indexOf("\"result\":") != -1) {
            return true;
        } else {
            return false;
        }
    }
}
