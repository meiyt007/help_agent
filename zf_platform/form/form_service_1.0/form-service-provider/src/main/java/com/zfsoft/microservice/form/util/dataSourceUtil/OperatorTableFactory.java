package com.zfsoft.microservice.form.util.dataSourceUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName OperatorFactory
 * @Description: 数据库操作的工厂
 * @Author wuxx
 * @Date 2021/8/25
 **/
public class OperatorTableFactory {

    static Map<String, OperationTable> operationMap = new HashMap<>();

    static {
        operationMap.put("mysql", new MySqlFormTableDtoUtil());
        operationMap.put("oracle", new OracleFormTableDtoUtil());
        operationMap.put("sqlserver", new SQLServerFormTableDtoUtil());
        operationMap.put("mongoDB", new MongDbFormTableDtoUtil());
        operationMap.put("dm", new DmFormTableDtoUtil());
        operationMap.put("postgreSQL", new PostgreSqlFormTableDtoUtil());
    }

    public static OperationTable getOperation(String operation) {
        return Optional.ofNullable(operationMap.get(operation)).get();
    }

    public static void setOperation(String operation,OperationTable object) {
        operationMap.put(operation, object);
    }
}
