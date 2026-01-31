package utility;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

public class UIActions extends Driver {

    public void click(WebElement el, String errorMessage) throws InterruptedException {
            try {
                explicitlyWait(el, 15, errorMessage);  // Wait until element is ready
                el.click();
                System.out.println("Clicked on: " + errorMessage.replace("missing", ""));
                System.out.println(":-------------------------------------------------------------------------------------------------------:");
                return; // Success: exit method immediately, no failure
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                System.out.println("\nRetrying click due to " + e.getClass().getSimpleName() + "...");
                System.out.println(":-------------------------------------------------------------------------------------------------------:");
                Thread.sleep(3000); // Wait before retrying
            } catch (TimeoutException e) {
                Assert.fail(errorMessage + " | TimeoutException: " + e.getMessage());
                return;
            }


        System.out.println(":-------------------------------------------------------------------------------------------------------:");
    }


    public void clickIfDisplayed(WebElement el, int waitForSec, String errorMessage) {
        try {
            explicitlyWait(el, waitForSec, errorMessage);
            if (el.isDisplayed()) {
                el.click();
                System.out.println("Clicked on: " + errorMessage.replace("missing", ""));
            }
        } catch (Exception e) {
            System.out.println("Not displayed, skipping: " + errorMessage.replace("missing", ""));
        }
        System.out.println(":-------------------------------------------------------------------------------------------------------:");
    }

    public void sendKeys(WebElement el, String input, String errorMessage) {
        try {
            explicitlyWait(el, 15, errorMessage);
            el.click();
            el.sendKeys(input);
            System.out.println("Sent keys to: " + errorMessage.replace("missing", ""));
        } catch (TimeoutException e) {
            Assert.fail(errorMessage + " | " + e.getMessage());
        }
        System.out.println(":-------------------------------------------------------------------------------------------------------:");
    }

    public void explicitlyWait(WebElement el, int durationInSec, String message) {
        LocalDateTime start = LocalDateTime.now();
        System.out.print("Waiting for element: " + message.replace("missing", "") + " for [" + durationInSec + " sec]");

        try {
            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(durationInSec));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(el));
            long millis = Duration.between(start, LocalDateTime.now()).toMillis();
            System.out.print(" | Found in [" + millis + " ms]");
        } catch (TimeoutException e) {
            System.out.print(" | Element not found within timeout");
        }

        System.out.println();

    }

    public void implicitlyWait(int durationInSec) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(durationInSec));
        System.out.println("Set implicit wait: " + durationInSec + " sec");
    }

    public void scrollByElement(WebElement from, WebElement to) throws InterruptedException {

        explicitlyWait(from,10,"");
        explicitlyWait(to,10,"");

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Get the center coordinates of the start element
        Point startPoint = from.getLocation();
        int startX = startPoint.getX() + (from.getSize().getWidth() / 2);
        int startY = startPoint.getY() + (from.getSize().getHeight() / 2);

        // Get the center coordinates of the end element
        Point endPoint = to.getLocation();
        int endX = endPoint.getX() + (to.getSize().getWidth() / 2);
        int endY = endPoint.getY() + (to.getSize().getHeight() / 2);

        System.out.println("Scroll by coordiante: From["+startX+","+startY+"]"+" --> To["+endX+","+endY+"]");

        Point startCoordinate = new Point(startX, startY);
        Point endCoordinate = new Point (endX, endY);
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), startCoordinate.getX(), startCoordinate.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                PointerInput.Origin.viewport(), endCoordinate.getX(), endCoordinate.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }
    public void scrollByCoordinate(int from_x, int from_y, int to_x, int to_y) throws InterruptedException {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        System.out.println("Scroll down by coordiante: From["+from_x+","+from_y+"]"+" --> To["+to_x+","+to_y+"]");

        Point startCoordinate = new Point(from_x, from_y);
        Point endCoordinate = new Point (to_x, to_y);
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), startCoordinate.getX(), startCoordinate.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                PointerInput.Origin.viewport(), endCoordinate.getX(), endCoordinate.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }

    public HashMap getElementCoordinate(WebElement element, String errorMessage){
        HashMap coordinate = new HashMap();
        explicitlyWait(element,10, errorMessage);

        // Get the center coordinates of the start element
        Point location = element.getLocation();
        int x = location.getX() + (element.getSize().getWidth() / 2);
        int y = location.getY() + (element.getSize().getHeight() / 2);
        coordinate.put("X",x);
        coordinate.put("Y",y);
        return coordinate;
    }



}
