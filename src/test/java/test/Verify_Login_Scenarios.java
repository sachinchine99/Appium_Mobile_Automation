package test;

import BaseClass.BaseClass;

import org.testng.annotations.Test;
import pom.LoginPage;

public class Verify_Login_Scenarios extends BaseClass{

//    @Test(priority = 1)
//    public void verify_login() throws InterruptedException {
//    }
    @Test(priority = 2, enabled = true)
    public void verify_logout1() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 3, enabled = true)
    public void verify_logout2() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 4, enabled = false)
    public void verify_logout3() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 5, enabled = false)
    public void verify_logout4() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 6, enabled = false)
    public void verify_logout5() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 7, enabled = false)
    public void verify_logout6() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 8, enabled = false)
    public void verify_logout7() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 9, enabled = false)
    public void verify_logout8() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 10, enabled = false)
    public void verify_logout9() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 11, enabled = false)
    public void verify_logout10() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

//
}
