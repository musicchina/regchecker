package com.jisucloud.clawler.regagent.service.impl.game;

import com.jisucloud.clawler.regagent.service.PapaSpider;
import com.jisucloud.deepsearch.selenium.Ajax;
import com.jisucloud.deepsearch.selenium.AjaxListener;
import com.jisucloud.deepsearch.selenium.ChromeAjaxListenDriver;
import com.jisucloud.deepsearch.selenium.HeadlessUtil;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ChangYouWangSpider implements PapaSpider {

	private ChromeAjaxListenDriver chromeDriver;
	private boolean checkTel = false;
	
	@Override
	public String message() {
		return "畅游有限公司(纳斯达克股票交易代码:CYOU),中国在线游戏开发和运营商之一,自主研发的《新天龙八部》是中国最受欢迎的大型多人在线角色扮演游戏之一。";
	}

	@Override
	public String platform() {
		return "changyou";
	}

	@Override
	public String home() {
		return "changyou.com";
	}

	@Override
	public String platformName() {
		return "畅游网";
	}

	@Override
	public String[] tags() {
		return new String[] {"游戏"};
	}

//	public static void main(String[] args) throws InterruptedException {
//		System.out.println(new ChangYouWangSpider().checkTelephone("15700102865"));
//		System.out.println(new ChangYouWangSpider().checkTelephone("18210538513"));
//	}

	@Override
	public boolean checkTelephone(String account) {
		try {
			chromeDriver = HeadlessUtil.getChromeDriver(true, null, null);
			String url = "http://zhuce.changyou.com/reg.act?gameType=PE-ZHPT&invitecode=&regWay=phone&suffix=";
			chromeDriver.setAjaxListener(new AjaxListener() {
				
				@Override
				public String matcherUrl() {
					return "baseReg/checkCnIsUsed.act";
				}
				
				@Override
				public void ajax(Ajax ajax) throws Exception {
					checkTel = ajax.getResponse().contains("used");
				}
				
				@Override
				public String[] blockUrl() {
					return null;
				}
			});
			chromeDriver.get(url);
			Thread.sleep(3000);
			chromeDriver.findElementById("securityPhone").sendKeys(account);
			chromeDriver.findElementById("passwd_phone").click();
			Thread.sleep(3000);
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
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

}
