package com.zilaijuan.Start;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class Edusoa implements PageProcessor {
	private Site site = Site.me().setRetryTimes(10).setSleepTime(5000);

	@Override
	public void process(Page page) {
//		System.out.println(page.getJson());
		if (page.getUrl().regex("http://www\\.edusoa\\.com/dsideal_yy/zygh/occupation/getOccupationInfoById\\?occupation_id=\\d+").toString() != null) {
			processOccupationInfo(page);
		} else if (page.getUrl().regex("http://www\\.edusoa\\.com/dsideal_yy/zygh/occupation/getOccupationListByIndustryId\\?industry_id=\\d+").toString() != null) {
			processOccupationList(page);
		} else if (page.getUrl().toString().equals("http://www.edusoa.com/dsideal_yy/zygh/occupation/getIndustryList")) {
			processIndustryList(page);
		}

		// page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
		// page.putField("author",
		// page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
		// page.putField("name",
		// page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
		// if (page.getResultItems().get("name")==null){
		// //skip this page
		// page.setSkip(true);
		// }
		// page.putField("readme",
		// page.getHtml().xpath("//div[@id='readme']/tidyText()"));
	}

	// http://www.edusoa.com/dsideal_yy/zygh/occupation/getOccupationListByIndustryId?industry_id=1
	// http://www.edusoa.com/dsideal_yy/zygh/occupation/getOccupationInfoById?occupation_id=21
	public void processOccupationList(Page page) {
//		System.out.println(page.getRawText());
		String str = page.getRawText();
//		str = str + ",{\"industry_id\":"+page.getUrl().toString().split("=")[1]+"}";
		page.putField("OccupationList", str);
		List<String> li = new JsonPathSelector("$.table_List[*].occupation_id").selectList(page.getRawText());
		for(String l:li){
			String link = "http://www.edusoa.com/dsideal_yy/zygh/occupation/getOccupationInfoById?occupation_id="+l;
			page.addTargetRequest(new Request(link).setPriority(2));
		}
		
	}

	public void processOccupationInfo(Page page) {
		String str = page.getRawText();
		page.putField("OccupationInfo", str);
	}

	public void processIndustryList(Page page) {
		String str = page.getRawText();
		page.putField("IndustryList", str);
		List<String> li = new JsonPathSelector("$.table_List[*].industry_id").selectList(page.getRawText());
		for(String l:li){
			String link = "http://www.edusoa.com/dsideal_yy/zygh/occupation/getOccupationListByIndustryId?industry_id="+l;
			page.addTargetRequest(new Request(link).setPriority(1));
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new Edusoa())
						.addPipeline(new MongoDBPipeline())
//						.addPipeline(new JsonFilePipeline("F:\\"))
						.addUrl("http://www.edusoa.com/dsideal_yy/zygh/occupation/getIndustryList")
						.thread(5)
						.run();
	}
}
