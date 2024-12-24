package de.fhsw.fit.ws2024;

import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.Before;
import org.junit.Test;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class JCatalogJWebUnitTest
{
    @Before
    public void prepare() {
        setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
        setBaseUrl("http://localhost:8080/JCatalog-1.0-SNAPSHOT/pages/");
        //getTestContext().getWebClient
    }


    @Test
    public void testWelcomePage() throws Exception
    {
        beginAt("home.jsf");
        assertTitleEquals("JCatalog Application");
        assertFormPresent("navcontainer");
    }

    @Test
    public void testLinkToLoginPage() throws Exception
    {
        beginAt("home.jsf");
        clickLink("navcontainer:loginLink");
        assertTitleEquals("JCatalog Login");
    }

    @Test
    public void testLoginPage() throws Exception
    {
        beginAt("login.jsf");
        setTextField("loginForm:username", "admin");
        setTextField("loginForm:password", "masterkey");
        submit();
        assertTitleEquals("JCatalog Welcome");
    }

    @Test
    public void testLogout() throws Exception
    {
        beginAt("login.jsf");
        setTextField("loginForm:username", "admin");
        setTextField("loginForm:password", "masterkey");
        submit();
        assertTitleEquals("JCatalog Welcome");
        clickLink("navcontainer:logoutLink");
        assertTitleEquals("JCatalog Application");
    }

    @Test
    public void testCatalogNavigation() throws Exception {
        beginAt("categories.jsf");
        //clickLink("navcontainer:categoriesLink");
        assertTextPresent("JCatalog Categories");
    }
}
