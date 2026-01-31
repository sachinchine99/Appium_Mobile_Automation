package pom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utility.UIActions;


public class LoginPage extends UIActions {
    @FindBy(xpath = "//android.widget.Button[@resource-id=\"com.android.permissioncontroller:id/permission_allow_button\"]") WebElement notificationAllow_button;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Skip\"]") WebElement skip_button;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Continue to Login\"]") WebElement continueToLogin_button;
    @FindBy(xpath = "(//android.view.View[@content-desc=\"Email\"]/following-sibling::android.view.View/following-sibling::android.widget.EditText)[1]") WebElement email_button;
    @FindBy(xpath = "(//android.view.View[@content-desc=\"Email\"]/following-sibling::android.view.View/following-sibling::android.widget.EditText)[2]") WebElement pass_button;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Login\"]") WebElement login_button;
    @FindBy(xpath = "//android.view.View[@content-desc=\"cancelIcon\"]/android.widget.ImageView") WebElement closePopUp_iconButton;
    @FindBy(xpath = "//android.view.View[@content-desc=\"icon3\"]") WebElement profileMenuList_iconButton;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Logout\"]") WebElement logoutProfileMenuList_button;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Confirm\"]") WebElement confirmWarningPopUp_button;
    @FindBy(xpath = "(//android.view.View[@content-desc=\"godavari enterprises\"])[2]") WebElement godavariProjectName_text;
    @FindBy(xpath = "(//android.view.View[@content-desc=\"Nikhil\"])[3]") WebElement nikhilProjectName_text;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Measurement\"]") WebElement measurementMenuList;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Logout\"]") WebElement logoutMenuList;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Hide Entries\"]") WebElement hideEntriesMenuList;

    public LoginPage(){
        PageFactory.initElements(driver,this);
    }
    public void login() throws InterruptedException {
        clickIfDisplayed(notificationAllow_button, 1,"Notification button missing");
        clickIfDisplayed(skip_button, 1,"Skip button missing");
        clickIfDisplayed(continueToLogin_button, 1,"Continue to login button missing");
        sendKeys(email_button,"nikhil@kolonizer.com","email tab missing");
        sendKeys(pass_button,"123","Password tab missing");
        click(login_button,"Login button missing");
        clickIfDisplayed(closePopUp_iconButton,2,"Close pop up icon button missing");
    }
    public void loginWithoutReset() throws InterruptedException {
        sendKeys(email_button,"nikhil@kolonizer.com","email tab missing");
        sendKeys(pass_button,"123","Password tab missing");
        click(login_button,"Login button missing");

    }

    public void logout() throws InterruptedException {
        click(profileMenuList_iconButton,"Profile dashboard menu list icon button missing");
        scrollByElement(hideEntriesMenuList,measurementMenuList);
        click(logoutProfileMenuList_button,"logout profile menu list icon button missing");
        click(confirmWarningPopUp_button,"Confirm warning pop up button missing");
    }


}
