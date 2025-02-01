package com.qa.stepDef;

import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LoginStepDef {

    @When("^I enter username as \"([^\"]*)\"$")
    public void iEnterUsernameAs(String username) throws InterruptedException {
        new LoginPage().enterUserName(username);

    }

    @When("^I enter password as \"([^\"]*)\"$")
    public void iEnterPasswordAs(String password) {
        new LoginPage().enterPassword(password);

    }

    @When("^I click on the login button$")
    public void iClickOnTheLoginButton() {
        new LoginPage().pressLoginBtn();

    }

    @Then("^I should see the error message \"([^\"]*)\"$")
    public void iShouldSeeTheErrorMessage(String errorMessage) {
        Assert.assertEquals(errorMessage, new LoginPage().getErrTxt());
    }

    @Then("^I should see the products page title \"([^\"]*)\"$")
    public void iShouldSeeTheProductsPageTitle(String pageTitle) {
        Assert.assertEquals(pageTitle, new ProductsPage().getTitle());
    }
}
