package com.zilaijuan.Start;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MongoDBPipeline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		MongoJDBC jdbc = new MongoJDBC();
		DB db = jdbc.connection();
		String tableName = "";

		int priority = (int) resultItems.getRequest().getPriority();
		String industry_id = "";
		String str = "";
		switch (priority) {
		case 0:
			str = resultItems.get("IndustryList");
			tableName = "IndustryList";
			break;
		case 1:
			str = resultItems.get("OccupationList");
			tableName = "OccupationList";
			industry_id = resultItems.getRequest().getUrl().toString().split("=")[1];
			break;
		case 2:
			str = resultItems.get("OccupationInfo");
			tableName = "OccupationInfo";
			break;

		default:
			break;
		}
		System.out.println(str);
		DBCollection collection = db.getCollection(tableName);
		DBObject query = (BasicDBObject) JSON.parse(str);
		if (priority == 1) {
			query.put("industry_id", industry_id);
		}
		// collection.save(query);
		collection.save(query);
	}
}
