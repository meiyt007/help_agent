package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName FieldTree
 **/
@Data
@ToString
public class FieldTree {

    private String value;

    private String label;

    private List<FieldTree> children;

    private String parentOid;
}
