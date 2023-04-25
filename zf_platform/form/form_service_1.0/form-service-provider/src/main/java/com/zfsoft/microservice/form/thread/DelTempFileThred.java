package com.zfsoft.microservice.form.thread;


import java.io.File;

/**
 * @Description: 上传文件后删除临时文件 线程
 * @Author: wuxx
 * @Date: 2021/12/1 17:25
 * @Return:
 **/
public class DelTempFileThred extends Thread {

    /**
     * 临时文件路径
     */
    private String path;

    public DelTempFileThred(String path) {
        super();
        this.path = path;
    }

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(30000L);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                System.out.println("临时文件删除成功,path="+path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
