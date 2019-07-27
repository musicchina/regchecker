package com.jisucloud.clawler.regagent.service.impl.news;

import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.HookTracker;
import com.google.common.collect.Sets;
import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.UsePapaSpider;
import com.jisucloud.clawler.regagent.util.StringUtil;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@UsePapaSpider
public class MeiPaiSpider extends PapaSpider implements AjaxHook{

	private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

	@Override
	public String message() {
		return "美拍是一款可以直播、制作小视频的受年轻人喜爱的软件。美拍 - 高颜值手机直播+超火爆原创视频。2014年5月上线后，连续24天蝉联App Store免费总榜冠军，并成为当月App Store全球非游戏类下载量第一。";
	}

	@Override
	public String platform() {
		return "meipai";
	}

	@Override
	public String home() {
		return "meipai.com";
	}

	@Override
	public String platformName() {
		return "美拍";
	}

	@Override
	public String[] tags() {
		return new String[] {"美图" , "美颜", "工具" , "短视频"};
	}
	
	@Override
	public boolean checkTelephone(String account) {
		try {
			String url = "https://www.meipai.com/users/login";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("phone", account)
	                .add("password", "adasd1231")
	                .add("phone_flag", "86")
	                .add("auto_login", "false")
	                .add("referer", "https://www.meipai.com/")
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", CHROME_USER_AGENT)
					.addHeader("Referer", " https://www.meipai.com/")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = StringUtil.unicodeToString(response.body().string());
			if (!res.contains("21303")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkTel;
	}


	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

	@Override
	public Set<String> getTestTelephones() {
		return Sets.newHashSet("18210538513", "15161509916");
	}

	@Override
	public HookTracker getHookTracker() {
		// TODO Auto-generated method stub
		return HookTracker.builder().addUrl("oauth/access_token.json").isPost().build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		return null;
	}
	boolean checkTel;
	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		checkTel = !contents.getTextContents().contains("还未被注册");
	}

}
