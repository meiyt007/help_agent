package com.zfsoft.single.util;

import com.zfsoft.cases.util.spring.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/***
 * @Description: 智审使用
 * @Author:liangss
 * @Date:2021/10/11
 * @Param:
 */
@Component
@Slf4j
public class AuditUtil {



	/** 线程池 */
	private static  ExecutorService fixedThreadPool =null;



	private static final ThreadPoolExecutor execu;

	static {
		//fixedThreadPool = Executors.newFixedThreadPool(5);
		execu=new ThreadPoolExecutor(1,10,4,
				TimeUnit.SECONDS,new ArrayBlockingQueue<>(10),new ThreadPoolExecutor.AbortPolicy());
	}


	public static void sendIntelligentPretrial(String caseOid,
											   String attaOid,
											   String materialOid,
											   String  caseMaterialOid,
											   String  refinedMaterialOid,
											   String materialCatalogOid,
											   String materialAttaOid,
											   String materialName) {
		Thread thread = new Thread(new Runnable() {
			@SneakyThrows
			@Override
			public void run() {
				System.out.println("进入"+materialName+"线程");
				Map<String, Object> modelMap=new HashMap<>();
				modelMap=null;
				//intelligentPreTrialManager.intelligentPretrialmaterialPrePrial(caseOid,attaOid, materialOid,caseMaterialOid, refinedMaterialOid,materialCatalogOid,materialAttaOid,materialName);
				modelMap.put(materialAttaOid+"-"+materialName,modelMap.get("message"));
				System.out.println("获取调用百度ocr接口结束="+materialName+"****"+modelMap.get("message"));
				System.out.println("当前活动线程数："+ execu.getActiveCount());
			}

		});



		log.info("核心线程数："+ execu.getCorePoolSize());
		log.info("总线程数："+ execu.getPoolSize());
		/*System.out.println("最大线程池数量"+execu.getMaxPoolSize());
		System.out.println("线程处理队列长度"+executor.getThreadPoolExecutor().getQueue().size());*/
		execu.execute(thread);
		//fixedThreadPool.execute(thread);

	}




	public static String intelligentPretrial(List<Map<String, Object>> parmap) throws InterruptedException {

		final int threadCount = parmap.size();
		final AtomicInteger count = new AtomicInteger(threadCount);
		final Object waitObject = new Object();

		ExecutorService pool = Executors.newCachedThreadPool();
		int i = 0;
		for(Map<String, Object> map:parmap){
			String  caseOid= (String) map.get("caseOid");
			String  attaOid= (String) map.get("attaOid");
			String  materialOid= (String) map.get("materialOid");
			String  caseMaterialOid= (String) map.get("caseMaterialOid");
			String  refinedMaterialOid= (String) map.get("refinedMaterialOid");
			String  materialCatalogOid= (String) map.get("materialCatalogOid");
			String  materialAttaOid= (String) map.get("materialAttaOid");
			String  materialName= (String) map.get("materialName");
			i++;
			final int j = i;
			pool.execute(new Runnable() {
				@Override
				public void run() {
					Map<String, Object> modelMap=new HashMap<>();
					try {
						log.info("运行线程号开始:" + j +"materialName="+materialName);
//						modelMap=intelligentPreTrialManager.intelligentPretrialmaterialPrePrialBaiduOcr(caseOid,attaOid,
//								materialOid,caseMaterialOid, refinedMaterialOid,materialCatalogOid,materialAttaOid,materialName);
					} catch (Exception e) {
						e.printStackTrace();
					}
					modelMap.put(materialAttaOid+"-"+materialName,modelMap.get("message"));
				/*	log.info("获取调用百度ocr接口结束="+materialName+"****"+modelMap.get("message")
							+"materialAttaOid="+materialAttaOid+"refinedMaterialOid="+refinedMaterialOid
					        +"caseMaterialOid="+caseMaterialOid+"materialCatalogOid="+materialCatalogOid);*/
					log.info("运行线程号结束:" + j +"materialName="+materialName);
					synchronized (waitObject) {
						int cnt = count.decrementAndGet();
						if (cnt == 0) {
							waitObject.notifyAll();
						}

					}
				}
			});
		}

		synchronized (waitObject) {
			while (count.get() != 0) {
				waitObject.wait();
			}
		}

		Thread.sleep(1000);

		System.out.println("结束线程");
		return "线程结束";
	}


	}
