package test;

import BaseClass.BaseClass;

import org.testng.annotations.Test;
import pom.LoginPage;

public class Verify_Login_Scenarios extends BaseClass{

//    @Test(priority = 1)
//    public void verify_login() throws InterruptedException {
//    }
    @Test(priority = 2)
    public void verify_logout1() throws InterruptedException {
//        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 3)
    public void verify_logout2() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 4)
    public void verify_logout3() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 5)
    public void verify_logout4() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 6)
    public void verify_logout5() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 7)
    public void verify_logout6() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 8)
    public void verify_logout7() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 9)
    public void verify_logout8() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 10)
    public void verify_logout9() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }

    @Test(priority = 11)
    public void verify_logout10() throws InterruptedException {
        new LoginPage().loginWithoutReset();
        new LoginPage().logout();
    }


}
