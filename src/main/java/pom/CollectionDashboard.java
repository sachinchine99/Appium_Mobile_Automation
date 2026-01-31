package pom;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import utility.UIActions;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;


import java.util.Collections;
import java.util.List;

public class CollectionDashboard extends UIActions {

    @FindBy(xpath = "//android.view.View[contains(@content-desc, \"Receivables\")]") WebElement receivableFinance_boxButton;
    @FindBy(xpath = "//android.view.View[contains(@content-desc,\"Overdue\")]") WebElement overdueReceiable_boxButton;
    @FindBy(xpath = "//android.widget.Button") WebElement filter_iconButton;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Customer Name\"]") WebElement customerNameFilter_menuList;
    @FindBy(xpath = "(//android.view.View[@content-desc=\"Customer Name\"])[2]") WebElement customerName_dropDown;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Cancel\"]/preceding-sibling::android.view.View//android.view.View//android.view.View")
    List<WebElement> customerNameDropDown_options;

    public CollectionDashboard(){
        PageFactory.initElements(driver,this);
    }

    public void handle_customer_name_dropdown_list() throws InterruptedException {
        // Clicking elements to open the customer name dropdown list
        click(receivableFinance_boxButton, "Receivable [Finance] button is missing");
        click(overdueReceiable_boxButton, "Overdue [Receivable] button is missing");
        click(filter_iconButton, "Filter icon button is missing");
        click(customerNameFilter_menuList, "Customer Name Filter menu list is missing");
        click(customerName_dropDown, "Customer Name dropdown is missing");

        // Log the initial size of the dropdown list
        System.out.println("Initial dropdown list size: " + customerNameDropDown_options.size());

        // List to store unique customer names
        List<String> uniqueCustomerNames = new ArrayList<>();
        String lastProcessedText = "";

        // Loop to scroll and fetch all dropdown options
        for (int attempt = 0; attempt < 25; attempt++) {
            for (WebElement option : customerNameDropDown_options) {
                String customerName = option.getAttribute("content-desc");

                // Add to list only if not already present
                if (!uniqueCustomerNames.contains(customerName)) {
                    uniqueCustomerNames.add(customerName);
                }
            }

            // Get the first element after scrolling to check for new data
            if (!customerNameDropDown_options.isEmpty()) {
                String currentProcessedText = customerNameDropDown_options.get(0).getAttribute("content-desc");
                System.out.println("Last Processed: " + lastProcessedText + " | Current Processed: " + currentProcessedText);

                // If new elements are found, scroll further
                if (!lastProcessedText.equals(currentProcessedText)) {
                    scrollByElement(customerNameDropDown_options.get(customerNameDropDown_options.size() - 4),
                            customerNameDropDown_options.get(0));
                    lastProcessedText = currentProcessedText;
                } else {
                    break; // Exit loop if no new elements appear
                }
            }
            System.out.println("===============================================================================");
        }

        // Sorting the extracted names for validation
        List<String> sortedCustomerNames = new ArrayList<>(uniqueCustomerNames);
        Collections.sort(sortedCustomerNames);

        // Print extracted and sorted lists
        System.out.println("Extracted Customer Names: " + uniqueCustomerNames);
        System.out.println("Sorted Customer Names: " + sortedCustomerNames);

        // Assertion to check if dropdown values are sorted
        Assert.assertEquals(uniqueCustomerNames, sortedCustomerNames, "Customer names are not in alphabetical order");
    }



}
