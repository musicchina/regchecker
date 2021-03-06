package com.jisucloud.clawler.regagent.service.impl.money;

import java.util.Map;


import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;
import com.deep077.spiderbase.selenium.mitm.HookTracker;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

@PapaSpiderConfig(
		home = "gw.com.cn", 
		message = "大智慧-中国90%的投资者选择用大智慧炒股软件,提供沪深港美股实时高速行情，Level-2行情，财经热点新闻及全方面投资资讯。", 
		platform = "dazhihui", 
		platformName = "大智慧", 
		tags = { "理财", "炒股" }, 
		testTelephones = { "18763623587", "18212345678" })
public class DaZhiHuiSpider extends PapaSpider implements AjaxHook{
	
	private ChromeAjaxHookDriver chromeDriver;
	private boolean checkTel = false;

	@Override
	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(true, true);
			chromeDriver.get("https://i.gw.com.cn/UserCenter/page/account/forgetPass?source=1&redirect_uri=");
			chromeDriver.addAjaxHook(this);
			chromeDriver.findElementById("mobile").sendKeys(account);
			chromeDriver.mouseClick(chromeDriver.findElementById("sendCode"));smartSleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTel;
	}

	@Override
	public boolean checkEmail(String account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HookTracker getHookTracker() {
		// TODO Auto-generated method stub
		return HookTracker.builder().addUrl("/sms/").build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		// TODO Auto-generated method stub
		if (messageInfo.getOriginalUrl().contains("/sms/")) {
			checkTel = true;
			return DEFAULT_HTTPRESPONSE;
		}
		return null;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		
	}

}
