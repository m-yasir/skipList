package main.test;

import main.lib.MyLinkedList;
import main.lib.SkipList;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    // SCRAPING URLS!
    private static String beginnerUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3ABEGINNER%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String intermediateUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3AINTERMEDIATE%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String advancedUrl = "https://www.coursera.org/browse/computer-science?facets=skillNameMultiTag%2CjobTitleMultiTag%2CdifficultyLevelTag%3AADVANCED%2Clanguages%2CentityTypeTag%2CpartnerMultiTag%2CcategoryMultiTag%3Acomputer-science%2CsubcategoryMultiTag&sortField=";
    private static String expertUrl = "https://www.lynda.com/Developer-training-tutorials/50-0.html?category=advanced_339";
    private static String[] skillLevels = {"beginner", "intermediate", "advanced", "expert"};

    // CHROME WEBDRIVER URL
    private static String webDriverDir = ".\\src\\main\\res\\chromedriver.exe";

    public static void main(String[] args) {

        // This is necessary, it's set's the property of the chrome driver directory so that ChromeWebDriver class's object can use it.
        System.setProperty("webdriver.chrome.driver", webDriverDir);

        // TODO: Implement a memory friendly method to getCourses from the site

        // COMMENT SOME COURSES and other place where there's a reference to these courses (in this same scope) if your internet is slow and don't want to check all the offerings.

        SkipList<Element> beginnerCourses = new SkipList<Element>("beginner", getCourses(new TestScraping(beginnerUrl, ".rc-OfferingCard")));
        SkipList<Element> intermediateCourses = new SkipList<Element>("intermediate", getCourses(new TestScraping(intermediateUrl, ".rc-OfferingCard")));
        SkipList<Element> advancedCourses = new SkipList<Element>("advanced", getCourses(new TestScraping(advancedUrl, ".rc-OfferingCard")));
        SkipList<Element> expertCourses = new SkipList<Element>("expert", getCourses(new ExpertScrapping(expertUrl, ".card-list-style")));

        HashMap<String, CourseData> courses = new HashMap<>();

        courses.put("beginner", new CourseData(".product-name", beginnerCourses));
        courses.put("intermediate", new CourseData(".product-name", intermediateCourses));
        courses.put("advanced", new CourseData(".product-name", advancedCourses));
        courses.put("expert", new CourseData("h3", expertCourses));

        Scanner s = new Scanner(System.in);
        System.out.print("Please enter your skill level (Beginner, Intermediate, Advanced or Expert): ");
        String level = "";
        while (true) {
            level = s.next();
            if (!courses.containsKey(level)) {
                level = "";
                System.out.println("Invalid Skill level, please try again! press Q to quit");
                if (level.equalsIgnoreCase("q")) System.exit(0);
                continue;
            }
            break;
        }

        selectCourses(level, courses.get(level).skipList, courses.get(level).querySelectName, s);

    }

    private static void selectCourses(String levelOfCourse, SkipList<Element> list, String query, Scanner s) {
        levelOfCourse = levelOfCourse.substring(0, 1).toUpperCase() + levelOfCourse.substring(1).toLowerCase();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("***************** Available " +  levelOfCourse +" Courses: ***************** ");

        MyLinkedList<Element> ll = list.getItem(levelOfCourse.toLowerCase());

        String ch = "";

        String el = "";

        for (int i = 0; i < ll.getSize() && ch.isEmpty(); i++) {
            el = ll.find(i).getData().select(query).text();
            System.out.println(el);
            System.out.print("Press enter to see next course! Enter any other key to fetch the current course! or Press Q to exit");
            if (i == 0) s.nextLine();
            ch = s.nextLine();
            if (ch.equalsIgnoreCase("q")) System.exit(0);
        }

        // Below code searches the course in chrome!

        WebDriver webDriver = new ChromeDriver();

        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        webDriver.get("https://www.google.com.pk/");

        WebElement webElement = webDriver.findElement(By.name("q"));
        webElement.sendKeys(el);
        webElement.submit();
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

class CourseData {
    public String querySelectName;
    public SkipList<Element> skipList;

    public CourseData(String querySelectName, SkipList<Element> skipList) {
        this.querySelectName = querySelectName;
        this.skipList = skipList;
    }
}

class Loader extends Thread {
    @Override
    public void run() {
        System.out.print("Fetching data");
        int i = 4;
        while (true) {
            System.out.print(".");
            try {
                this.sleep(1000);
            } catch (Exception e) { }
            if (i == 0) {
                System.out.print("\b\b\b\b\r");
                System.out.print("Fetching data");
                i = 4;
            }
            i--;
        }
    }
}
