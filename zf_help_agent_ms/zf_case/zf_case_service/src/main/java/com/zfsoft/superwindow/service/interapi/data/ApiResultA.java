package com.zfsoft.superwindow.service.interapi.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: qiaol
 * @Description:
 * @Date: 2022/5/16 17:52
 */
@Data
public class ApiResultA implements Serializable {

    private static final long serialVersionUID = 6549917170586993409L;

    private String nameA;

    private String ageA;
}
