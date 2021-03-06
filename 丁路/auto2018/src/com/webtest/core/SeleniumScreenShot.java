package com.webtest.core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
/**
 * author:lihaunzhen
 * 截屏类
 */
public class SeleniumScreenShot {
    public WebDriver driver;
 
    public SeleniumScreenShot(WebDriver driver) {
        this.driver = driver;
    }
    //截屏功能，名字为当前时间
    public void screenShot(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String nowDateTime=sdf.format(new Date());
		
		File s_file=((TakesScreenshot)driver).
				getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(s_file, new File("D:\\demo\\"+nowDateTime+".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}