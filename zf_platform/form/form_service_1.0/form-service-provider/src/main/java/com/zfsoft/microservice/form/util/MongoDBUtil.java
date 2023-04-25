package com.zfsoft.microservice.form.util;

import cn.hutool.core.util.StrUtil;
import com.mongodb.MongoClient;
import com.mongodb.*;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:  MongoDB操作类
 * @author: wuxx
 * @Date: 2021/4/12 17:02
 **/
public class MongoDBUtil {
    private MongoClient mongoClient;
    private String dbName;

    public MongoDBUtil(){

    }

    public MongoDBUtil(String host,String port,String dbName,String userName,String password){
        this.init(host,port,dbName,userName,password);
    }
    /**
     * @description: 初始化数据库
     * @param host
     * @param port
     * @param dbName
     * @author: wuxx
     * @Date: 2021/4/12 17:11
     **/
    public void init(String host,String port,String dbName,String userName,String password){
        if(StrUtil.isNotBlank(userName) && StrUtil.isNotBlank(password)){
            MongoClientOptions.Builder builder = MongoClientOptions.builder(); //可以通过builder做各种详细配置
            MongoClientOptions myOptions = builder.build();
            ArrayList<ServerAddress> serverAddressList = new ArrayList();
            ServerAddress record = new ServerAddress(host,  Integer.parseInt(port)); //IP、端口
            serverAddressList.add(record);
            //用户名、默认库名、密码
            MongoCredential credential = MongoCredential.createCredential(userName, dbName,password.toCharArray());
            mongoClient = new MongoClient(serverAddressList, credential, myOptions);
        }else {
            mongoClient = new MongoClient(host, Integer.parseInt(port));
        }
        this.dbName = dbName;
        Builder options = new Builder();
        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        options.connectTimeout(3000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000); //
        options.socketTimeout(0);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出"Out of semaphores to get db"错误。
        options.writeConcern(WriteConcern.SAFE);//
        options.build();
    }

    /**
     * @description: 测试连接
     * @author: wuxx
     * @Date: 2021/4/13 11:50
     **/
    public Map<String,String> checkMongoDbConnnect() {
        Map<String,String> map = new HashMap<>();
        try {
            MongoDatabase mongoDatabase = this.getMongoDatabase();
            MongoIterable<String> iterable = mongoDatabase.listCollectionNames();
            iterable.first();
            map.put("success","true");
            return map;
        }catch (Exception e){
            map.put("success","false");
            map.put("message",e.getLocalizedMessage());
            return map;
        }
    }

    public MongoDatabase getMongoDatabase() {
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database;
    }

