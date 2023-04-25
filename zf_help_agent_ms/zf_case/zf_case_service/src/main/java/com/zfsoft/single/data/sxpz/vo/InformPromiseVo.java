package com.zfsoft.single.data.sxpz.vo;

import com.zfsoft.single.data.sxpz.InformPromise;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InformPromiseVo {
    private List<InformPromise> inforList;

    private List<String> delServiceOids;
}
