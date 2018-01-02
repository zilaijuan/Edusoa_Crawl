package com.zilaijuan.edusoa.university;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class EdusoaUniversity implements PageProcessor {
	private Site site = Site.me().setRetryTimes(10).setSleepTime(50);

	@Override
	public void process(Page page) {
		System.out.println(page.getRawText());
		Json json = page.getJson();
		JSONObject jsonObject = JSON.parseObject(page.getRawText());
		int totalRow = jsonObject.getInteger("totalRow");
		int pageNumber = jsonObject.getInteger("pageNumber");
		int pageSize = jsonObject.getInteger("pageSize");
		if(totalRow/(float)pageSize > pageNumber){
			String url = "http://www.edusoa.com/dsideal_yy/zygh/university/getUniversityList?pageNumber="+
							(pageNumber+1)+"&pageSize=10&university_classification=&province_id=&university_type=&university_label=";
			page.addTargetRequest(new Request(url));
		}
		String str = page.getRawText();
		page.putField("UniversityList", jsonObject.getString("university_list").toString());
		
		
	}

	

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new EdusoaUniversity())
						.addPipeline(new MongoDBPipeline())
						.addPipeline(new ConsolePipeline())
						.addUrl("http://www.edusoa.com/dsideal_yy/zygh/university/getUniversityList?pageNumber=1&pageSize=10&university_classification=&province_id=&university_type=&university_label=")
						.thread(5)
						.run();
	}
}
