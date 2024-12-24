package de.fhsw.fit.ws2024;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JCatalogHtmlUnitTest
{

   @Test
   public void testWelcomePage() throws Exception
   {
      HtmlPage page = gotoHomePage();
      assertEquals("JCatalog Application", page.getTitleText());

      List<HtmlForm> forms = (List<HtmlForm>) page.getForms();
      assertEquals(1, forms.size());
      assertEquals("navcontainer", forms.get(0).getId());
   }

   @Test
   public void testLinkToLoginPage() throws Exception
   {
      HtmlPage response = navigateToLoginPage();
      assertEquals("JCatalog Login", response.getTitleText());
   }

   @Test
   public void testLoginPage() throws Exception
   {
      HtmlPage response = login();
      assertEquals("JCatalog Welcome", response.getTitleText());
   }

   /**
    * Test logout link, does not work correctly.
    * 
    * @throws Exception
    */
   @Test
   public void testLogout() throws Exception
   {
      HtmlPage response = login();
      HtmlAnchor login = response.getAnchorByText("Logout");
    		  //getFirstAnchorByText("Logout");
      response = (HtmlPage) login.click();
      // test fails, clicking on links with href="#" seems not to work
      // unfortunately JSF generates lots of them :-(
      assertEquals("JCatalog Application", response.getTitleText());
   }

   private HtmlPage gotoHomePage() throws FailingHttpStatusCodeException,
         IOException
   {
      WebClient webClient = new WebClient(BrowserVersion.CHROME);
//      webClient.setJavaScriptEnabled(true);
      //webClient.setRedirectEnabled(true); // does not work :-(
      URL url = new URL("http://localhost:8080/JCatalog-1.0-SNAPSHOT/pages/home.jsf");
      return (HtmlPage) webClient.getPage(url);
   }

   private HtmlPage navigateToLoginPage() throws MalformedURLException,
         IOException, SAXException
   {
      // HtmlPage response = gotoHomePage();
      // HtmlAnchor login = response.getFirstAnchorByText("Login");
      // return (HtmlPage) login.click();
      WebClient webClient = new WebClient();
      //webClient.setRedirectEnabled(true); // does not work :-(
      URL url = new URL("http://localhost:8080/JCatalog-1.0-SNAPSHOT/pages/login.jsf");
      return (HtmlPage) webClient.getPage(url);
   }

   private HtmlPage login() throws MalformedURLException, IOException,
         SAXException
   {
      HtmlPage response = navigateToLoginPage();
      HtmlForm form = response.getFormByName("loginForm");
      HtmlTextInput username = (HtmlTextInput) form
            .getInputByName("loginForm:username");
      username.setValueAttribute("admin");
      HtmlPasswordInput password = (HtmlPasswordInput) form
            .getInputByName("loginForm:password");
      password.setValueAttribute("masterkey");

      HtmlSubmitInput button = (HtmlSubmitInput) form
            .getInputByName("loginForm:submit");
      return (HtmlPage) button.click();
   }
}
