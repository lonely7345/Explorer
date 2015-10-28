package com.stratio.explorer.testsAT.automated;

import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.explorer.testsAT.utils.BaseTest;
import cucumber.api.CucumberOptions;
import org.testng.annotations.Test;
import org.testng.annotations.Factory;
import com.stratio.data.BrowsersDataProvider;


@CucumberOptions(features = { "src/test/resources/features/automated/ATwLogin.feature" })
public class ATwLogin extends BaseTest {

    @Factory(enabled = false, dataProviderClass = BrowsersDataProvider.class, dataProvider = "availableUniqueBrowsers")
    public ATwLogin(String browser) {
        this.browser = browser;
    }

    @Test(groups = {"basic"}, enabled = true, priority = 1)
    public void installTest() throws Exception {
        new CucumberRunner(this.getClass()).runCukes();
    }
}