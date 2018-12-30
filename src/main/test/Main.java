package main.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Main {

    private static String beginnerUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3ABEGINNER%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String intermediateUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3AINTERMEDIATE%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String advancedUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3AADVANCED%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String webDriverDir = ".\\src\\main\\res\\chromedriver.exe";
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver",webDriverDir);

        // TODO: Implement a memory friendly method to getCourses from the site

        LinkedHashSet<Element> beginnerCourses = getCourses(new TestScraping(beginnerUrl));
        LinkedHashSet<Element> intermediateCourses = getCourses(new TestScraping(intermediateUrl));
        LinkedHashSet<Element> advancedUrlCourses = getCourses(new TestScraping(advancedUrl));

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("***************** Available Beginner Courses: *****************");

        beginnerCourses.forEach((e) -> {
            System.out.println(e.getElementsByClass("product-name").text());
        });

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("***************** Available Intermediate Courses: *****************");

        intermediateCourses.forEach((e) -> {
            System.out.println(e.getElementsByClass("product-name").text());
        });

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("***************** Available Advanced Courses: ***************** ");

        advancedUrlCourses.forEach((e) -> {
            System.out.println(e.getElementsByClass("product-name").text());
        });

    }

    private static LinkedHashSet<Element> getCourses(Thread connectionThread) {
        connectionThread.start();

        try {
            connectionThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return ((TestScraping) connectionThread).elements;
    }
}

class TestScraping extends Thread {

    public static LinkedHashSet<Element> elements;

    private String url;

    public TestScraping(String url) {
        super();
        this.url = url;
    }

    // TODO: Handle Exceptions according to their type
    @Override
    public void run() {

        elements = new LinkedHashSet<Element>();

        try {
            Scrapper.initWebDrive(url); // TODO: Add proper error handling if browser doesn't open or crashes or is closed by user on purpose.
            elements.addAll(Scrapper.fetchDataByQuerySelector(url, ".rc-OfferingCard"));
            Scrapper.scrollBrowserBy(2000);
            Scrapper.setDocument();
            elements.addAll(Scrapper.getElements(".rc-OfferingCard"));
        } catch (Exception e) { // IOException | IllegalArgumentException | ExceptionInInitializerError | NullPointerException
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        Scrapper.closeBrowser();

        this.interrupt();
    }
}
