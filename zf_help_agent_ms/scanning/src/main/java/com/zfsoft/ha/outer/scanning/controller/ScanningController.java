package com.zfsoft.ha.outer.scanning.controller;


import com.zfsoft.ha.outer.scanning.Lister.MyLister;
import com.zfsoft.ha.outer.scanning.component.PortInit;
import com.zfsoft.ha.outer.scanning.pojo.ApiResultSet;
import com.zfsoft.ha.outer.scanning.util.SerialPortUtil;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/21 16:18
 */
@RestController
@RequestMapping(value = "/scanning")
public class ScanningController {
    private static final Logger logger = LoggerFactory.getLogger(ScanningController.class);

    public static final Object object= new Object();

    public static String result = "";




    @Value("${portname}")
    private String portname;


    /**
     * 打开扫描灯
     * @param
     */
    @PostMapping(value = "/open.do")
    public ApiResultSet<String> open() {
        result="";
        MyLister.status=false;
//        synchronized (object) {
            try {

                Thread thread = Thread.currentThread();
                System.out.println("===========scanning==========="+thread.getName());
                addListen();
                SerialPort serialPort = PortInit.serialPort;
                byte[] bytes = SerialPortUtil.hexStrToByteArray("327501");
                //获取输出流，利用输出流发送数据 327501
                OutputStream outputStream = serialPort.getOutputStream();
                outputStream.write(bytes);
                long t1 = System.currentTimeMillis();
//                for (long t2 = System.currentTimeMillis(); t2 - t1 < 8000; t2 = System.currentTimeMillis()) {
//                    object.wait(1000);
//                    if(MyLister.status){
//                        break;
//                    }
//                }
                while (!MyLister.status) {
                    long t2 = System.currentTimeMillis();
                    //超时关闭
                    if(t2-t1 > 8000){
                        if(MyLister.status){
                            break;
                        }
                        byte[] bytes2 = SerialPortUtil.hexStrToByteArray("327502");
                        outputStream.write(bytes2);
                        outputStream.close();
                        return new ApiResultSet<>(505, "timeOut");
                    }

                }
                //一定要关闭串口，否则会阻塞该串口，直到你关闭程序
//            serialPort.close();
                outputStream.close();

            } catch (Exception e) {
                System.out.println("Exception抛出");
                e.printStackTrace();
                return new ApiResultSet<>(500, e.getMessage(), e.getMessage());
//                Map<String,String > re = new HashMap<>();
//                re.put("qrcode","");
//                return ApiResultSet.ok("成功",result);
            } finally {
                close();
            }


            return ApiResultSet.ok("成功", result);
//        }
    }

    @GetMapping(value = "/close.do")
    public ApiResultSet<String> close(byte[] message) {
        if(PortInit.serialPort==null){
            return ApiResultSet.ok("串口未连接",result);
        }
        close();
        return ApiResultSet.ok("成功",result);
    }
    /**
     * 监听com1
     */
    public void addListen(){
        try{
            SerialPortUtil serialPortUtil = SerialPortUtil.getSerialPortUtil();
            ArrayList<String> port = serialPortUtil.findPort();
            logger.info("发现全部串口：" + port);
            logger.info("打开指定portname:" + portname);

            //打开该对应portname名字的串口
            PortInit.serialPort = serialPortUtil.openPort(portname, 15200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            //给对应的serialPort添加监听器
            serialPortUtil.addListener(PortInit.serialPort, new MyLister());
        } catch (Exception e1) {
            throw new RuntimeException(e1.getMessage());
        }
    }

    public void close(){
        //关闭应用前 关闭端口
        SerialPortUtil serialPortUtil = SerialPortUtil.getSerialPortUtil();
//        System.out.println("关闭监听" );
        if(PortInit.serialPort==null){
            logger.info("没有需要关闭的串口和监听");
            return;

        }
        logger.info("关闭监听");
        serialPortUtil.removeListener(PortInit.serialPort, new MyLister());
//        System.out.println("关闭串口" );
        logger.info("关闭串口");
        serialPortUtil.closePort(PortInit.serialPort);
    }


    /**
     * 向com2传参数 监听com1  用于本地测试，，因为是模拟的串口，输入和监听串口不一致。扫描机说是一个串口

    @GetMapping(value = "/open2.do")
    public ApiResultSet<String> write2(byte[] message) {
        result="";
        addListen();


        //枚举类型，获取所有的通行端口，包括232（PORT_SERIAL）、485、并口等等
        Enumeration enumeration= CommPortIdentifier.getPortIdentifiers();

        while (enumeration.hasMoreElements()){
            //判断enumeration里面是否有更多的元素

            //获取下一个元素，该元素包含某个通信端口的所有信息
            CommPortIdentifier commPortIdentifier=
                    (CommPortIdentifier) enumeration.nextElement();

            //如果该端口的类型是串口
            if (commPortIdentifier.getPortType()==CommPortIdentifier.PORT_SERIAL){

                //判断该串口的名称
                if (commPortIdentifier.getName().equals("COM7")){
                    try {
                        //打开串口，获得该串口的serialPort对象
                        SerialPort serialPort=
                                (SerialPort) commPortIdentifier.open("",2000);

                        //设置该串口参数，9600,8,1,n
                        serialPort.setSerialPortParams(9600,8,1,0);

                        //获取输出流，利用输出流发送数据
                        OutputStream outputStream=serialPort.getOutputStream();
                        outputStream.write(message);

                        //一定要关闭串口，否则会阻塞该串口，直到你关闭程序
                        serialPort.close();
                        outputStream.close();

                    } catch (PortInUseException e) {
                        System.out.println("PortInUseException抛出，串口被使用");
                        e.printStackTrace();
                        return new ApiResultSet<>(500, "获取核酸信息错误", e.getMessage());
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println("UnsupportedCommOperationException抛出");
                        e.printStackTrace();
                        return new ApiResultSet<>(500, "获取核酸信息错误", e.getMessage());
                    } catch (IOException e) {
                        System.out.println("IOException抛出");
                        e.printStackTrace();
                        return new ApiResultSet<>(500, "获取核酸信息错误", e.getMessage());
                    }
                }
            }
        }

        close();

        return ApiResultSet.ok("成功",result);
    }
     */






}
