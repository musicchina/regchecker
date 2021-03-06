package com.jisucloud.clawler.regagent.service.impl.borrow;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import java.util.Map;



@Slf4j
@PapaSpiderConfig(
		home = "jintouxing.com", 
		message = "金投行4周年 让钱变得有温度 关注微信服务号 资金变动尽在掌握 智选专区 智能出借◎一键实现安全有保障 国有全资,3亿资本实力, 财险公司提供保证保险。", 
		platform = "jintouxing", 
		platformName = "金投行", 
		tags = { "P2P", "借贷" }, 
		testTelephones = { "15985268904", "18212345678" })
public class JinTouHangSpider extends PapaSpider {

	
	
	public boolean checkTelephone(String account) {
		try {
			String url = "https://www.jintouxing.com/register/checkUserName.htm";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("username", account)
	                .add("action", "mobileCheck")
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "www.jintouxing.com")
					.addHeader("Referer", "https://www.jintouxing.com/register/registerIndex.htm")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (response.body().string().contains("已被注册")) {
				return true;
			}
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
		return null;
	}

}