    /**
     * 获取collection对象 - 指定Collection
     *
     */
    public MongoCollection<Document> getCollection(String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    public DBCollection getDbCollection(String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        DBCollection collection = mongoClient.getDB(dbName).getCollection(collName);
        return collection;
    }

    /* *
     * @Description: 保存到mongdb数据库
     * @Param: [object, oid, createDate, collName]
     * @Author: wuxx
     * @Date: 2020/2/28 13:14
     * @Return: boolean
     **/
    public boolean saveMongoDBObject(Object object, String oid, Date createDate,String collName){
        DBCollection coll = this.getDbCollection(collName);
        DBObject doc = new BasicDBObject();
        doc.put("id", oid);// 主键
        doc.putAll(this.toHashMap(object));
        // 日期需传递java.util.Date，保证存入mongodb时为ISODate格式
        doc.put("createDate", createDate);
        //WriteResult writeResult = coll.insert(doc);
        WriteResult writeResult = coll.save(doc);
        boolean insertFlag = writeResult.wasAcknowledged();
        return insertFlag;
    }

    /* *
     * @Description: 保存到mongdb数据库
     * @Param: [object, oid, createDate, collName]
     * @Author: wuxx
     * @Date: 2020/2/28 13:14
     * @Return: boolean
     **/
    public boolean saveMongoDBObject(Map<String, Object> dataMap, String oid, Date createDate,String collName){
        DBCollection coll = this.getDbCollection(collName);
        DBObject doc = new BasicDBObject();
        doc.put("_id", oid);// 主键
        doc.putAll(dataMap);
        // 日期需传递java.util.Date，保证存入mongodb时为ISODate格式
        doc.put("createDate", createDate);
        //WriteResult writeResult = coll.insert(doc);
        WriteResult writeResult = coll.save(doc);
        boolean insertFlag = writeResult.wasAcknowledged();
        return insertFlag;
    }
    /* *
     * @Description:根据主键获取mongdb数据
     * @Param: collName mongdb集合名称
     * @Param: oid 主键
     * @Author: wuxx
     * @Date: 2020/3/1 11:00
     * @Return: org.bson.Document
     **/
    public Document getMongoDBInfoByOid(String collName,String oid){
        MongoCollection<Document> coll = this.getCollection(collName);
        BasicDBObject order = new BasicDBObject();
        // mongodb中按createDate字段倒序查询（-1是倒序，1是正序）
        //order.put("createDate", -1);
        //order.put("_id", ObjectId);//主键
        ObjectId ObjectId = new ObjectId(oid);
        Document document = coll.find(Filters.eq("_id", ObjectId)).first();
        return document;
    }

    /**
     * 获取mongodb数据
     * @author zxx
     * @date 2020-03-08 18:50
     * @param collName 集合名称
     * @param basicDBObject 基础数据类
     * @return
     * @throws Exception
     */
    public Document getMongoDBInfo(String collName,BasicDBObject basicDBObject){
        if(basicDBObject==null){
            return null;
        }
        MongoCollection<Document> coll = this.getCollection(collName);
        Document document = coll.find(basicDBObject).first();
        return document;
    }


    /**
     * 获取查询结果集合List
     * @param tableName 查询的表名
     * @param query 查询条件
     *         BasicDBObject query = new BasicDBObject();
     *         时间区间查询 记住如果想根据这种形式进行时间的区间查询 ，存储的时候 记得把字段存成字符串，就按yyyy-MM-dd HH:mm:ss 格式来
     *         query.put("times", new BasicDBObject("$gte", "2018-06-02 12:20:00").append("$lte","2018-07-04 10:02:46"));
     *         模糊查询
     *         Pattern pattern = Pattern.compile("^.*王.*$", Pattern.CASE_INSENSITIVE);
     *         query.put("userName", pattern);
     *         精确查询
     *         query.put("id", "11");
     *         不等于
     *         new BasicDBObject(QueryOperators.NE, "500")
     *         并且
     *         new BasicDBObject("$and",Arrays.asList(ageObj,sexObj));
     * @param sort 排序 默认mongodb中按createDate字段倒序查询（-1是倒序，1是正序）
     * @param  groupFields
     *          // 根据测试编号分组统计，存在多个分组条件则在Document后面追加
     *         //Document groupFields =  new Document("_id", new Document("testNumber", "$testNumber").append("isBelong", "$isBelong"));
     *         //groupFields.put("count", new Document("$sum", 1));
     *         //groupFields.put("count", new Document("$avg", 1));
     *         //groupFields.put("count", new Document("$count", 1));
     * @return 结果集合List<Document>
     *
     */
    public List<Document> queryMongDbPagesList(String tableName, BasicDBObject query,BasicDBObject sort,Document groupFields) {
        if(null==query){
            query =  new BasicDBObject();
            query.put("_id",new BasicDBObject(QueryOperators.NE, ""));
        }
        if(null==sort){
            sort = new BasicDBObject();
            // mongodb中按createDate字段倒序查询（-1是倒序，1是正序） 默认
            sort.put("createDate", -1);
        }
        if(null==groupFields){
            //不分组
            MongoCollection<Document> collection = this.getCollection(tableName);
            List<Document> newLins = new ArrayList<>();
            Block<Document> saveBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    newLins.add(document);
                }
            };
            collection.find(query).sort(sort).forEach(saveBlock);
            return newLins;
        }
        Document match = new Document("$match", query);// 限定查询条件
        Document sortDoc = new Document("$sort", sort);// 排序条件
        // 根据测试编号分组统计，存在多个分组条件则在Document后面追加
        //Document groupFields =  new Document("_id", new Document("testNumber", "$testNumber").append("isBelong", "$isBelong"));
        //groupFields.put("count", new Document("$sum", 1));
        Document group = new Document("$group", groupFields);
        List<Document> qs = new ArrayList<Document>();
        qs.add(match);
        qs.add(sortDoc);
        qs.add(group);
        List<Document> newLins = new ArrayList<>();
        MongoCollection<Document> coll = this.getCollection(tableName);
        // 放到管道中将这些节点运算符运算起来
        AggregateIterable<Document> output = coll.aggregate(qs, Document.class);
        MongoCursor<Document> i = output.iterator();
        while (i.hasNext()) {
            Document d = i.next();
            newLins.add(d);
        }
        return newLins;
    }

    /* *
     * @Description: 根据主键删除mongdb数据库数据
     * @Param: [tableName, oid]
     * @Author: wuxx
     * @Date: 2020/2/27 14:23
     * @Return: long
     **/
    public long deleteMongoDBInfo(String tableName,String oid) throws Exception {
        MongoCollection<Document> collection = this.getCollection(tableName);
        DeleteResult deleteResult =collection.deleteOne(Filters.and(Filters.eq("_id", oid)));
        return deleteResult.getDeletedCount();
    }

    /* *
     * @Description: 根据条件更新mongdb数据库数据
     * @Param: [tableName, oid]
     * @param mapParams 需要更新的参数以及对应的值
     * @Author: wuxx
     * @Date: 2020/2/27 14:23
     * @Return: long
     **/
    public void updateMongoDBInfo(String tableName, String oid, Map<String,Object> mapParams) throws Exception {
        MongoCollection<Document> collection = this.getCollection(tableName);
        for(Map.Entry<String, Object> entry : mapParams.entrySet()){
            String mapKey = entry.getKey();
            if(StrUtil.isEmpty(mapKey)){
                continue;
            }
            Object mapValue = entry.getValue();
            collection.updateOne(Filters.eq("_id", oid), new Document("$set", new Document(mapKey, mapValue)));
        }
    }

    /***
     * 将对象转换为HashMap
     *
     * @param object
     * @return
     */
    public static Map toHashMap(Object object) {
        Map<String, Object> data = new HashMap<String, Object>(16);
        cn.hutool.json.JSONObject jsonObject = cn.hutool.json.JSONUtil.parseObj(object);
        Iterator it = jsonObject.keySet().iterator();
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            Object value = jsonObject.get(key);
            data.put(key, value);
        }
        return data;
    }

    /**
     * @description: 操作文档的增删改
     * @param tableName 文档名称
     * @param tableSqlList sql
     * @author: wuxx
     * @Date: 2021/8/26 14:51
     **/
    public Map<String, Object> executeBatchByList(String tableName,List<String> tableSqlList){
        Map<String, Object> map = new HashMap<>();
        try {
            DBCollection coll = this.getDbCollection(tableName);
            coll.drop();
            DBObject dbObject = new BasicDBObject();
            //dbObject.put("_id", oid);// 主键
            for(String str : tableSqlList){
                dbObject.put(str,"");
            }
            WriteResult writeResult = coll.save(dbObject);
            boolean insertFlag = writeResult.wasAcknowledged();
            if(insertFlag){
                map.put("success",true);
            }else {
                map.put("success",false);
                map.put("message","生成数据表失败!");
            }
        }catch (Exception e){
            map.put("success",false);
            map.put("message",e.getMessage());
            return map;
        }
        return map;
    }


}


