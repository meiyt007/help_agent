package com.zfsoft.ha.outer.scanning;

import com.zfsoft.ha.outer.scanning.Lister.MyLister;
import com.zfsoft.ha.outer.scanning.component.PortInit;
import com.zfsoft.ha.outer.scanning.util.SerialPortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PreDestroy;

@SpringBootApplication
@ConfigurationPropertiesScan
@CrossOrigin
@EnableAsync
public class ScanningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScanningApplication.class, args);
    }

//    @PreDestroy
//    public void destory() {
//        //关闭应用前 关闭端口
//        SerialPortUtil serialPortUtil = SerialPortUtil.getSerialPortUtil();
//        serialPortUtil.removeListener(PortInit.serialPort, new MyLister());
//        serialPortUtil.closePort(PortInit.serialPort);
//    }

}
