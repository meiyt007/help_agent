package com.zfsoft.ocr.util;


import com.alibaba.fastjson.JSON;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenResponse;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;


/**
 * 百度印刷体识别
 *
 * @author cbc
 * @date 2018-3-14 15:25:55
 */
public class BaiduOcrUtil {

    /**
     * 百度接口访问地址
     */
    public static final String BAI_DU_URL = "https://aip.baidubce.com/";

    /**
     * 百度获取token地址
     */
    public static final String BAI_DU_URL_TOKEN_URL = BAI_DU_URL + "oauth/2.0/token?";

    /**
     * 百度OCR接口地址
     */
    public static final String HOST_URL_OCR = BAI_DU_URL + "rest/2.0/ocr/v1/";

    /**
     * 百度OCR接口地址，通用文字识别（高精度版）:含位置信息-0
     */
    public static final String HOST_URL_OCR_HOST_HIGH_POS = HOST_URL_OCR + "accurate";

    /**
     * 百度OCR接口地址，通用文字识别（含生僻字版）-1
     */
    public static final String HOST_URL_OCR_HOST_UNCOMMON = HOST_URL_OCR + "general_enhanced";

    /**
     * 百度OCR接口地址，通用文字识别（高精度版）-2
     */
    public static final String HOST_URL_OCR_HOST_HIGH = HOST_URL_OCR + "accurate_basic";

    /**
     * 百度OCR接口地址，通用文字识别:含位置信息-3
     */
    public static final String HOST_URL_OCR_HOST_COMMON_POS = HOST_URL_OCR + "general";

    /**
     * 百度OCR接口地址，营业执照识别
     */
    public static final String HOST_URL_OCR_HOST_BUSINESS_LICENSE = HOST_URL_OCR + "business_license";

    /**
     * 百度OCR接口地址，身份证、临时身份证
     */
    public static final String HOST_URL_OCR_HOST_IDCARD = HOST_URL_OCR + "idcard";


    /**
     * 百度OCR接口地址，混贴
     */
    public static final String HOST_URL_OCR_HOST_MULTI_IDCARD = HOST_URL_OCR + "multi_idcard";

    /**
     * 百度OCR接口地址，身份证、临时身份证
     */
    public static final String HOST_URL_OCR_HOST_HOUSEHOLD_REGISTER = HOST_URL_OCR + "household_register";

    /**
     * 百度OCR接口地址，银行卡
     */
    public static final String HOST_URL_OCR_HOST_BANK_CARD = HOST_URL_OCR + "bankcard";

    /**
     * 百度OCR接口地址，名片识别
     */
    public static final String HOST_URL_OCR_HOST_BUSINESS_CARD = HOST_URL_OCR + "business_card";

    /**
     * 百度OCR接口地址，护照识别
     */
    public static final String HOST_URL_OCR_HOST_PASSPORT = HOST_URL_OCR + "passport";
    /**
     * 百度OCR接口地址，社保卡识别
     */
    public static final String HOST_URL_OCR_HOST_SOCIAL_SECURITY_CARD = HOST_URL_OCR + "social_security_card";
    /**
     * 百度OCR接口地址，港澳通行证
     */
    public static final String HOST_URL_OCR_HOST_HK_MACAU_EXITENTEYPERMIT = HOST_URL_OCR + "HK_Macau_exitentrypermit";

    /**
     * 百度OCR接口地址，台湾通行证
     */
    public static final String HOST_URL_OCR_HOST_TAIWAN_EXITENTEYPERMIT = HOST_URL_OCR + "taiwan_exitentrypermit";
    /**
     * 百度OCR接口地址，出生医学证明
     */
    public static final String HOST_URL_OCR_HOST_BIRTH_CERTIFICATE = HOST_URL_OCR + "birth_certificate";
    /**
     * 百度OCR接口地址，港澳通行证
     */
    public static final String HOST_URL_OCR_HOST_MULTI_CARD_CLASSIFY = HOST_URL_OCR + "multi_card_classify";


    /**
     * 百度OCR接口地址，自定义模板识别 & 图片分类
     */
    public static final String HOST_URL_OCR_HOST_CUSTOM_TEMPLATE = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";

    /**
     * 百度OCR接口地址，获取token固定参数
     */
    public static final String HOST_URL_OCR_TOKEN_GRANT_TYPE = "client_credentials";

