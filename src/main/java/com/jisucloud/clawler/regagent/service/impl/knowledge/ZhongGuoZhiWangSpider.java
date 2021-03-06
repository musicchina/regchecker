package com.jisucloud.clawler.regagent.service.impl.knowledge;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;


@PapaSpiderConfig(
		home = "cnki.net", 
		message = "中国知网，是国家知识基础设施的概念，由世界银行于1998年提出。CNKI工程是以实现全社会知识资源传播共享与增值利用为目标的信息化建设项目。", 
		platform = "cnki", 
		platformName = "中国知网", 
		tags = { "中国学术文献", "外文文献", "学位论文" }, 
		testTelephones = { "13910200000", "18212345678" },
		ignoreTestResult = true)
public class ZhongGuoZhiWangSpider extends PapaSpider {

	
	
	private String name = null;

	public boolean checkTelephone(String account) {
		try {
			String url = "http://my.cnki.net/elibregister/Server.aspx?mobile="+account+"&temp="+System.currentTimeMillis()+"&operatetype=3";
			FormBody formBody = new FormBody
	                .Builder()
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "my.cnki.net")
					.addHeader("Referer", "http://my.cnki.net/elibregister/commonRegister.aspx")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			return res.contains("2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		if (name != null) {
			Map<String, String> fields = new HashMap<>();
			fields.put("name" , name);
			return fields;
		}
		return null;
	}

}
