package com.zilaijuan.edusoa.university;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.*;
import com.alibaba.fastjson.JSON;
import com.zilaijuan.tools.MongoJDBC;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.selector.Json;

@SuppressWarnings("deprecation")
public class MongoDBPipeline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		MongoJDBC jdbc = new MongoJDBC();
		DB db = jdbc.connection();
		String tableName = "UniversityList";
		String str = resultItems.get("UniversityList");;
		System.out.println(str);
		DBCollection collection = db.getCollection(tableName);
		DBObject query = null;
		
		JSONArray jsonArray = JSON.parseArray(str);

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject temp =  jsonArray.getJSONObject(i);
            query = (BasicDBObject) com.mongodb.util.JSON.parse(temp.toString());
//            query = (BasicDBObject) JSON.parse(temp.toString());
    		// collection.save(query);
    		collection.save(query);
        }
		
	}
}