    /**
     * 百度OCR接口地址，获取token
     */
    public static final String HOST_URL_OCR_ACCESS_TOKEN = "access_token";

    /**
     * 百度OCR接口地址，获取识别信息错误提示
     */
    public static final String BAI_DU_URL_ERROR_CODE = "error_code";

    /**
     * 身份证-头像面
     */
    public static final String IDCARD_FRONT = "front";

    /**
     * 身份证-国徽面
     */
    public static final String IDCARD_BACK = "back";


    /**
     * 百度OCR接口,唯一的log id，用于问题定位
     */
    public static final String BAI_DU_LOG_ID = "log_id";

    /**
     * 百度OCR接口地址，获取识别信息
     */
    public static final String BAI_DU_URL_WORDS_RESULT = "words_result";

    /**
     * 百度自定义模板成功CODE
     */
    public static final String BAIDU_CUSTOM_TEMPLATE_CODE_SUCCESS = "0";

    /**
     * 官网获取的 API Key 更新为你注册的
     */
    public static String BAI_DU_URL_CLIENT_ID = "";
    /**
     * 官网获取的 Secret Key 更新为你注册的
     */
    public static String BAI_DU_URL_CLIENT_SECRET = "";

    /**
     * 百度配置文件配置异常提示
     */
    //public static final String BAIDU_CONFIG_MESSAGE = "请按照部署手册配置百度接口参数";

  /*  static {
        PropertiesUtil pu = new PropertiesUtil("interface_param.properties");
        BAI_DU_URL_CLIENT_ID = pu.readProperty("clientId");
        BAI_DU_URL_CLIENT_SECRET = pu.readProperty("clientSecret");
    }*/

