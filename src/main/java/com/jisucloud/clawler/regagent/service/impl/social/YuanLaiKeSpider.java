package com.jisucloud.clawler.regagent.service.impl.social;

import com.deep007.spiderbase.okhttp.OKHttpUtil;
import com.google.common.collect.Sets;
import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.UsePapaSpider;
import com.jisucloud.clawler.regagent.util.PapaSpiderTester;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;

@Slf4j
@UsePapaSpider
public class YuanLaiKeSpider extends PapaSpider {

	private OkHttpClient okHttpClient = OKHttpUtil.createOkHttpClient();

	@Override
	public String message() {
		return "缘来客交友网是拥有多年口碑的大型同城约会交友网站,缘来客手机版专为同城男女提供同城约会手机登录,缘来客交友网拥有1200万同城交友会员,使用缘来客手机版登录。";
	}

	@Override
	public String platform() {
		return "ylike";
	}

	@Override
	public String home() {
		return "ylike.com";
	}

	@Override
	public String platformName() {
		return "缘来客";
	}

	@Override
	public String[] tags() {
		return new String[] {"单身交友", "社区"};
	}
	
	@Override
	public Set<String> getTestTelephones() {
		return Sets.newHashSet("18515290717", "18210538513");
	}
	
	public static void main(String[] args) {
		PapaSpiderTester.testingWithPrint(YuanLaiKeSpider.class);
	}
	
	private String getRegCck() throws Exception {
		String url = "http://www.ylike.com/Reg_Base.do?action=OK";
		Request request = new Request.Builder().url(url)
				.addHeader("User-Agent", CHROME_USER_AGENT)
				.addHeader("Host", "www.ylike.com")
				.build();
		Response response = okHttpClient.newCall(request).execute();
		String res = response.body().string();
		return Jsoup.parse(res).getElementById("RegCck").attr("value");
	}

	@Override
	public boolean checkTelephone(String account) {
		try {
			String url = "http://www.ylike.com/g/getCheck_Data.do?dt=" + System.currentTimeMillis();
			FormBody formBody = new FormBody
	                .Builder()
	                .add("val", account)
	                .add("cln", "UserName")
	                .add("RegCck", getRegCck())
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", CHROME_USER_AGENT)
					.addHeader("Host", "www.ylike.com")
					.addHeader("Referer", "http://www.ylike.com/Reg_Base.do?action=OK")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			if (res.contains("已存在")) {
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
