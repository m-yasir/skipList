package main.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Scrapper {
    // Properties needed for a connection, they are made to avoid redundancy.
    private static Document document;
    private static WebDriver webDriver;
    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0";
    private static String REFERRER = "http://www.google.com";

    private Scrapper() {}

    public static void initWebDrive(String url) {
        webDriver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        webDriver.get(url);
    }

    public static void setDocument() throws ExceptionInInitializerError {
        if (webDriver == null) throw new ExceptionInInitializerError("WEBDRIVER NOT INITIALIZED!");
        WebElement webElement = webDriver.findElement(By.tagName("body"));

        document = Jsoup.parse(webElement.getAttribute("innerHTML"));
    }

    public static void scrollBrowserBy(int y) {
        ((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + y + ")");
    }

    public static void navigate(String url) throws InterruptedException {
        webDriver.navigate().to(url);
    }

    public static ArrayList<Element> fetchDataByQuerySelector(String url, String querySelectorParam) throws IllegalArgumentException, IOException {
        if (webDriver == null) initWebDrive(url);
        setDocument();
        return getElements(querySelectorParam);
    }

    public static ArrayList<Element> getElements(String querySelectorParam) throws IllegalArgumentException, IOException {
        if (webDriver == null) throw new ExceptionInInitializerError("WEBDRIVER NOT INITIALIZED!");

        Elements elements = document.select(querySelectorParam);

        ArrayList<Element> elementArrayList = new ArrayList<>();
        elementArrayList.addAll(elements);
        return elementArrayList;
    }

    public static void closeBrowser() {
        webDriver.close();
    }
}
