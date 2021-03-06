package com.jisucloud.clawler.regagent.service.impl.photo;

import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;
import com.deep077.spiderbase.selenium.mitm.HookTracker;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;
import com.jisucloud.clawler.regagent.util.OCRDecode;
import com.jisucloud.clawler.regagent.util.StringUtil;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.util.Map;


import org.openqa.selenium.WebElement;

@Slf4j
@PapaSpiderConfig(
		home = "58pic.cn", 
		message = "千图网(www.58pic.com) 是专注免费设计素材下载的网站!提供矢量图素材,矢量背景图片,矢量图库,还有psd素材,PS素材,设计模板,设计素材,PPT素材,以及网页素材,网站。", 
		platform = "58pic", 
		platformName = "千图网", 
		tags = { "工具", "海报设计" }, 
		testTelephones = { "13910000000", "18212345678" })
public class QianTuWangSpider extends PapaSpider implements AjaxHook{
	
	private ChromeAjaxHookDriver chromeDriver;
	private boolean checkTel = false;
	private boolean vcodeSuc = false;//验证码是否正确
	
	private String getImgCode() {
		for (int i = 0 ; i < 3; i++) {
			try {
				WebElement img = chromeDriver.findElementByCssSelector("#img-captcha2");
				img.click();smartSleep(1000);
				byte[] body = chromeDriver.screenshot(img);
				return OCRDecode.decodeImageCode(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}


	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(false, true);
			chromeDriver.addAjaxHook(this);
			chromeDriver.get("https://www.58pic.com/login#");smartSleep(2000);
			chromeDriver.findElementById("parbox-toPhoneLogin").click();smartSleep(2000);
			chromeDriver.findElementById("pal-toAccountLoginBtn").click();smartSleep(2000);
			WebElement nameInputArea = chromeDriver.findElementByCssSelector("#account");
			nameInputArea.sendKeys(account);
			chromeDriver.findElementByCssSelector("#passwd").sendKeys("xas021na");
			for (int i = 0 ; i < 5 ; i ++) {
				if (chromeDriver.checkElement("#img-captcha2")) {
					WebElement captcha = chromeDriver.findElementByCssSelector("#img-code2");
					captcha.clear();
					captcha.sendKeys(getImgCode());
				}
				chromeDriver.findElementByCssSelector("#submit-passwd").click();smartSleep(3000);
				if (vcodeSuc) {
					break;
				}
			}
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

	@Override
	public HookTracker getHookTracker() {
		// TODO Auto-generated method stub
		return HookTracker.builder().addUrl("loginByPasswdNew").isPost().build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		// TODO Auto-generated method stub
		String res = StringUtil.unicodeToString(contents.getTextContents());
		if (!res.contains("验证码")) {
			vcodeSuc = true;
			checkTel = res.contains("密码");
		}
	}

}
