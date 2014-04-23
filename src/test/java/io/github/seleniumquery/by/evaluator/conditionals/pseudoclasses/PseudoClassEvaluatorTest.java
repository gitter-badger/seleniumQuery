package io.github.seleniumquery.by.evaluator.conditionals.pseudoclasses;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.github.seleniumquery.TestInfrastructure;
import io.github.seleniumquery.by.evaluator.SelectorEvaluator;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PseudoClassEvaluatorTest {

	static WebDriver driver;
	
	@BeforeClass
	public static void before() {
		driver = TestInfrastructure.getDriver();
//		driver = new FirefoxDriver();
//    	System.setProperty("webdriver.chrome.driver", "F:\\desenv\\chromedriver.exe");
//    	driver = new ChromeDriver();
//		System.setProperty("webdriver.ie.driver",  "F:\\desenv\\IEDriverServer.exe");
//    	driver = new InternetExplorerDriver();
		driver.get(TestInfrastructure.getHtmlTestFileUrl(PseudoClassEvaluatorTest.class));
	}
	
	@AfterClass
	public static void after() {
		driver.quit();
	}
	
	@Test
	public void option() {
        WebElement optionElement = driver.findElement(By.cssSelector(".e4"));
			
		assertThat(SelectorEvaluator.is(driver, optionElement, "option"), is(true));
		assertThat(SelectorEvaluator.is(driver, optionElement, "*"), is(true));
		assertThat(SelectorEvaluator.is(driver, optionElement, ":not(div)"), is(true));
		assertThat(SelectorEvaluator.is(driver, optionElement, ":not(:not(div))"), is(false));
		assertThat(SelectorEvaluator.is(driver, optionElement, ":not(:not(option.e4))"), is(true));
		assertThat(SelectorEvaluator.is(driver, optionElement, ":only-of-type"), is(false));
	}
	
	@Test
	public void html() {
		WebElement htmlElement = driver.findElement(By.cssSelector("html"));
		
		assertThat(SelectorEvaluator.is(driver, htmlElement, "option"), is(false));
		assertThat(SelectorEvaluator.is(driver, htmlElement, "*"), is(true));
		assertThat(SelectorEvaluator.is(driver, htmlElement, ":root"), is(true));
		assertThat(SelectorEvaluator.is(driver, htmlElement, ":only-of-type"), is(true));
	}
	
	@Test
	public void body() {
		WebElement bodyElement = driver.findElement(By.cssSelector("body"));
		
		assertThat(SelectorEvaluator.is(driver, bodyElement, "option"), is(false));
		assertThat(SelectorEvaluator.is(driver, bodyElement, "*"), is(true));
		assertThat(SelectorEvaluator.is(driver, bodyElement, ":root"), is(false));
		assertThat(SelectorEvaluator.is(driver, bodyElement, ":only-of-type"), is(true));
	}
	
	// :lang()
	@Test
	public void lang() {
		WebElement brazilian_p = driver.findElement(By.id("brazilian-p"));
		WebElement french_p = driver.findElement(By.id("french-p"));
		WebElement hero_combo = driver.findElement(By.id("hero-combo"));
		
		WebElement htmlElement = driver.findElement(By.cssSelector("html"));
		WebElement bodyElement = driver.findElement(By.cssSelector("body"));
		
		assertThat(SelectorEvaluator.is(driver, french_p, ":lang(fr)"), is(true));
		assertThat(SelectorEvaluator.is(driver, brazilian_p, ":lang(fr)"), is(false));
		assertThat(SelectorEvaluator.is(driver, hero_combo, ":lang(fr)"), is(false));
		
		assertThat(SelectorEvaluator.is(driver, bodyElement, ":lang(fr)"), is(false));
		assertThat(SelectorEvaluator.is(driver, htmlElement, ":lang(fr)"), is(false));
		
		assertThat(SelectorEvaluator.is(driver, french_p, ":lang(pt-BR)"), is(false));
		assertThat(SelectorEvaluator.is(driver, brazilian_p, ":lang(pt-BR)"), is(true));
		assertThat(SelectorEvaluator.is(driver, hero_combo, ":lang(pt-BR)"), is(true));
		assertThat(SelectorEvaluator.is(driver, bodyElement, ":lang(pt-BR)"), is(true));
		assertThat(SelectorEvaluator.is(driver, htmlElement, ":lang(pt-BR)"), is(false));
	}
	
	// :only-child
	@Test
	public void only_child() {
		WebElement onlyChild = driver.findElement(By.id("onlyChild"));
		WebElement grandsonWithSiblings = driver.findElement(By.id("grandsonWithSiblings"));
		WebElement brazilian_p = driver.findElement(By.id("brazilian-p"));
		WebElement hero_combo = driver.findElement(By.id("hero-combo"));
		WebElement htmlElement = driver.findElement(By.cssSelector("html"));
		WebElement bodyElement = driver.findElement(By.cssSelector("body"));
		
		String selector = ":only-child";
		
		assertThat(SelectorEvaluator.is(driver, onlyChild, selector), is(true));
		assertThat(SelectorEvaluator.is(driver, grandsonWithSiblings, selector), is(false));
		assertThat(SelectorEvaluator.is(driver, brazilian_p, selector), is(false));
		assertThat(SelectorEvaluator.is(driver, hero_combo, selector), is(false));
		
		assertThat(SelectorEvaluator.is(driver, bodyElement, selector), is(false));
		assertThat(SelectorEvaluator.is(driver, htmlElement, selector), is(false));
	}
	
	// :contains()
    @Test
    public void contains_pseudo() throws Exception {
    	WebElement containsDiv = driver.findElement(By.id("containsDiv"));
    	List<WebElement> containsDivs = containsDiv.findElements(By.tagName("div"));
    	
    	// <div>abc</div>
    	WebElement div = containsDivs.get(0);
		assertThat(SelectorEvaluator.is(driver, div, ":contains(abc)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div, ":contains(ac)"), is(false));
    	assertThat(SelectorEvaluator.is(driver, div, ":contains(\"abc\")"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div, ":contains('abc')"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div, ":contains('\"abc\"')"), is(false));
    	assertThat(SelectorEvaluator.is(driver, div, ":contains(\"'abc'\")"), is(false));
    	
		// <div>"abc"</div>
    	WebElement div1 = containsDivs.get(1);
		assertThat(SelectorEvaluator.is(driver, div1, ":contains(abc)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div1, ":contains(ac)"), is(false));
    	assertThat(SelectorEvaluator.is(driver, div1, ":contains(\"abc\")"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div1, ":contains('abc')"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div1, ":contains('\"abc\"')"), is(true)); // diferenca da 0
    	assertThat(SelectorEvaluator.is(driver, div1, ":contains(\"'abc'\")"), is(false));
    	
    	// <div>'abc'</div>
    	WebElement div2 = containsDivs.get(2);
    	assertThat(SelectorEvaluator.is(driver, div2, ":contains(abc)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div2, ":contains(ac)"), is(false));
    	assertThat(SelectorEvaluator.is(driver, div2, ":contains(\"abc\")"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div2, ":contains('abc')"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div2, ":contains('\"abc\"')"), is(false));
    	assertThat(SelectorEvaluator.is(driver, div2, ":contains(\"'abc'\")"), is(true)); // diferenca da 0
    	
		// <div>a"bc</div>
    	WebElement div3 = containsDivs.get(3);
    	assertThat(SelectorEvaluator.is(driver, div3, ":contains(a\\\"bc)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div3, ":contains('a\"bc')"), is(true));

    	// <div>ab)c</div>
    	WebElement div4 = containsDivs.get(4);
    	assertThat(SelectorEvaluator.is(driver, div4, ":contains(ab\\)c)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div4, ":contains('ab)c')"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div4, ":contains(\"ab)c\")"), is(true));
		
    	// <div>a\"b)c</div>
    	WebElement div5 = containsDivs.get(5);
    	String slash = "\\\\"+"\\\\"; // after escaped by the java compiler: "\\\\" -- will become a \ after escaped by the css parser
    	String quote = "\\"+"\""; // after escaped by the java compiler: "\"" -- will become a " after escaped by the css parser
    	assertThat(SelectorEvaluator.is(driver, div5, ":contains(a"+ slash + quote + "b\\)c)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div5, ":contains('a"+ slash + "\"b)c')"), is(true));
    	assertThat(SelectorEvaluator.is(driver, div5, ":contains(\"a"+ slash + quote + "b)c\")"), is(true));
    }
    
    // :first-child
    @Test
    public void first_child() {
    	WebElement onlyChild = driver.findElement(By.id("onlyChild"));
    	WebElement grandsonWithSiblings = driver.findElement(By.id("grandsonWithSiblings"));
    	WebElement brazilian_p = driver.findElement(By.id("brazilian-p"));
    	WebElement hero_combo = driver.findElement(By.id("hero-combo"));
    	WebElement htmlElement = driver.findElement(By.cssSelector("html"));
    	WebElement headElement = driver.findElement(By.cssSelector("head"));
    	WebElement bodyElement = driver.findElement(By.cssSelector("body"));
    	
    	String selector = ":first-child";
    	
    	assertThat(SelectorEvaluator.is(driver, onlyChild, selector), is(true));
    	assertThat(SelectorEvaluator.is(driver, grandsonWithSiblings, selector), is(true));
    	assertThat(SelectorEvaluator.is(driver, brazilian_p, selector), is(true));
    	assertThat(SelectorEvaluator.is(driver, hero_combo, selector), is(false));
    	
    	assertThat(SelectorEvaluator.is(driver, htmlElement, selector), is(false));
    	assertThat(SelectorEvaluator.is(driver, headElement, selector), is(true));
    	assertThat(SelectorEvaluator.is(driver, bodyElement, selector), is(false));
    }
    
    // :present
    @Test
    public void present() {
    	WebElement onlyChild = driver.findElement(By.id("onlyChild"));
    	
    	assertThat(SelectorEvaluator.is(driver, onlyChild, ":present"), is(true));
    	assertThat(SelectorEvaluator.is(driver, onlyChild, ":not(:present)"), is(false));
    }
    
    // :eq()
    @Test
    public void eq() {
		WebElement brazilian_p = driver.findElement(By.id("brazilian-p"));
		WebElement french_p = driver.findElement(By.id("french-p"));
    	
		assertThat(SelectorEvaluator.is(driver, brazilian_p, "p:eq(0)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, brazilian_p, ":eq(0)"), is(false));
    	assertThat(SelectorEvaluator.is(driver, french_p, "p:eq(1)"), is(true));
    	assertThat(SelectorEvaluator.is(driver, french_p, ":eq(1)"), is(false));
		
		WebElement xidv = driver.findElement(By.id("xidv"));
		assertThat(SelectorEvaluator.is(driver, xidv, ".ball.div:eq(1)"), is(false));
		assertThat(SelectorEvaluator.is(driver, xidv, ".div:eq(1).ball"), is(true));
		assertThat(SelectorEvaluator.is(driver, xidv, ".div.ball:eq(1)"), is(false));
		assertThat(SelectorEvaluator.is(driver, xidv, ".div.ball:eq(0)"), is(true));
		
    }

}