    /**
     * 通用百度token授权
     *
     * @return
     * @throws Exception
     * @author chenbw
     * @date 2019年6月25日
     */
    public static BaiduTokenResponse getCommonAuth() throws Exception {
        BaiduTokenResponse ocrAuthResponse = new BaiduTokenResponse();
        // 获取请求的拼接参数字符串
        String getAccessTokenUrl = BAI_DU_URL_TOKEN_URL
                // 1. grant_type为固定参数
                + "grant_type=" + HOST_URL_OCR_TOKEN_GRANT_TYPE
                // 2. 官网获取的 API Key
                + "&client_id=" + BAI_DU_URL_CLIENT_ID
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + BAI_DU_URL_CLIENT_SECRET;
        String result = CommonRestUtil.sendGet(getAccessTokenUrl);
        if (result != null) {
            Map<String, Object> map = JSON.parseObject(result);
            String authToken = map.get(HOST_URL_OCR_ACCESS_TOKEN).toString();
            ocrAuthResponse.setCode(200);
            ocrAuthResponse.setAuthToken(authToken);
        } else {
            throw new ServiceException("获取百度token,调用百度接口异常。");
        }
        return ocrAuthResponse;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                new Thread() {
                    public void run() {
                        try {
                            BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getBaiduCommonAuth();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            Thread.sleep(20 * 1000);
        }
    }

    public static BaiduTokenResponse getBaiduCommonAuth() throws Exception {
        BaiduTokenResponse ocrAuthResponse = new BaiduTokenResponse();
        // 获取请求的拼接参数字符串
        String getAccessTokenUrl = "https://aip.baidubce.com/oauth/2.0/token?"
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=QwLqlM8mtIYY2jo6kXfpRwXn"
                // 3. 官网获取的 Secret Key
                + "&client_secret=eThfSfYgcjxZeHtqyur0K1xYVuWva5oe";
        String result = CommonRestUtil.sendGet(getAccessTokenUrl);
        if (result != null) {
            Map<String, Object> map = JSON.parseObject(result);
            String authToken = map.get("access_token").toString();
            ocrAuthResponse.setCode(200);
            ocrAuthResponse.setAuthToken(authToken);

            Map<String, Object> params = new HashMap<String, Object>(8);
            params.put("access_token", authToken);
            params.put("image", "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQk" +
                    "JCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wgARCAAmAZcDASIAAhEBAxEB/8QAGgABAAMBAQEAAAAAAAAAAAAAAA" +
                    "IDBAEFBv/EABYBAQEBAAAAAAAAAAAAAAAAAAABAv/aAAwDAQACEAMQAAAB+/IkiJJGQREissU2EgCJJHp1zhJATZ7CxTadc6EenXOEkenXB1RaSR4TZ7zqPTqi0kj0650AeL7WYwx9C" +
                    "szd19MjVaYY+hXGKV+isOyUYrqvts8u/ZBasPqwMHPS4nna7b5cUd1WpVTtlLiq39MkN1ZlzeojytW+dnmV+h0ww3TXztGgZo6dJ5lt/THHbIxepkkmkKAAAAAAAAAAAAAAAAAAAAAA" +
                    "AAAB/8QAIhAAAwADAQEAAQUBAAAAAAAAAQIDAAQREhMhEBQgMVAj/9oACAEBAAEFAv8AU6AcLAEMG/QMGxiFXDRFKVnQsQq/qWAxWDAng/segCGBxmVFW8qFnRMSs6YTwf2M6PR/AB6" +
                    "OgEMDhPBhIAW0mzo9Ag4zKireVD0YGDAkALaTYSBgIOE8H8NrppFe3uzfuYE/a4X76wf3tuoTXKSyvf3OqWNNt1Ca5SWURSsJTRKCh2Ndl9XZhRKFrbijNVDl5g7Gue7FWcGHrl2YUS" +
                    "ha2wB9E9NfaHvNcda/S+v6NNw+s0vK0p8/tLorb2dnVDerzB2Nc92LevWqCMp8/tLorQMdiQ7be7501A/haZqspfMvD1VZshMVLLL5uy+iilQ0u5OHzxl9FFKgz6RBFPn/AKTXwjoxo" +
                    "qFWtM0yc/njT/KzK4w6qjyjoxoqFW8DAhFqJ7CoVd5eysyjWkKzSazVukCPnHn7IiVdp/lZlcMFLLErRukCPnKT94kQjGfcWR78R9v9T//EABwRAAIBBQEAAAAAAAAAAAAAAAABIAIQ" +
                    "ITFAUP/aAAgBAwEBPwHwFgUFsUEUiuhd3//EABkRAAMBAQEAAAAAAAAAAAAAAAABMSBAUP/aAAgBAgEBPwHuYxwdw4O4Y/D/AP/EADMQAAEDAwEECAUEAwAAAAAAAAEAAhESITFBIjJ" +
                    "RcQMQQmFigaHBICNSseETUILRQ3KR/9oACAEBAAY/Av3QDj1AHJwrdRjSyJOB1Q5wBUMe1x7iiTgfAJ1spGFJx1AalWKqcYChnSNcfCZW04N5lbHSNdyKk466dVKlATlGNLIk4HVJMB" +
                    "bPSNPIqNSjGiqcYChnSNcfCZWVIUkwFs9I08ihJyrKT8IIcyBkO+2Vps3ydUQKsDynyUUty7am+eSb8tki8ls6H+k7bNIy2BvIh3kHY/KPRhl5uWssT7LBjY+6MjDG6/hEO8g7H5R6M" +
                    "MvNy1lifZEkaJrm9G1pIuQEIsKTHpdBn6gNMgfMv/xNb8ug/U+J9FYgiLjgqnhhEw2Rj1CqFAGDDc+qMNYHOY7m7Cu9kiq1d88FI6N9uV/VB1JuBkprfl0H6nxPorEERccEwS7aP1GF" +
                    "Je4bItEcV2qZg7REo3x4ifdRteHIunbboGWmMohhmxqAIP3RBYQaR/ijii5rQXhrtoDBW+4y7WL25KWuNLWmbx7K9/5kow1gc5jubsK72SKrV3zwThSzcN50Rm0k2JJRc1oLw120Bgr" +
                    "fcZdrF7ckCXQA0xHktqZzvfn2TLTe/KORTiBEx2Yn0HwNgxBlG8z3IvkTbRbLm01TuqrtcTeOS2DDe0Dqr4CuZMyt7tSU0h142rbyvgK5kzKM9I6DohBfbxlB3AQo7ymua4CJ0RcXSS" +
                    "mw6KTKN5lEsoBO9LZlb5zMAIhAcAmua4CJ0RcXSSjN54pzy6QQAAh3GU4k5xZSc9nuUtNjvTqiIbMWqEqGtA5BWjzTaXRGe9C9oIKlrrTJtz/tEsoBO9LZlb5zMAIul1X+yqqB/jdWj" +
                    "zTaXRGe9Z7JaqrYiAIRNRnQ8FtUkDdgRC/U9v3X/8QAKBABAAICAgEDAwUBAQAAAAAAAQARITFBUWFxgaGR0fAQIFDh8cGx/9oACAEBAAE/If5RYHOn6IJS0O4RastPpj9MwXa3rFTo" +
                    "LX9CSZoXcWlBaApFToLX9mZKtTyyxtwDpQLVggJpiivQRdLEaYoGe1dQq/Z/oQ2zvQi9N+NgOlAtWCAmn9My2BcQiaDLAMaSyLAB08xEq129YWNBbBEE0xEANrxFaX8LEg3Aoen+zKl" +
                    "2p8MUDPauoVfs/wBCUKUwW+Jcmy0+jURADa8RWl/CxUAWo8svbDTT6wGSgLWDYJz+y401Ax3lhb14jO5jb3cqwlHOmBT1b4C4z67Jgq7E2tuMDPnNRyKIZRUTLPCYJVzV1qNGaukJ9/" +
                    "hL5jamB4PCK2npZKoz3z9JWlQuddYp68xozV0hPv8ACXzG1MDweEuZIuYfKtGLLHfQ7f8A0/LiHKYOtQs41HlyKRM42xEpnKp7UjzqXrwFUuF8gf8AY1DHoBx3hHO/EqAyED8nvNpKG" +
                    "3V0CXhy9piK8iWrKvgwB49Y8uRSJnG2IlM5VPakedSxwppCFdl1nB7wvTCNYc4Zt47gAoaFZvCj5/KBtnCtDfanXjQLs8UTAX89QtU3VsG6ur1XPJCV2OqLGIsQtGZPVkm801vVVvDC" +
                    "bGazoW8D8Jlfq1BxgS3X1iGpoZWs2mB9GVAZCB+T3m0lDbq6BEvMixbF8Y+eIOr5Dml8tG+pvNNb1Vbwwmxms6FvA/CLYdimM5S1ua0Bl+2/EMVIXxHk3X/hMnIZeRnk31+wwN5yPnp" +
                    "O4iU0Bilb+8pkXu3QG0uUwSTN2293HJM37YdTKGmhdu77ilfNYHL5jOpUkKiZlVGLdaI74Ob7HpUUr5rA5fMZ1KkhUb2poHxcYegMf0uoptYSnrX2i3W8l+rcpWMN2u68+Iwqqaqq/w" +
                    "Bg9Z7Qt4ThO5nWxi48TAG1YX6kNAIMABmSKsq5nO6CUrGG7XdefEYVVNVVf7LcXft7THIANVf3lNsoVZuoQ1KUKUQ8Wjl8/X89RYvcl36/11L97osKFsT6IEmR6XLKazYvz9/ScaVCt" +
                    "jX2mNVUHJyowBtWF+pDQCDAAZVBXNmvQcfEwEu3b66+IEmR6XLKazYvz9/SJfAHWvdfaEbYMjH5iXKlo+B+fMLx0jOZ8yzOX9Wqq+vHf8r/AP/aAAwDAQACAAMAAAAQ84w0w004w488" +
                    "w880w4884w88885IgYeSfPcJ3t9M8sXL1UoJk0d888888888888888888888888888//xAAhEQACAgEEAgMAAAAAAAAAAAAAAREhMRBAQVEwccHR8P/aAAgBAwEBPxDYq8eFYYqkWiq" +
                    "UcyNKRGlGiEqg5H2Si9HzH6vsRRyUySWSMpiy/fyQTspkXb3v/8QAIhEBAAIABQQDAAAAAAAAAAAAAQARECFBYfAxQFHBcYGx/9oACAECAQE/EO61mpg85+SyH1KLAtNa9RDbBi53Cu" +
                    "TxHaSazx8+8BZlFbZHaajg7R25lHp3v//EACMQAQEAAgIDAAMAAwEAAAAAAAERACExQVFhcYGRoRAgULH/2gAIAQEAAT8Q/wCof8dD5QV/h/gOqHYQK/wchf8AsCUfhExQFUA5XNE7Y" +
                    "9DkwY7iOjBGx459ZKogOKaADyseMFH4eDxQdfnBjuI6MEbHjn1/lMmjF7BZ/H9YEsFBibFHn2OHvaQgByuOHQEfWSlV1LYV+YXxMuNnM8/jCUTYwPK+MMgDAwHKtTnvBC52R/rjwMKj" +
                    "ocXTh72kIAcrjh0BH1/gYClZHhUN8dODiGo8AYw6moIx9OzH/GhewVD3Nz0+M6ugrqBnvSYuFBJYBXHaoUfJhthq4DyuB3ZCM144cGaeYBF/8fvHwKUznwP4R+JhKJsYHlfGGQBgYDl" +
                    "Wpz3ixAkC8N7fBp/TjSEaybRH7HDbDVwHlcDuyEZrxw48I3/gWfocHd3l0ORwriyOg5wrqClEf08f6EnBY8KjBOGxNKmq2IOKkhEnrVP7tYawmRNRJtbtrwZpUjgiZJhZ/I2jF+/SLU" +
                    "jw6ib5fWJQ60gAg0BRvs+YtHGgL2M5SkAlljEpD4rUFXRo6OgQFjClIW/Lmz/B6udfkK4WOBdrfTcxaONAXsZylIBLLGJSHxWoKujR0dAgLGallhU4uyxzgJXgI7Qr+cBRgBB1C/cQH" +
                    "2+F/qLmkBi07vFmG9MvxGACKMtfgjLYwR32QANCehO6QuG2SkiRUaVYDwDNJrNTA7NpG2BvW4IoA05ZEQ2xLx1quH8dUsA4anrJxsLadpURjZ0k3iKeKRazSRam+EOsN6ZfiMAEUZa/" +
                    "BGWxgjvsgAaE9Cd113+pIUIIUacg7MBIotYqBCrZtaKYIIE1ZiUieU9G1cJMdLYmEUHRrXvBsaBrCVSFJ5WxyMbKROBBrZS1dhPWSw7WroFtTfAfenZOb5JTHJeaX3vLFcDGIwY6LB6" +
                    "xAVBr13E1ZFf4hPRPtO6gMlI3o0TAf7NLJAmtESkNtzcEUAacsiIbYl461XD+OqWAcNT1jcETNMitcLwQnli8LUDVScBowWXLFcDGIwY6LB6xAVBr13E1ZFf4gPYw2Gwo7RT0L25Nv9" +
                    "qIQVK8GwcGK5XYIbDwdnMfJkIRSwyVwg4Wf9Bi1KowCbX7fvIZohjFdrypw2QCFRwci3rj3koO+twh02vM6w50SI6xKhFt4O23jGAg7V18xFWrbDxkWQHVGx+HYeQcMpKOKuoVkIcvG" +
                    "N0agUEew4Nj585w9S4AE7HgOdLa7yLIDqjY/DsPIOGUlHFXUKyEOXjF5ISZEjE/ovOEE60TQhdo9JMkXTRtU7fy/eGCvRE5eH5zdjlbFNaB9DhiWTQCoB+Wc73YHCbH7ZYTECAgNVXq" +
                    "7e8dTWF4BIz+l+Y8EiphVNihvpPuKeE4RZSWY79GU7hM3Y5WxTWgfQ4Ylk0AqAfli5HoCAIcR4PHt84STRhbNrd+Guu8pYB1AKzGkeKArp3va9Gp9xWj5zRiMSKkOzRPOFZqs717IHI" +
                    "dSQQBiiRAgSJxz304RoAMizvWLGAlgD538phJVaIBuxqbcx0RCYi5MHs1IN15a76d5AtSk4KIgFXTxjqawvAJGf0vzHgkVMKpsUN9J9zo6Fj3HbTsGaUPGMWjtAKIsJtCwvvFjASwB8" +
                    "7+UwkqtEA3Y1NuY6IhMUlqLzc3JorXdwDAkARRdHx31gm6CDi0BNm2jbW+ITGWzA9kKgiEHp84gqHZpO3k87+S9f8AV//Z");
            Map<String, Object> resultMap = CommonRestUtil.sendPost("https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic", params);
        } else {
            throw new ServiceException("获取百度token,调用百度接口异常。" + result);
        }
        return ocrAuthResponse;
    }
}
