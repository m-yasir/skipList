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

    // SCRAPING URLS!
    private static String beginnerUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3ABEGINNER%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String intermediateUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3AINTERMEDIATE%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String advancedUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3AADVANCED%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String expertUrl = "https://www.lynda.com/Developer-training-tutorials/50-0.html?category=advanced_339";

    // CHROME WEBDRIVER URL
    private static String webDriverDir = ".\\src\\main\\res\\chromedriver.exe";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver",webDriverDir);

        // TODO: Implement a memory friendly method to getCourses from the site

        LinkedHashSet<Element> beginnerCourses = getCourses(new TestScraping(beginnerUrl, ".rc-OfferingCard"));
        LinkedHashSet<Element> intermediateCourses = getCourses(new TestScraping(intermediateUrl, ".rc-OfferingCard"));
        LinkedHashSet<Element> advancedCourses = getCourses(new TestScraping(advancedUrl, ".rc-OfferingCard"));
        LinkedHashSet<Element> expertCourses = getCourses(new ExpertScrapping(expertUrl, ".card-list-style"));

        printCourses("beginner", beginnerCourses, ".product-name");
        printCourses("intermediate", intermediateCourses, ".product-name");
        printCourses("advanced", advancedCourses, ".product-name");
        printCourses("expert", expertCourses, "h3");

    }

    private static void printCourses(String levelOfCourse, LinkedHashSet<Element> list, String query) {
        levelOfCourse = levelOfCourse.substring(0, 1).toUpperCase() + levelOfCourse.substring(1).toLowerCase();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("***************** Available " +  levelOfCourse +" Courses: ***************** ");

        list.forEach((e) -> {
            String el = e.select(query).text();
            System.out.println(el);
        });
    }

    private static LinkedHashSet<Element> getCourses(Thread connectionThread) {
        connectionThread.start();

        try {
            connectionThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return ((ScrapingTester) connectionThread).elements;
    }
}

class TestScraping extends ScrapingTester {

    private String url, className;

    public TestScraping(String url, String className) {
        super();
        this.url = url;
        this.className = className;
    }

    // TODO: Handle Exceptions according to their type
    @Override
    public void run() {

        ScrapingTester.elements = new LinkedHashSet<Element>();
        try {
            Scrapper.initWebDrive(url); // TODO: Add proper error handling if browser doesn't open or crashes or is closed by user on purpose.
            ScrapingTester.elements.addAll(Scrapper.fetchDataByQuerySelector(url, className));
            Scrapper.scrollBrowserBy(2000);
            Scrapper.setDocument();
            ScrapingTester.elements.addAll(Scrapper.getElements(className));
        } catch (Exception e) { // IOException | InterruptedThreadException | IllegalArgumentException | ExceptionInInitializerError | NullPointerException
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        Scrapper.closeBrowser();

        this.interrupt();
    }
}

class ExpertScrapping extends ScrapingTester {

    private String url, className;

    public ExpertScrapping(String url, String className) {
        super();
        this.url = url;
        this.className = className;
    }

    @Override
    public void run() {
        ScrapingTester.elements = new LinkedHashSet<Element>();

        try {
            Scrapper.initWebDrive(url); // TODO: Add proper error handling if browser doesn't open or crashes or is closed by user on purpose.
            ScrapingTester.elements.addAll(Scrapper.fetchDataByQuerySelector(url, className));
        } catch (Exception e) { // IOException | IllegalArgumentException | InterruptedException | ExceptionInInitializerError | NullPointerException
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        Scrapper.closeBrowser();

        this.interrupt();
    }
}

class ScrapingTester extends Thread {
    public static LinkedHashSet<Element> elements;

    public ScrapingTester() {}
}
