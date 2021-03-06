/*
 * Copyright (c) 2015 seleniumQuery authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.seleniumquery.by.firstgen.css.pseudoclasses;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import io.github.seleniumquery.by.firstgen.xpath.component.ConditionSimpleComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;

import java.lang.reflect.Method;

import static io.github.seleniumquery.utils.DriverVersionUtils.isHtmlUnitDriverEmulatingIEBelow11;

/**
 * <p>
 * http://api.jquery.com/submit-selector/
 * </p>
 * <b>
 *     Notice that <code>:submit</code> is not consistent accross browsers when the type attribute is not defined.
 *     IE, HtmlUnit acting as IE and even FirefoxDriver (not Firefox itself) "implicitly create" a type attribute
 *     when it doesn't exist! (The jQuery docs also talks about this issue!)
 * </b>
 * 
 * @author acdcjunior
 *
 * @since 0.9.0
 */
public class SubmitPseudoClass implements PseudoClass<ConditionSimpleComponent> {

	private static final Log LOGGER = LogFactory.getLog(SubmitPseudoClass.class);
	
	private static final String SUBMIT = "submit";
	private static final String INPUT = "input";
	private static final String BUTTON = "button";
	
	@Override
	public boolean isApplicable(String pseudoClassValue) {
		return SUBMIT.equals(pseudoClassValue);
	}
	
	@Override
	public boolean isPseudoClass(WebDriver driver, WebElement element, PseudoClassSelector pseudoClassSelector) {
		return inputWithTypeSubmit(element) || buttonWithTypeSubmitOrWithoutType(driver, element);
	}

	private boolean inputWithTypeSubmit(WebElement element) {
		return INPUT.equals(element.getTagName()) && SUBMIT.equalsIgnoreCase(element.getAttribute("type"));
	}

	private boolean buttonWithTypeSubmitOrWithoutType(WebDriver driver, WebElement element) {
		boolean isButtonTag = BUTTON.equals(element.getTagName());
		if (!isButtonTag) {
			return false;
		}
		boolean isTypeSubmit = SUBMIT.equalsIgnoreCase(element.getAttribute("type"));
		if (isTypeSubmit) {
			return true;
		}
		if (isHtmlUnitDriverEmulatingIEBelow11(driver)) {
			return getDeclaredTypeAttributeFromHtmlUnitButton(element) == null;
		} else {
			return element.getAttribute("type") == null;
		}
	}

	/**
	 * Latest HtmlUnit returns @type=button when type is not set and browser is <=IE9. We don't want that,
	 * we want null if it is not set, so we can decide if it is submit or not. Because if it is null,
	 * then it is :submit. If type is "button", though, it is not.
	 *
	 * #Cross-Driver
	 * #HtmlUnit #reflection #hack
	 */
	private String getDeclaredTypeAttributeFromHtmlUnitButton(WebElement element) {
		try {
			if (element instanceof HtmlUnitWebElement) {
				Method getElementMethod = HtmlUnitWebElement.class.getDeclaredMethod("getElement");
				getElementMethod.setAccessible(true);
				Object htmlUnitElement = getElementMethod.invoke(element);
				if (htmlUnitElement instanceof DomElement) {
					DomAttr domAttr = ((DomElement) htmlUnitElement).getAttributesMap().get("type");
					return domAttr == null ? null : domAttr.getNodeValue();
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Unable to retrieve declared \"type\" attribute from HtmlUnitWebElement "+element+".", e);
		}
		return element.getAttribute("type");
	}

	@Override
	public ConditionSimpleComponent pseudoClassToXPath(PseudoClassSelector pseudoClassSelector) {
		return new ConditionSimpleComponent("[("
				+ "( local-name() = 'input' and @type = 'submit' ) or "
				+ "( local-name() = 'button' and (@type = 'submit' or not(@type)) )"
				+ ")]");
	}
	
}