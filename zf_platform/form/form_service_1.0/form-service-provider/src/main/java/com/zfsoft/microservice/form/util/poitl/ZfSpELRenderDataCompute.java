/*
 * Copyright 2014-2021 Sayi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zfsoft.microservice.form.util.poitl;

import com.deepoove.poi.render.compute.EnvModel;
import com.deepoove.poi.render.compute.ReadMapAccessor;
import com.deepoove.poi.render.compute.RenderDataCompute;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Spring expression language compute
 *
 * @author Sayi
 * @since 1.5.0
 */
public class ZfSpELRenderDataCompute implements RenderDataCompute {

    private final ExpressionParser parser;
    private final EvaluationContext context;
    private EvaluationContext envContext;
    private boolean isStrict;

    public ZfSpELRenderDataCompute(EnvModel model) {
        this(model, true);
    }

    public ZfSpELRenderDataCompute(EnvModel model, boolean isStrict) {
        this(model, isStrict, Collections.emptyMap());
    }

    public ZfSpELRenderDataCompute(EnvModel model, boolean isStrict, Map<String, Method> spELFunction) {
        this.isStrict = isStrict;
        this.parser = new SpelExpressionParser();
        if (null != model.getEnv() && !model.getEnv().isEmpty()) {
            this.envContext = new StandardEvaluationContext(model.getEnv());
            ((StandardEvaluationContext) envContext).addPropertyAccessor(new ReadMapAccessor());
        }
        this.context = new StandardEvaluationContext(model.getRoot());
        ((StandardEvaluationContext) context).addPropertyAccessor(new ReadMapAccessor());
        spELFunction.forEach(((StandardEvaluationContext) context)::registerFunction);
    }

    @Override
    public Object compute(String el) {
        try {
            if (null != envContext && !el.contains("#this")) {
                try {
                    Object val = parser.parseExpression(el).getValue(envContext);
                    if (null != val) {
                        return val;
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
            //--- 防止三目运算报错 处理
			HashMap<String,Object> value = (HashMap<String, Object>) ((StandardEvaluationContext) context).getRootObject().getValue();
			if (el.contains("==") && el.contains("?") && el.contains(":")){
				String substring = el.substring(0, el.indexOf("==")).trim();
				if (substring.contains(".")){
                    String[] split = substring.split("\\.");
                    if (!"empty".equals(split[0]) && !value.containsKey(split[0])){
                        HashMap<String, String> map = new HashMap<>();
                        map.put(split[1],null);
                        value.put(split[0],map);
                    }else {
                        Map<String, Object> map = (Map<String, Object>) value.get(split[0]);
                        if (!"empty".equals(split[1]) && !map.containsKey(split[1])){
                            map.put(split[1],null);
                        }
                    }
                }else {
                    if (!"empty".equals(substring) && !value.containsKey(substring))
                        value.put(substring,null);
                }
			}
            //---上下无关联块选择 处理 要求 必须 _efBlockTag结尾
            if (el.endsWith("_efBlockTag") ) {
                Object obj = value.get(el.replaceAll("_efBlockTag",""));
                if (obj != null && obj.toString().equals("1")) {
                    value.put(el,true);
                } else {
                    value.put(el,false);
                }
            }
            //--- 处理list固定条数问题，要求 必须 _elListTag_{number} 结尾 ，其中 占位 {number} 值必须是 大于 0 的正整数 ，代表几行
            //--- 如果实际的list长度超过了 number， 那就以实际的 list为准。如果小于 number ，则补空数据，使得list长度等于 number。
            //--- elListTag_inx 为序号
            if(el.contains("_elListTag_")){
                String strNum = el.substring(el.indexOf("_elListTag_")).replaceAll("_elListTag_","");
                int num = Integer.parseInt(strNum);
                Object obj = value.get(el.substring(0, el.indexOf("_elListTag_")));
                if (obj == null){
                    obj = new ArrayList<>();
                }
                List<HashMap<String ,Object>> listData = (List) obj;
                int index = 1;
                for (HashMap<String,Object> data:listData ) {
                    data.put("elListTag_inx",index++);
                }
                while (listData.size() < num){
                    listData.add(new HashMap<>());
                }
                value.put(el,listData);
            }
			return parser.parseExpression(el).getValue(context);
        } catch (Exception e) {
            if (isStrict) throw e;
            return null;
        }
    }

}
