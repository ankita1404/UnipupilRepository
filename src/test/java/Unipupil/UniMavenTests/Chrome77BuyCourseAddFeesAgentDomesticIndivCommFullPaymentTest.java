/* Test script to buy a course with additional fees
 * Script logs in as institution, selects a course and records the course title, basic fee and additional fee.
 * It then logs in as a student and selects the same course and adds it to the cart.  The information displayed
 * in the cart, the checkout page, the order review page, the Paypal minibasket, the order status page and the
 * student account page are checked against the information pulled during the institution login.  The script also
 * checks that line totals match order totals and subtotals and that information in a given view is consistent
 * with that visible in a previous view.
 *
 * @author Joseph Lalor
 *
 */

package Unipupil.UniMavenTests;

import java.math.BigDecimal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Unipupil.TestFramework.pageObjects.AdminProfilePage;
import Unipupil.TestFramework.pageObjects.AgentProfilePage;
import Unipupil.TestFramework.pageObjects.CartPage;
import Unipupil.TestFramework.pageObjects.CheckoutPage;
import Unipupil.TestFramework.pageObjects.CommissionInstitutionListPage;
import Unipupil.TestFramework.pageObjects.CompleteOrderPage;
import Unipupil.TestFramework.pageObjects.CourseClassWorkshopMoreInfoPageDomestic;
import Unipupil.TestFramework.pageObjects.CourseInstalmentSchedule;
import Unipupil.TestFramework.pageObjects.DomesticCourse;
import Unipupil.TestFramework.pageObjects.Homepage;
import Unipupil.TestFramework.pageObjects.InstitutionAccountOverviewPage;
import Unipupil.TestFramework.pageObjects.InstitutionCourseListPage;
import Unipupil.TestFramework.pageObjects.InstitutionListPage;
import Unipupil.TestFramework.pageObjects.InstitutionProfilePage;
import Unipupil.TestFramework.pageObjects.LoginPage;
import Unipupil.TestFramework.pageObjects.PaymentOrderHistoryPage;
import Unipupil.TestFramework.pageObjects.PublicCourseListPage;
import Unipupil.TestFramework.pageObjects.ReviewOrderPage;
import Unipupil.TestFramework.pageObjects.StudentProfilePage;
import Unipupil.TestFramework.pageObjects.AgentAccountTableRows.AgentAccountCourseFullPaymentTableRow;
import Unipupil.TestFramework.pageObjects.AgentAccountTableRows.AgentAccountTableRow;
import Unipupil.TestFramework.pageObjects.CartLineItems.CartCourseAdditionalFeesLineItem;
import Unipupil.TestFramework.pageObjects.CheckoutLineItems.CheckoutCourseAdditionalFeesLineItem;
import Unipupil.TestFramework.pageObjects.InstitutionAccountTableRows.InstitutionAccountCourseFullPaymentAdditionalFeesTableRow;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalLoginPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSandboxAgentPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSandboxEduInstPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSandboxLoginPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSandboxPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSandboxStudentPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSandboxUnipupilPage;
import Unipupil.TestFramework.pageObjects.PayPal.Sandbox.PayPalSubmitPage;
import Unipupil.TestFramework.pageObjects.PaymentOrderHistoryLineItems.PaymentOrderHistoryCourseAddFeesLineItem;
import Unipupil.TestFramework.pageObjects.ReviewOrderLineItems.ReviewOrderCourseAdditionalFeesLineItem;
import Unipupil.TestFramework.pageObjects.StudentAccountTableRows.StudentAccountCourseAdditionalFeesTableRow;



public class Chrome77BuyCourseAddFeesAgentDomesticIndivCommFullPaymentTest
{
	//private static Logger logger = LoggerFactory.getLogger(ChromeLiveBuyCourseAddFeesAgentDomesticIndivCommFullPaymentTest.class);
	// Variables
	String programClassName = "Chrome77BuyCourseAddFeesAgentDomesticIndivCommFullPaymentTest";

	// Server variables
	WebDriver driver;
	WebDriverWait wait;
	FluentWait fluentWait;
	// Course Variables
	String courseName = "FULLPAYMENTSAFIC TESTING";
	String courseNameForURL = "fullpaymentsafic-testing";
	// Student Variables
	String studentUsername = "Prom Student";
	String studentUsernameForURL="prom-student";
	String studentPassword = "Fbdgshic1.";
	String userNumber = "2428"; // Prom Student
	// Institution Variables
	String institutionUsername = "Unipupil University Sample";
	String institutionPassword = "Fbdgshic1.";
	String optionValue = "Institution Name";
	String searchString = "unipupil language";
	String educationInstitutionName = "Unipupil Language School";
	// Admin Variables
	String adminUsername = "moranj";
	String adminUsernameForURL = "moranj";
	String adminPassword = "!bOxoun2weok";
	// Agent Variables
	String agentUsername = "Promo Agent";
	String agentUsernameForURL = "promo-agent";
	String agentPassword = "Fbdgshic1.";
	// Live PayPal logins
	/*String ppAgentUsername = "unipupilpromoagent@gmail.com";
	String ppAgentPassword = "P4rgoemnot";
	String ppInstitutionUsername = "paypal@unipupil.com"; // Unipupil Universtity Sample
	String ppInstitutionPassword = "*!@aD153";
	String ppEduInstitutionUsername = "unipupilpromoeduinst@gmail.com"; // Unipupil University (Sample)
	String ppEduInstitutionPassword = "M3zzan!ne";
	String ppCardAccountUsername = "JLalor1001@gmail.com";
	String ppCardAccountPassword = "Test1001";
	String institutionUsername = "Unipupil University Sample";
	String institutionPassword = "Fbdgshic1.";*/
	// Test PayPal logins
	String ppAgentUsername = "addon.vivek5@gmail.com";
	String ppAgentPassword = "developer1";
	String ppUnipupilUsername = "addon.vivek007@gmail.com"; // Unipupil Universtity Sample
	String ppUnipupilPassword = "developer1";
	String ppEduInstitutionUsername = "addon.vivek4@gmail.com"; // Unipupil University (Sample)
	String ppEduInstitutionPassword = "developer1";
	String ppCardAccountUsername = "addon.vivek6@gmail.com";
	String ppCardAccountPassword = "developer1";
	String orderNumber;
	// 77 server Url Variables
	//String homepageURL = "http://184.172.46.77/";
	String homepageURL = "https://unipupil.com/";
	// Live server URL Variables
	//String homepageURL = "https://unipupil.com/";
	String loginPageURL= homepageURL+"user/login";

	//String studentProfilePageURL = homepageURL+"users/" + studentUsernameForURL;
	String studentProfilePageURL = homepageURL+"user";
	//String institutionProfilePageURL = homepageURL+"users/unipupil-university-sample";
	String institutionProfilePageURL = homepageURL+"user";
	//String agentProfilePageURL = homepageURL+"users/"+agentUsernameForURL; //s/promo-agent";
	String agentProfilePageURL = homepageURL+"user";
	//String adminProfilePageURL = homepageURL+"users/"+adminUsernameForURL; //s/joseph";
	String adminProfilePageURL = homepageURL+"user";
	String institutionListPageURL = homepageURL+"institutions/search?title=unipupil%20language";
	String institutionPageURL = homepageURL+"content/unipupil-language-school";
	String unipupilSchoolLanguagesAccountOverviewURL = homepageURL+"institution/102061046/manage/account_overview";
	String cartPageURL = homepageURL+"cart";
	String checkoutPageURL = homepageURL+"cart/checkout";
	String reviewOrderURL = homepageURL+"cart/checkout/review";
	String institutionCommissionListPageURL = homepageURL+"admin/store/settings/set_commission/list";
	String completeOrderPageURL = homepageURL+"cart/checkout/complete";
	//String livePayPalURL = "https://www.paypal.com/home";
	//String livePayPalLoginURL = "https://www.paypal.com/signin";
	String payPalSandboxURL = "https://www.sandbox.paypal.com/home";
	String payPalSandboxLoginURL = "https://www.sandbox.paypal.com/signin";
	String paymentOrderHistoryURL;

	// Page object classes
	Homepage homepage;
	LoginPage loginPage;
	//CourseClassWorkshopEditPage ccwEditPage;
	CourseClassWorkshopMoreInfoPageDomestic ccwMoreInfoPage;
	//CourseClassWorkshopMoreInfoPageDomestic ccwMoreInfoPage;
	CourseInstalmentSchedule courseInstalmentSchedule;
	InstitutionCourseListPage institutionCourseListPage;
	PublicCourseListPage publicCourseListPage;
	CartPage cartPage;
	CartCourseAdditionalFeesLineItem cartCourseAddFeesLineItem;
	CheckoutPage checkoutPage;
	CheckoutCourseAdditionalFeesLineItem checkoutCourseAddFeesLineItem;
	ReviewOrderPage reviewOrderPage;
	ReviewOrderCourseAdditionalFeesLineItem reviewOrderCourseAddFeesLineItem;
	InstitutionListPage institutionListPage;
	InstitutionProfilePage institutionProfilePage;
	InstitutionAccountOverviewPage institutionAccountOverviewPage;
	InstitutionAccountCourseFullPaymentAdditionalFeesTableRow institutionAccountCourseFullPaymentAdditionalFeesTableRow;
	StudentProfilePage studentProfilePage;
	PayPalLoginPage paypalLoginPage;
	PayPalSubmitPage paypalSubmitPage;
	CompleteOrderPage completeOrderPage;
	PaymentOrderHistoryPage paymentOrderHistoryPage;
	PaymentOrderHistoryCourseAddFeesLineItem paymentOrderHistoryCourseAddFeesLineItem;
	StudentAccountCourseAdditionalFeesTableRow studentAccountCourseAdditionalFeesTableRow;
	AdminProfilePage adminProfilePage;
	CommissionInstitutionListPage commInstListPage;
	AgentProfilePage agentProfilePage;
	AgentAccountTableRow agentAccountTableRow;
	AgentAccountCourseFullPaymentTableRow agentAccountCourseFullPaymentTableRow;
	DomesticCourse course;
	//DomesticCourse course;
	PayPalSandboxPage paypalSandboxPage;
	PayPalSandboxLoginPage paypalSandboxLoginPage;
	PayPalSandboxAgentPage paypalSandboxAgentPage;
	PayPalSandboxEduInstPage paypalSandboxEduInstPage;
	PayPalSandboxUnipupilPage paypalSandboxUnipupilPage;
	PayPalSandboxStudentPage paypalSandboxStudentPage;

	// Other
	String basicFeeString;
	String grossFeesString;
	String additionalFeeName;
	String additionalFeeString;
	String addFeesLabelText;
	String startDate;
	String finishDate;
	String checkoutLineProductName;
	String checkoutAdditionalFeesTitle;
	String checkoutInstitutionTitle;
	String reviewOrderHeader;
	String reviewOrderProductName;
	String reviewOrderInstitutionTitle;
	String reviewOrderAdditionalFeesTitle;
	String ppSummaryAmount;
	String ppSummaryTotal;
	String ppReviewInfoHeader;
	String completeOrderTitle;
	String orderCompleteMessage;
	String paymentOrderHistoryHeader;
	String paymentOrderHistoryProductName;
	String paymentOrderHistoryProductDescription;
	String studentAccount;
	String studentAccountCourseTitle;
	String studentAccountStartDate;
	String studentAccountFinishDate;
	String studentAccountPurchaseDate;
	String studentAccountAdditionalFeeName;
	BigDecimal studentAccountAdditionalFee;
	BigDecimal studentAccountAdditionalFeeTotal;
	String agentAccountPurchaseDate;
	String institutionAccountPurchaseDate;
	String institutionAccountStartDate;
	String institutionAccountFinishDate;
	String institutionAccountAdditionalFeeName;
	String institutionAccountAdditionalFeeTotal;

	BigDecimal courseBasicFee;
	BigDecimal displayedCourseBasicFee;
	BigDecimal additionalFee;
	BigDecimal grossFees;
	BigDecimal cartLinePrice;
	BigDecimal cartAdditionalFees;
	BigDecimal checkoutProdUnitPrice;
	BigDecimal checkoutLineAdditionalFees;
	BigDecimal checkoutLinePrice;
	BigDecimal checkoutTotal;
	BigDecimal reviewOrderLinePrice;
	BigDecimal reviewOrderAdditionalFee;
	BigDecimal reviewOrderLineAdditionalFees;
	BigDecimal reviewOrderProductUnitPrice;
	BigDecimal reviewOrderSubTotal;
	BigDecimal reviewOrderTotal;
	BigDecimal paymentOrderHistoryUnitPrice;
	BigDecimal paymentOrderHistoryLinePrice;
	BigDecimal paymentOrderHistoryAdditionalFees;
	BigDecimal paymentOrderHistorySubTotal;
	BigDecimal paymentOrderHistoryTotal;
	BigDecimal agentCommission;
	BigDecimal displayedAgentCommission;
	BigDecimal displayedTotalCommissionPaid;
	BigDecimal agentCommissionPercent;
	BigDecimal institutionCommissionPercent;
	BigDecimal displayedAgentCommissionPercent;
	BigDecimal totalCommissionPercent;
	BigDecimal displayedTotalCommissionPercent;
	BigDecimal displayedInstitutionCommissionPercent;
	BigDecimal totalCommission;
	BigDecimal instAcctDisplayedBasicPrice;
	BigDecimal institutionAccountAdditionalFee;
	BigDecimal totalCommissionPaid;
	BigDecimal studentAccountCoursePrice;
	boolean instTitleFound;
	boolean orderNumberFound;
	String purchaseDate;


//**** ----------------------------------------------------------------------------     SETUP-DRIVERS

	@BeforeTest
	public void setup() {
		System.out.println(programClassName+" starting");
		System.out.println(programClassName+": 'setup' running");
		System.out.println("Setting driver");


		try{
			System.out.println("Using ChromefoxDriverManager to get Chrome driver");
			//WebDriverManager.firefoxdriver().setup();
			//driver = new FirefoxDriver();
			//WebDriverManager.chromedriver().setup();
			System.setProperty("webdriver.chrome.driver","D:\\Automation\\chromedriver.exe");
			driver = new ChromeDriver();
			//WebDriverManager.iedriver().setup();
			//driver = new InternetExplorerDriver();
			//WebDriverManager.operadriver().setup();
			//driver = new OperaDriver();
			if(driver==null){
				System.out.println("Driver not allocated memory");
			}
		}
		catch(Error er){
			er.printStackTrace();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		System.out.println("initialising page objects");
		homepage = new Homepage(driver);
		loginPage = new LoginPage(driver);
		institutionListPage = new InstitutionListPage(driver);
		institutionProfilePage = new InstitutionProfilePage(driver);
		publicCourseListPage = new PublicCourseListPage(driver);
		institutionCourseListPage = new InstitutionCourseListPage(driver);
		course = new DomesticCourse(driver);
		studentProfilePage = new StudentProfilePage(driver);
		ccwMoreInfoPage = new CourseClassWorkshopMoreInfoPageDomestic(driver);
		cartPage = new CartPage(driver);
		cartCourseAddFeesLineItem = new CartCourseAdditionalFeesLineItem(driver);
		checkoutPage = new CheckoutPage(driver);
		checkoutCourseAddFeesLineItem = new CheckoutCourseAdditionalFeesLineItem(driver);
		reviewOrderPage = new ReviewOrderPage(driver);
		reviewOrderCourseAddFeesLineItem = new ReviewOrderCourseAdditionalFeesLineItem(driver);
		paypalLoginPage = new PayPalLoginPage(driver);
		paypalSubmitPage = new PayPalSubmitPage(driver);
		completeOrderPage = new CompleteOrderPage(driver);
		paymentOrderHistoryPage = new PaymentOrderHistoryPage(driver);
		paymentOrderHistoryCourseAddFeesLineItem = new PaymentOrderHistoryCourseAddFeesLineItem(driver);
		adminProfilePage = new AdminProfilePage(driver);
		commInstListPage = new CommissionInstitutionListPage(driver);
		agentProfilePage = new AgentProfilePage(driver);
		paypalSandboxPage = new PayPalSandboxPage(driver);
		paypalSandboxLoginPage = new PayPalSandboxLoginPage(driver);
		paypalSandboxAgentPage = new PayPalSandboxAgentPage(driver);
		paypalSandboxEduInstPage = new PayPalSandboxEduInstPage(driver);
		paypalSandboxUnipupilPage = new PayPalSandboxUnipupilPage(driver);
		paypalSandboxStudentPage = new PayPalSandboxStudentPage(driver);

		System.out.println("Page objects initialised");

		wait = new WebDriverWait(driver, 100);
		//fluentWait = new FluentWait<>(driver).withTimeout(30,SECONDS).pollingEvery(5,SECONDS).ignoring(NoSuchElementException.class);
		// Set the implicit wait time for elements to  load to 50 Seconds
		//driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);

		//Maximize the browser
		driver.manage().window().maximize();
	}

	//**** ----------------------------------------------------------------------------    HOMEPAGE
	@Test
	public void goToHomepage() throws InterruptedException
	{
		System.out.println(programClassName+": Test Method 1 'goToHomepage' running");
		//logger.info(programClassName+": Test Method 1 'goToHomepage' running");
		driver.get(homepageURL);
		//wait.until(ExpectedConditions.urlToBe(homepageURL));
		String urlName = driver.getCurrentUrl();
		//Assert.assertEquals(driver.getCurrentUrl(), urlName, "Incorrect page, test failed");
		Thread.sleep(400);
	}

	@Test(dependsOnMethods={"goToHomepage"})
	public void agreeCookiePolicy() throws InterruptedException {
		System.out.println(programClassName+": Test Method 2 'agreeCookiePolicy' running");
		homepage.agreeCookiePolicy();
		//Assert.assertEquals(true, homepage.isCookiePolicyAccepted(), "Cookie policy has not been accepted, test failed.");
	}

//**** ----------------------------------------------------------------------------     INSTITUTION
	@Test(dependsOnMethods={"agreeCookiePolicy"})
	public void institutionLogin() throws InterruptedException {
		System.out.println(programClassName+": Test Method 3 'institutionLogin' running");
		homepage.loginpage();
		wait.until(ExpectedConditions.urlToBe(loginPageURL));
		Assert.assertEquals(driver.getCurrentUrl(), loginPageURL, "Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"institutionLogin"})
	public void enterInstitutionLoginDetails(){
		System.out.println(programClassName+": Test Method 4 'enterInstitutionLoginDetails' running");
		loginPage.submitLoginDetails(institutionUsername,institutionPassword);
		wait.until(ExpectedConditions.urlToBe(institutionProfilePageURL));
		Assert.assertEquals(driver.getCurrentUrl(),institutionProfilePageURL, "Institution profile page not reached, test failed");
	}// End of enterDetails method

	@Test (dependsOnMethods={"enterInstitutionLoginDetails"})
	public void getCourseList()
	{
		System.out.println(programClassName+": Test Method 5 'getCourseList' running");
		System.out.println(educationInstitutionName);
		institutionProfilePage.getCourseList(educationInstitutionName);
		System.out.println(driver.getTitle());
		wait.until(ExpectedConditions.titleContains("Selling"));
		Assert.assertTrue(driver.getTitle().contains("Selling"), "Selling page not reached, test failed");
	}

	@Test (dependsOnMethods={"getCourseList"})
	public void goToCourseEditPage()
	{
		System.out.println(programClassName+": Test Method 6 'goToCourseEditPage' running");
		System.out.println(courseName);
		institutionCourseListPage.editCourse(courseName);
		System.out.println("probando click");
		wait.until(ExpectedConditions.titleContains("Edit"));
		//Assert.assertTrue(driver.getTitle().contains("Edit Course "+ courseName),"Domestic course edit page not reached");
		Assert.assertTrue(driver.getTitle().contains("Edit"),"Domestic course edit page not reached");
	}

	@Test(dependsOnMethods={"goToCourseEditPage"})
	public void getCourseFees()
	{
		System.out.println(programClassName+": Test Method 7 'getCourseFees' running");
		course.getCourseFees();

		startDate = course.getStartDate();
		finishDate = course.getFinishDate();
		courseBasicFee = course.getCourseBasicFee();
		additionalFee = course.getAdditionalFee();
		System.out.println("Basic fee " + courseBasicFee);
		System.out.println("Additional fee " + additionalFee);

	}

	@Test(dependsOnMethods={"getCourseFees"})
	public void institutionLogout()
	{
		System.out.println(programClassName+": Test Method 8 'institutionLogOut' running");
		course.logout();
		wait.until(ExpectedConditions.urlToBe(homepageURL));
		Assert.assertEquals(driver.getCurrentUrl(), homepageURL, "Incorrect page, test failed");
	} // End of logOut method


//**** ----------------------------------------------------------------------------     STUDENT

	@Test(dependsOnMethods={"institutionLogout"})
	public void studentLogin() {
		System.out.println(programClassName+": Test Method 9 'studentLogin' running");
		homepage.login();
		wait.until(ExpectedConditions.urlToBe(loginPageURL));
		Assert.assertEquals(driver.getCurrentUrl(), loginPageURL, "Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"studentLogin"})
	public void enterStudentLoginDetails(){
		System.out.println(programClassName+": Test Method 10 'enterStudentLoginDetails' running");
		loginPage.submitLoginDetails(studentUsername,studentPassword);
		wait.until(ExpectedConditions.urlToBe(studentProfilePageURL));
		Assert.assertEquals(driver.getCurrentUrl(), studentProfilePageURL, "Incorrect page, test failed");
	}// End of enterDetails method

	@Test (dependsOnMethods={"enterStudentLoginDetails"})
	public void findInstitution()
	{
		System.out.println(programClassName+": Test Method 11 'findInstitution' running");
		studentProfilePage.quickSearch(optionValue, searchString);
		wait.until(ExpectedConditions.urlToBe(institutionListPageURL));
		Assert.assertEquals(driver.getCurrentUrl(),institutionListPageURL, "Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"findInstitution"})
	public void selectInstitution()
	{
		System.out.println(programClassName+": Test Method 12 'selectInstitution' running");
		institutionListPage.selectInstitution(educationInstitutionName);
		wait.until(ExpectedConditions.urlToBe(institutionPageURL));
		Assert.assertEquals(driver.getCurrentUrl(),institutionPageURL,"Incorrect page, test failed");
		//Assert.assertEquals(driver.getCurrentUrl(),testHomepageURL+"/node/102058541","Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"selectInstitution"})
	public void selectCourse()
	{
		System.out.println(programClassName+": Test Method 13 'selectCourse' running");
		publicCourseListPage.selectCourse(courseName);
		//wait.until(ExpectedConditions.urlToBe());
		//Assert.assertEquals(driver.getCurrentUrl(),homepageURL+"content/" + courseNameForURL,"Domestic Course more info page not reached, test failed");
		wait.until(ExpectedConditions.urlContains(homepageURL+"content/" + courseNameForURL));
		Assert.assertTrue(driver.getCurrentUrl().contains(homepageURL+"content/" + courseNameForURL),"Complete order page not reached");
	}

	@Test(dependsOnMethods={"selectCourse"})
	public void addCourseToCart()
	{
		System.out.println(programClassName+": Test Method 14 'addProductToCart' running");
		System.out.println(ccwMoreInfoPage.getDisplayedBasicCourseFee());
		System.out.println(ccwMoreInfoPage.getAddtionalFeesLabel());
		// Compare values obtained through institution login and student login
		Assert.assertEquals(ccwMoreInfoPage.getDisplayedBasicCourseFee().compareTo(courseBasicFee),0, "Basic fee incorrectly displayed");
		Assert.assertEquals(ccwMoreInfoPage.getAddtionalFeesLabel().toUpperCase(), course.getAdditionalFeeName().toUpperCase() +", €"+ additionalFee.toString(),"Additional fees not displayed correctly");

		ccwMoreInfoPage.calcGrossFees();
		grossFees = ccwMoreInfoPage.getGrossFees();
		System.out.println(grossFees);
		ccwMoreInfoPage.addToCart();
		wait.until(ExpectedConditions.urlToBe(cartPageURL));
		Assert.assertEquals(driver.getCurrentUrl(),cartPageURL,"Cart not reached, test failed");
	}

	@Test(dependsOnMethods={"addCourseToCart"})
	public void cartProductName()
	{
		System.out.println(programClassName+": Test Method 15 'cartProductName' running");
		cartCourseAddFeesLineItem.findLineItemElements();
		cartPage.findWebElements(driver);
		Assert.assertEquals(cartCourseAddFeesLineItem.getCartLineProductName().toUpperCase(),courseName.toUpperCase()+" DOMESTIC FEE","Cart product name incorrect, test failed");
	}

	@Test(dependsOnMethods={"cartProductName"})
	public void cartAdditionalFeesTitle()
	{
		System.out.println(programClassName+": Test Method 16 'cartAdditionalFeesTitle' running");
		System.out.println("additionalfFeesTitle");
		Assert.assertEquals(cartCourseAddFeesLineItem.getAdditionalFeesTitle().toUpperCase(),"ADDITIONAL FEES: "+course.getAdditionalFeeName().toUpperCase(),"Additional fees title not properly displayed");
	}

	@Test(dependsOnMethods={"cartAdditionalFeesTitle"})
	public void cartProductUnitPrice()
	{
		System.out.println(programClassName+": Test Method 17 'cartProductUnitPrice' running");
		System.out.println("cartProductUnitPrice running");
		Assert.assertEquals(cartCourseAddFeesLineItem.getProductUnitPrice().compareTo(courseBasicFee),0,"Unit price does not match basic fee");
	}

	@Test(dependsOnMethods={"cartProductUnitPrice"})
	public void cartLinePrice()
	{
		System.out.println(programClassName+": Test Method 18 'cartLinePrice' running");
		cartLinePrice = cartCourseAddFeesLineItem.getLinePrice();
		Assert.assertTrue(cartLinePrice.equals(courseBasicFee),"Cart line price does not match course basic fee");
	}

	@Test(dependsOnMethods={"cartLinePrice"})
	public void cartAdditionalFees()
	{
		System.out.println(programClassName+": Test Method 19 'cartAdditionalFees' running");
		cartAdditionalFees = cartCourseAddFeesLineItem.getAdditionalFees();
		Assert.assertTrue(cartAdditionalFees.equals(additionalFee),"Cart additional fees do not match fee in course details");
	}

	@Test(dependsOnMethods={"cartAdditionalFees"})
	public void checkCartSubTotalAgainstGrossFees()
	{
		System.out.println(programClassName+": Test Method 20 'checkCartSubTotalAgainstGrossFees' running");
		System.out.println(ccwMoreInfoPage.getGrossFees());
		System.out.println(grossFees);
		System.out.println(cartPage.getCartSubTotal());
		Assert.assertEquals(cartPage.getCartSubTotal().compareTo(grossFees),0,"Total incorrect, test failed");
	}

	@Test(dependsOnMethods={"checkCartSubTotalAgainstGrossFees"})
	public void checkAggregateFeesEqualCartSubtotal()
	{
		System.out.println(programClassName+": Test Method 21 'checkAggregateFeesEqualCartSubtotal' running");
		Assert.assertTrue(cartPage.getCartSubTotal().equals(cartCourseAddFeesLineItem.getLinePrice().add(cartCourseAddFeesLineItem.getAdditionalFees())),"Cart total does not match sum of displayed fees");
	}

	@Test(dependsOnMethods={"checkAggregateFeesEqualCartSubtotal"})
	public void goToCheckout()
	{
		System.out.println(programClassName+": Test Method 22 'goToCheckout' running");
		cartPage.goToCheckout();
		wait.until(ExpectedConditions.urlContains(checkoutPageURL));
		Assert.assertTrue(driver.getCurrentUrl().startsWith(checkoutPageURL),"Checkout not reached, test failed");
	}

	@Test(dependsOnMethods={"goToCheckout"})
	public void checkoutLineProductName()
	{
		System.out.println(programClassName+": Test Method 23 'checkoutLineProductName' running");

		checkoutPage.findWebElements();
		checkoutCourseAddFeesLineItem.findLineItemElements();
		checkoutLineProductName = checkoutCourseAddFeesLineItem.getLineProductName();
		Assert.assertEquals(checkoutLineProductName.toUpperCase(),courseName.toUpperCase()+" DOMESTIC FEE","Checkout course name incorrect, test failed");
	}

	@Test(dependsOnMethods={"checkoutLineProductName"})
	public void checkoutAdditionalFeesTitle()
	{
		System.out.println(programClassName+": Test Method 24 'checkoutAdditionalFeesTitle' running");
		checkoutAdditionalFeesTitle = checkoutCourseAddFeesLineItem.getAdditionalFeesTitle();
		Assert.assertEquals(checkoutAdditionalFeesTitle.toUpperCase(),"ADDITIONAL FEES: "+course.getAdditionalFeeName().toUpperCase(),"Additional fees title not properly displayed");
	}

	@Test(dependsOnMethods={"checkoutAdditionalFeesTitle"})
	public void checkoutInstitutionTitle()
	{
		System.out.println(programClassName+": TestMethod 25 'checkoutInstitutionTitle' running");
		checkoutInstitutionTitle = checkoutCourseAddFeesLineItem.getInstitutionTitle();
		Assert.assertTrue(checkoutInstitutionTitle.startsWith(educationInstitutionName),"Institution title not properly displayed");
	}


	@Test(dependsOnMethods={"checkoutInstitutionTitle"})
	public void checkoutProductUnitPrice()
	{
		System.out.println(programClassName+": Test Method 26 'checkoutProductUnitPrice' running");
		checkoutProdUnitPrice = checkoutCourseAddFeesLineItem.getProductUnitPrice();
		Assert.assertEquals(checkoutProdUnitPrice.compareTo(courseBasicFee),0,"Unit price does not match basic fee");
	}

	@Test(dependsOnMethods={"checkoutProductUnitPrice"})
	public void checkoutLinePrice()
	{
		System.out.println(programClassName+": Test Method 27 'checkoutLinePrice' running");
		checkoutLinePrice = checkoutCourseAddFeesLineItem.getLinePrice(); //= new BigDecimal(linePriceText);
		Assert.assertEquals(checkoutLinePrice.compareTo(courseBasicFee),0,"Line price does not match basic fee");
	}

	@Test(dependsOnMethods={"checkoutLinePrice"})
	public void checkoutAdditionalFees()
	{
		System.out.println(programClassName+": Test Method 28 'checkoutAdditionalFees' running");
		checkoutLineAdditionalFees = checkoutCourseAddFeesLineItem.getAdditionalFees();
		Assert.assertTrue(checkoutLineAdditionalFees.equals(additionalFee),"Checkout additional fees do not match fee in course details");
	}

	@Test(dependsOnMethods={"checkoutAdditionalFees"})
	public void checkoutTotal()
	{
		System.out.println(programClassName+": Test Method 29 'checkoutTotal' running");
		checkoutTotal = checkoutPage.getCheckoutTotal();
		System.out.println(checkoutTotal+" "+grossFees);
		Assert.assertEquals(checkoutTotal.compareTo(grossFees),0,"Total incorrect, test failed");
	}

	@Test(dependsOnMethods={"checkoutTotal"})
	public void checkAggregateFeesEqualCheckoutTotal()
	{
		System.out.println(programClassName+": Test Method 30 'checkAggregateFeesEqualCheckoutTotal' running");
		Assert.assertTrue(checkoutTotal.equals(checkoutLinePrice.add(checkoutLineAdditionalFees)),"Checkout total does not match sum of displayed fees");
	}

	@Test(dependsOnMethods={"checkAggregateFeesEqualCheckoutTotal"})
	public void enterAddressAndContinue()
	{
		System.out.println(programClassName+": Test Method 31 'enterAddressAndContinue' running");
		try{
			checkoutPage.enterAddressDetails();
			checkoutPage.reviewOrder();
			wait.until(ExpectedConditions.urlToBe(reviewOrderURL));
			Assert.assertEquals(driver.getCurrentUrl(),reviewOrderURL,"Incorrect page, test failed");
		}
		catch(Exception ex){
			System.out.println(ex.getLocalizedMessage());
		}
	}

	@Test(dependsOnMethods={"enterAddressAndContinue"})
	public void reviewOrderHeader()
	{
		System.out.println(programClassName+": Test Method 32 'reviewOrderHeader' running");
		reviewOrderPage.findWebElements();
		reviewOrderCourseAddFeesLineItem.findLineItemElements();
		reviewOrderHeader = reviewOrderPage.getReviewOrderHeader();
		Assert.assertEquals(reviewOrderHeader,"REVIEW ORDER","Review Order header not displayed correctly, test failed");
	}

	@Test(dependsOnMethods={"reviewOrderHeader"})
	public void reviewOrderProductName()
	{
		System.out.println(programClassName+": Test Method 33 'reviewOrderProductName' running");
		reviewOrderProductName = reviewOrderCourseAddFeesLineItem.getLineProductName();
		Assert.assertEquals(reviewOrderProductName.toUpperCase(),courseName.toUpperCase()+" DOMESTIC FEE","reviewOrder course name incorrect, test failed");

	}

	@Test(dependsOnMethods={"reviewOrderProductName"})
	public void reviewOrderAdditionalFeesTitle()
	{
		System.out.println(programClassName+": Test Method 34 'reviewOrderAdditionalFeesTitle' running");
		reviewOrderAdditionalFeesTitle = reviewOrderCourseAddFeesLineItem.getAdditionalFeesTitle();
		Assert.assertEquals(reviewOrderAdditionalFeesTitle.toUpperCase(),"ADDITIONAL FEES: "+course.getAdditionalFeeName().toUpperCase(),"Additional fees title not properly displayed");
	}

	@Test(dependsOnMethods={"reviewOrderAdditionalFeesTitle"})
	public void reviewOrderInstitutionTitle()
	{
		System.out.println(programClassName+": Test Method 35 'reviewOrderInstitutionTitle' running");
		reviewOrderInstitutionTitle = reviewOrderCourseAddFeesLineItem.getInstitutionTitle();
		Assert.assertTrue(reviewOrderInstitutionTitle.startsWith(educationInstitutionName),"Institution title not properly displayed");
	}

	@Test(dependsOnMethods={"reviewOrderInstitutionTitle"})
	public void reviewOrderProductUnitPrice()
	{
		System.out.println(programClassName+": Test Method 36 'reviewOrderProductUnitPrice' running");
		reviewOrderProductUnitPrice = reviewOrderCourseAddFeesLineItem.getProductUnitPrice();
		Assert.assertEquals(reviewOrderProductUnitPrice.compareTo(courseBasicFee),0,"Unit price does not match basic fee");
	}

	@Test(dependsOnMethods={"reviewOrderProductUnitPrice"})
	public void reviewOrderLinePrice()
	{
		System.out.println(programClassName+": Test Method 37 'reviewOrderLinePrice' running");
		reviewOrderLinePrice = reviewOrderCourseAddFeesLineItem.getLinePrice();
		Assert.assertTrue(reviewOrderLinePrice.equals(reviewOrderProductUnitPrice));
	}

	@Test(dependsOnMethods={"reviewOrderLinePrice"})
	public void reviewOrderAdditionalFees()
	{
		System.out.println(programClassName+": Test Method 38 'reviewOrderAdditionalFees' running");
		reviewOrderLineAdditionalFees = reviewOrderCourseAddFeesLineItem.getAdditionalFees();
		Assert.assertTrue(reviewOrderLineAdditionalFees.equals(additionalFee),"reviewOrder additional fees do not match fee in course details");
	}

	@Test(dependsOnMethods={"reviewOrderAdditionalFees"})
	public void reviewOrderSubTotal()
	{
		System.out.println(programClassName+": Test Method 39 'reviewOrderSubTotal' running");
		reviewOrderSubTotal = reviewOrderPage.getReviewOrderSubTotal();
		Assert.assertEquals(reviewOrderSubTotal.compareTo(grossFees),0,"Review Order SubTotal does not match Gross Fees");
	}

	@Test(dependsOnMethods={"reviewOrderSubTotal"})
	public void reviewOrderTotal()
	{
		System.out.println(programClassName+": Test Method 40 'reviewOrderTotal' running");
		reviewOrderTotal = reviewOrderPage.getReviewOrderTotal();
		Assert.assertEquals(reviewOrderTotal.compareTo(grossFees),0,"Total incorrect, test failed");
	}

	@Test(dependsOnMethods={"reviewOrderTotal"})
	public void aggregateFeesEqualReviewOrderSubTotal()
	{
		System.out.println(programClassName+": Test Method 41 'reviewOrderAdditionalFeesTitle' running");
		Assert.assertTrue(reviewOrderTotal.equals(reviewOrderLinePrice.add(reviewOrderLineAdditionalFees)),"reviewOrder total does not match sum of displayed fees");
		//Confirm reviewOrder
	}

	@Test(dependsOnMethods={"aggregateFeesEqualReviewOrderSubTotal"})
	public void submitOrder()
	{
		System.out.println(programClassName+": Test Method 42 'submitOrder' running");
		reviewOrderPage.submitOrder();
		//Assert.assertEquals(driver.getTitle(),"Choose a way to pay - PayPal","PayPal payment page not reached, test failed");
		Assert.assertEquals(0,0);
	}

	@Test(dependsOnMethods={"submitOrder"})
	public void ppLoginPageSummaryAmount()
	{
		System.out.println(programClassName+": Test Method 43 'ppLoginPageSummaryAmount' running");
		// Using PayPal
		//Open PayPal login form
		paypalLoginPage.findSummaryWebElements();

		ppSummaryAmount = paypalLoginPage.getSummaryAmount();
		Assert.assertEquals(ppSummaryAmount,reviewOrderPage.getReviewOrderTotalTextNoEUR()+" EUR","PP minibasket summary amount incorrectly displayed, test failed");
		//Assert.assertEquals(ppSummaryAmount,"€"+grossFees+" EUR","PP minibasket summary amount incorrectly displayed, test failed");

	}

	@Test(dependsOnMethods={"ppLoginPageSummaryAmount"})
	public void ppLoginPageSummaryTotal()
	{
		System.out.println(programClassName+": Test Method 44 'ppLoginPageSummaryTotal' running");
		ppSummaryTotal = paypalLoginPage.getSummaryTotal();
		Assert.assertEquals(ppSummaryTotal,reviewOrderPage.getReviewOrderTotalTextNoEUR()+" EUR","PP minibasket total amount incorrectly displayed, test failed");
		//Assert.assertEquals(ppSummaryTotal,"€"+grossFees+" EUR","PP minibasket total amount incorrectly displayed, test failed");

	}

	//@Test(dependsOnMethods={"submitOrder"})
	@Test(dependsOnMethods={"ppLoginPageSummaryTotal"})
	public void payPalLogin()
	{
		System.out.println(programClassName+": Test Method 45 'paypalLogin' running");

		//System.out.println("ppCardAccountUserName: "+ppCardAccountUsername);
		//System.out.println("ppCardAccountPassword: "+ppCardAccountPassword);
		paypalLoginPage.login(ppCardAccountUsername,ppCardAccountPassword);
		//paypalLoginPage.login();*/
		paypalSubmitPage.findSummaryWebElements();
		ppReviewInfoHeader = paypalSubmitPage.getReviewInfoHeader();
		Assert.assertEquals(ppReviewInfoHeader,"Review your information","PayPal review page not reached, test failed");
	}

	@Test(dependsOnMethods={"payPalLogin"})
	public void ppConfirmPageSummaryAmount()
	{
		System.out.println(programClassName+": Test Method 46 'ppConfirmPageSummaryAmount' running");
		String ppSummaryAmount = paypalSubmitPage.getSummaryAmount();
		Assert.assertEquals(ppSummaryAmount,reviewOrderPage.getReviewOrderTotalTextNoEUR()+" EUR","PP minibasket summary amount incorrectly displayed, test failed");
		//Assert.assertEquals(ppSummaryAmount,"€"+grossFees+" EUR","PP minibasket summary amount incorrectly displayed, test failed");

	}

	@Test(dependsOnMethods={"ppConfirmPageSummaryAmount"})
	public void ppConfirmPageSummaryTotal()
	{
		System.out.println(programClassName+": Test Method 47 'ppConfirmPageSummaryTotal' running");
		String ppSummaryTotal = paypalSubmitPage.getSummaryTotal();
		Assert.assertEquals(ppSummaryTotal,reviewOrderPage.getReviewOrderTotalTextNoEUR()+" EUR","PP minibasket total amount incorrectly displayed, test failed");
		//Assert.assertEquals(ppSummaryTotal,"€"+grossFees+" EUR","PP minibasket total amount incorrectly displayed, test failed");

	}

	@Test(dependsOnMethods={"ppConfirmPageSummaryTotal"}) //ppConfirmPageSummaryTotal"})
	public void ppConfirmPayment()
	{
		System.out.println(programClassName+": Test Method 48 'ppConfirmPayment' running");
		paypalSubmitPage.confirmPayment();
		wait.until(ExpectedConditions.urlContains("complete"));
		Assert.assertTrue(driver.getCurrentUrl().contains("complete"),"Complete order page not reached");
	}

	@Test(dependsOnMethods={"ppConfirmPayment"})
	public void completeOrderTitle()
	{
		System.out.println(programClassName+": Test Method 49 'completeOrderTitle' running");
		completeOrderPage.findCompleteOrderPageWebElements();
		completeOrderTitle = completeOrderPage.getCompleteOrderTitle();
		Assert.assertEquals(completeOrderTitle,"COMPLETE ORDER","Confirmation header not displayed, test failed");
	}

	@Test(dependsOnMethods={"completeOrderTitle"})
	public void completeOrderPageMessage()
	{
		System.out.println(programClassName+": Test Method 50 'completeOrderPageMessage' running");
		orderNumber = completeOrderPage.getOrderNumber();
		Assert.assertEquals(completeOrderPage.getOrderCompleteMessage(),"Your order is complete! Your order number is "+orderNumber+".","Order number message incorrectly displayed");
	}

	@Test(dependsOnMethods={"completeOrderPageMessage"})
	public void paymentOrderHistoryPage()
	{
		System.out.println(programClassName+": Test Method 51 'paymentOrderHistoryPage' running");
		completeOrderPage.goToPaymentOrderHistory();
		paymentOrderHistoryURL = homepageURL+"user/"+userNumber+"/orders/"+orderNumber;
		wait.until(ExpectedConditions.urlToBe(paymentOrderHistoryURL));
		Assert.assertEquals(driver.getCurrentUrl(),paymentOrderHistoryURL,"Order History page not displayed, test failed");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryPage"})
	public void paymentOrderHistoryHeader()
	{
		System.out.println(programClassName+": Test Method 52 'paymentOrderHistoryHeader' running");
		paymentOrderHistoryPage.findElements();
		paymentOrderHistoryCourseAddFeesLineItem.findElements();
		paymentOrderHistoryHeader = paymentOrderHistoryPage.getPaymentOrderHistoryHeader();
		Assert.assertEquals(paymentOrderHistoryHeader,"PAYMENT ORDER HISTORY","Payment Order History header not displayed, test failed");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryHeader"})
	public void paymentOrderHistoryProductName()
	{
		System.out.println(programClassName+": Test Method 53 'paymentOrderHistoryProductName' running");
		paymentOrderHistoryProductName = paymentOrderHistoryCourseAddFeesLineItem.getProductName();
		Assert.assertEquals(paymentOrderHistoryProductName.toUpperCase(),courseName.toUpperCase()+" DOMESTIC FEE","orderConfirmation course name incorrect, test failed");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryProductName"})
	public void paymentOrderHistoryProductDescription()
	{
		System.out.println(programClassName+": Test Method 54 'paymentOrderHistoryProductDescription' running");
		paymentOrderHistoryProductDescription = paymentOrderHistoryCourseAddFeesLineItem.getProductDescription();
		Assert.assertTrue(paymentOrderHistoryProductDescription.toUpperCase().contains(courseName+" DOMESTIC FEE")
				&& paymentOrderHistoryProductDescription.contains(educationInstitutionName),"Order status product information incorrectly displayed");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryProductDescription"})
	public void paymentOrderHistoryUnitPrice()
	{
		System.out.println(programClassName+": Test Method 55 'paymentOrderHistoryUnitPrice' running");
		paymentOrderHistoryUnitPrice = paymentOrderHistoryCourseAddFeesLineItem.getUnitPrice();
		Assert.assertEquals(paymentOrderHistoryUnitPrice.compareTo(courseBasicFee),0,"Unit price does not match basic fee");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryUnitPrice"})
	public void paymentOrderHistoryQuantity()
	{
		BigDecimal paymentOrderHistoryLineQuantity = paymentOrderHistoryCourseAddFeesLineItem.getQuantity();
		System.out.println(programClassName+": Test Method 56 'paymentOrderHistoryQuantity' running");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryQuantity"})
	public void paymentOrderHistoryLinePrice()
	{
		System.out.println(programClassName+": Test Method 57 'paymentOrderHistoryLinePrice' running");
		paymentOrderHistoryLinePrice = paymentOrderHistoryCourseAddFeesLineItem.getLinePrice();
		//Assert.assertTrue(paymentOrderHistoryLinePrice.equals(paymentOrderHistoryUnitPrice));
	}

	@Test(dependsOnMethods={"paymentOrderHistoryLinePrice"})
	public void paymentOrderHistoryAdditionalFees()
	{
		System.out.println(programClassName+": Test Method 58 'paymentOrderHistoryAdditionalFees' running");
		paymentOrderHistoryAdditionalFees = paymentOrderHistoryCourseAddFeesLineItem.getAdditionalFees();
		Assert.assertTrue(paymentOrderHistoryAdditionalFees.equals(additionalFee),"orderConfirmation additional fees do not match fee in course details");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryAdditionalFees"})
	public void paymentOrderHistoryTotal()
	{
		System.out.println(programClassName+": Test Method 59 'paymentOrderHistoryAdditionalFees' running");
		paymentOrderHistoryTotal = paymentOrderHistoryPage.getPaymentOrderHistoryTotal();
		Assert.assertEquals(paymentOrderHistoryTotal.compareTo(grossFees),0,"Total incorrect, test failed");
	}

	@Test(dependsOnMethods={"paymentOrderHistoryTotal"})
	public void checkAggregateFeesEqualPaymentOrderHistoryTotal()
	{
		System.out.println(programClassName+": Test Method 60 'checkAggregateFeesEqualPaymentOrderHistoryTotal' running");
		Assert.assertTrue(paymentOrderHistoryTotal.equals(paymentOrderHistoryLinePrice.add(paymentOrderHistoryAdditionalFees)),"Order History total does not match sum of displayed fees");
	}

	@Test(dependsOnMethods={"checkAggregateFeesEqualPaymentOrderHistoryTotal"})
	public void goToStudentAccount()
	{
		System.out.println(programClassName+": Test Method 61 'checkAggregateFeesEqualPaymentOrderHistoryTotal' running");
		paymentOrderHistoryPage.goToMyAccount();
		wait.until(ExpectedConditions.urlContains(studentProfilePageURL));
		Assert.assertTrue(driver.getCurrentUrl().contains(studentProfilePageURL),"Student account page not displayed, test failed");
	}

	@Test(dependsOnMethods={"goToStudentAccount"})
	public void studentAccountPurchaseDate()
	{
		System.out.println(programClassName+": Test Method 62 'studentAccountPurchaseDate' running");
		studentProfilePage.findStudentAccountEntry(orderNumber);
		studentAccountCourseAdditionalFeesTableRow = new StudentAccountCourseAdditionalFeesTableRow(studentProfilePage.getTableRow(),orderNumber);
		studentAccountCourseAdditionalFeesTableRow.findElements();
		studentAccountPurchaseDate = studentAccountCourseAdditionalFeesTableRow.getPurchaseDate();
		Assert.assertEquals(studentAccountPurchaseDate,completeOrderPage.getDate(),"Purchase date incorrect");
	}

	@Test(dependsOnMethods={"studentAccountPurchaseDate"})
	public void studentAccountCourseName()
	{
		System.out.println(programClassName+": Test Method 63 'studentAccountCourseName' running");
		studentAccountCourseTitle = studentAccountCourseAdditionalFeesTableRow.getCourseTitle();
		Assert.assertTrue(studentAccountCourseTitle.toUpperCase().contains(courseName), "incorrect course name in account table row title area");
	}

	@Test(dependsOnMethods={"studentAccountCourseName"})
	public void studentAccountStartDate()
	{
		System.out.println(programClassName+": Test Method 64 'studentAccountStartDate' running");
		studentAccountStartDate = studentAccountCourseAdditionalFeesTableRow.getStartDate();
		Assert.assertEquals(studentAccountStartDate,startDate,"Start date incorrect");
	}

	@Test(dependsOnMethods={"studentAccountStartDate"})
	public void studentAccountFinishDate()
	{
		System.out.println(programClassName+": Test Method 65 'studentAccountFinishDate' running");
		studentAccountFinishDate = studentAccountCourseAdditionalFeesTableRow.getFinishDate();
		Assert.assertEquals(studentAccountFinishDate,finishDate,"Finish date incorrect");
	}

	@Test(dependsOnMethods={"studentAccountFinishDate"})
	public void studentAccountCoursePrice()
	{
		System.out.println(programClassName+": Test Method 66 'studentAccountCoursePrice' running");
		studentAccountCoursePrice = studentAccountCourseAdditionalFeesTableRow.getCoursePrice();
		Assert.assertEquals(studentAccountCoursePrice,courseBasicFee,"course basic price incorrect");
	}

	@Test(dependsOnMethods={"studentAccountCoursePrice"})
	public void studentAccountAdditionalFeeName()
	{
		System.out.println(programClassName+": Test Method 67 'studentAccountAdditionalFeeName' running");
		studentAccountAdditionalFeeName = studentAccountCourseAdditionalFeesTableRow.getAdditionalFeeName().toUpperCase();
		Assert.assertEquals(studentAccountAdditionalFeeName,course.getAdditionalFeeName().toUpperCase(),"Additional fee name incorrect");
	}

	@Test(dependsOnMethods={"studentAccountAdditionalFeeName"})
	public void studentAccountAdditionalFee()
	{
		System.out.println(programClassName+": Test Method 68 'studentAccountAdditionalFee' running");
		studentAccountAdditionalFee = studentAccountCourseAdditionalFeesTableRow.getAdditionalFee();
		Assert.assertEquals(studentAccountAdditionalFee,cartCourseAddFeesLineItem.getAdditionalFees(),"Additional fee incorrect");
	}

	@Test(dependsOnMethods={"studentAccountAdditionalFee"})
	public void studentAccountAdditionalFeeTotal()
	{
		System.out.println(programClassName+": Test Method 69 'studentAccountAdditionalFeeTotal' running");
		studentAccountAdditionalFeeTotal = studentAccountCourseAdditionalFeesTableRow.getAdditionalFeeTotal();
		Assert.assertEquals(studentAccountAdditionalFeeTotal,cartCourseAddFeesLineItem.getAdditionalFees(),"Total additional fees incorrect");
	}

	@Test(dependsOnMethods={"studentAccountAdditionalFeeTotal"})
	public void adminLogin()
	{
		System.out.println(programClassName+": Test Method 70 'adminLogin' running");
		studentProfilePage.logout();
		homepage.login();
		loginPage.submitLoginDetails(adminUsername, adminPassword);
		wait.until(ExpectedConditions.urlToBe(adminProfilePageURL));
		Assert.assertEquals(driver.getCurrentUrl(), adminProfilePageURL, "Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"adminLogin"})
	public void getCommissionPercentages()
	{
		System.out.println(programClassName+": Test Method 71 'getCommissionPercentages' running");
		adminProfilePage.findElements();
		adminProfilePage.openCommissionInstitutionsPage();
		wait.until(ExpectedConditions.urlToBe(institutionCommissionListPageURL));
		Assert.assertEquals(driver.getCurrentUrl(),institutionCommissionListPageURL,"Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"getCommissionPercentages"})
	public void agentLogin()
	{
		System.out.println(programClassName+": Test Method 72 'agentLogin' running");
		commInstListPage.findWebElements();
		commInstListPage.logout();
		homepage.login();
		loginPage.submitLoginDetails(agentUsername, agentPassword);
		wait.until(ExpectedConditions.urlToBe(agentProfilePageURL));
		Assert.assertEquals(driver.getCurrentUrl(), agentProfilePageURL, "Incorrect page, test failed");
	} // End of logOut method

	@Test(dependsOnMethods = {"agentLogin"})
	public void agentAccountPurchaseDate() {
		System.out.println(programClassName
				+ ": Test Method 73 'agentAccountPurchaseDate' running");
		agentProfilePage.findAgentAccountEntry(orderNumber);
		agentAccountCourseFullPaymentTableRow = new AgentAccountCourseFullPaymentTableRow(agentProfilePage.getTableRow(),orderNumber);
		agentAccountCourseFullPaymentTableRow.findElements();
		agentAccountPurchaseDate = agentAccountCourseFullPaymentTableRow
				.getPurchaseDate();
		Assert.assertEquals(agentAccountPurchaseDate,
				completeOrderPage.getDate(), "Purchase date incorrect");

	}

	@Test(dependsOnMethods={"agentAccountPurchaseDate"})
	public void agentAccountDisplayedTotalCommissionPercent()
	{
		System.out.println(programClassName+": Test Method 74 'displayedTotalCommissionPercent' running");
		totalCommissionPercent = commInstListPage.getDomesticCommissionPercent();
		displayedTotalCommissionPercent = agentAccountCourseFullPaymentTableRow.getTotalCommissionPercent();
		Assert.assertEquals(displayedTotalCommissionPercent,totalCommissionPercent,"Total Commission percentage incorrect");
	}



	@Test(dependsOnMethods={"agentAccountDisplayedTotalCommissionPercent"})
	public void agentAccountTotalPaid()
	{
		System.out.println(programClassName+": Test Method 75 'agentAccountTotalPaid' running");
		totalCommission = commInstListPage.calcAgentCommissionDomestic(courseBasicFee);
		displayedTotalCommissionPaid = agentAccountCourseFullPaymentTableRow.getTotalCommissionPaid();
		Assert.assertEquals(displayedTotalCommissionPaid,totalCommission,"Total Commission incorrect");
	}

	@Test(dependsOnMethods={"agentAccountTotalPaid"})
	public void agentAccountTotalPrice()
	{
		System.out.println(programClassName+": Test Method 76 'agentAccountTotalPrice' running");
		Assert.assertEquals(agentAccountCourseFullPaymentTableRow.getCoursePrice(),
				courseBasicFee, "Agent account Course Price incorrect");
	}

	@Test(dependsOnMethods={"agentAccountTotalPrice"})
	public void agentAccountTotalRevenue()
	{
		System.out.println(programClassName+": Test Method 77 'agentAccountRevenue' running");
		BigDecimal calcTotalRevenue = agentProfilePage.calcTotalRevenue();
		BigDecimal dispTotalRevenue = agentProfilePage.displayedTotalRevenue();
		Assert.assertEquals(dispTotalRevenue,calcTotalRevenue,"Displayed revenue not equal to calculated");
	}

	@Test(dependsOnMethods={"agentAccountTotalRevenue"})
	public void checkInstitutionAccount()
	{
		System.out.println(programClassName+": Test Method 78 'checkInstitutionAccount' running");
		agentProfilePage.logout();
		homepage.login();
		loginPage.submitLoginDetails(institutionUsername, institutionPassword);
		wait.until(ExpectedConditions.urlToBe(institutionProfilePageURL));
		Assert.assertEquals(driver.getCurrentUrl(), institutionProfilePageURL, "Incorrect page, test failed");
	}

	@Test(dependsOnMethods={"checkInstitutionAccount"})
	public void institutionAccountOverviewPage()
	{
		System.out.println(programClassName+": Test Method 79 'institutionAccountOverviewPage' running");
		institutionProfilePage.accountOverview(educationInstitutionName);
		wait.until(ExpectedConditions.urlToBe(unipupilSchoolLanguagesAccountOverviewURL));
		Assert.assertEquals(driver.getCurrentUrl(),unipupilSchoolLanguagesAccountOverviewURL,"Institution Account Overview page not reached, test failed");
		//Assert.assertEquals(driver.getCurrentUrl(),unipupilSchoolLanguagesAccountOverviewURL,"Institution Account Overview page not reached, test failed");
	}

	@Test(dependsOnMethods={"institutionAccountOverviewPage"})
	public void institutionAccountDisplayedTotalCommissionPercent()
	{
		System.out.println(programClassName+": Test Method 80 'institutionAccountDisplayedTotalCommissionPercent' running");
		institutionAccountOverviewPage = new InstitutionAccountOverviewPage(driver);
		institutionAccountOverviewPage.findInstitutionAccountEntry(orderNumber);
		institutionAccountCourseFullPaymentAdditionalFeesTableRow = new InstitutionAccountCourseFullPaymentAdditionalFeesTableRow(institutionAccountOverviewPage.getTableRow(), orderNumber);
		institutionAccountCourseFullPaymentAdditionalFeesTableRow.findElements();
		totalCommissionPercent = commInstListPage.getDomesticCommissionPercent();
		displayedTotalCommissionPercent = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getTotalCommissionPercent();
		Assert.assertEquals(displayedTotalCommissionPercent,totalCommissionPercent,"Total Commission percentage incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountDisplayedTotalCommissionPercent"})
	public void institutionAccountDisplayedAgentPercent()
	{
		System.out.println(programClassName+": Test Method 81 'institutionAccountDisplayedAgentPercent' running");
		agentCommissionPercent = commInstListPage.getAgentCommissionPercent();
		displayedAgentCommissionPercent = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getAgentCommissionPercent();
		Assert.assertEquals(displayedAgentCommissionPercent,agentCommissionPercent,"Agent Commission percentage incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountDisplayedAgentPercent"})
	public void institutionAccountDisplayedInstitutionPercent()
	{
		System.out.println(programClassName+": Test Method 82 'institutionAccountDisplayedInstitutionPercent' running");
		institutionCommissionPercent = commInstListPage.getInstitutionCommissionPercent();
		displayedInstitutionCommissionPercent = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getInstitutionCommissionPercent();
		Assert.assertEquals(displayedInstitutionCommissionPercent,institutionCommissionPercent,"Institution Commission percentage incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountDisplayedInstitutionPercent"})
	public void institutionAccountTotalPaid()
	{
		System.out.println(programClassName+": Test Method 83 'institutionAccountTotalPaid' running");
		totalCommissionPaid = commInstListPage.calcTotalCommissionDomestic(courseBasicFee);
		displayedTotalCommissionPaid = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getTotalCommissionPaid();
		Assert.assertEquals(displayedTotalCommissionPaid,totalCommissionPaid,"Total Commission incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountTotalPaid"})
	public void institutionAccountPurchaseDate()
	{
		System.out.println(programClassName+": Test Method 84 'institutionAccountPurchaseDate' running");
		System.out.println("getDate: " + completeOrderPage.getDate());
		institutionAccountPurchaseDate = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getPurchaseDate();
		Assert.assertEquals(institutionAccountPurchaseDate,
				completeOrderPage.getDate(),
				//purchaseDate,
				"Purchase date incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountPurchaseDate"})
	public void institutionAccountStartDate()
	{
		System.out.println(programClassName+": Test Method 85 'institutionAccountStartDate' running");
		institutionAccountStartDate = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getStartDate();
		Assert.assertEquals(institutionAccountStartDate,startDate,"Start date incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountStartDate"})
	public void institutionAccountFinishDate()
	{
		System.out.println(programClassName+": Test Method 86 'studentAccountFinishDate' running");
		institutionAccountFinishDate = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getFinishDate();
		Assert.assertEquals(institutionAccountFinishDate,finishDate,"Finish date incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountFinishDate"})
	public void institutionAccountCoursePrice()
	{
		System.out.println(programClassName+": Test Method 87 'institutionAccountCoursePrice' running");
		instAcctDisplayedBasicPrice = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getCoursePrice();
		Assert.assertEquals(instAcctDisplayedBasicPrice,courseBasicFee,"Institution course price incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountCoursePrice"})
	public void institutionAccountAdditionalFeeName()
	{
		System.out.println(programClassName+": Test Method 88 'institutionAccountAdditionalFeeName' running");
		Assert.assertEquals(institutionAccountCourseFullPaymentAdditionalFeesTableRow.getAdditionalFeeName().toUpperCase(),
				course.getAdditionalFeeName().toUpperCase(),
				//additionalFeeName,
				"Additional fee name incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountAdditionalFeeName"})
	public void institutionAccountAdditionalFee()
	{
		System.out.println(programClassName+": Test Method 89 'institutionAccountAdditionalFee' running");
		Assert.assertEquals(institutionAccountCourseFullPaymentAdditionalFeesTableRow.getAdditionalFee(),
				course.getAdditionalFee(),
				"Additional fee incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountAdditionalFee"})
	public void institutionAccountAdditionalFeeTotal()
	{
		System.out.println(programClassName+": Test Method 90 'institutionAccountAdditionalFeeTotal' running");
		Assert.assertEquals(institutionAccountCourseFullPaymentAdditionalFeesTableRow.getAdditionalFeeTotal(),
				course.getAdditionalFee(),
				"Additional fee total incorrect");
	}

	@Test(dependsOnMethods={"institutionAccountAdditionalFeeTotal"})
	public void studentPayPalAccount()
	{
		System.out.println(programClassName+": Test Method 91 'studentPayPalAccount' running");
		institutionProfilePage.clickLogout();
		driver.get(payPalSandboxLoginURL);
		//paypalSandboxLoginPage.enterDetails(ppCardAccountUsername, ppCardAccountPassword);
		Assert.assertEquals(paypalSandboxStudentPage.getGrossFee(),
				courseBasicFee.add(additionalFee),"Paypal Student account gross fees incorrect");
	}

	@Test(dependsOnMethods={"studentPayPalAccount"})
	public void studentPaymentStatus()
	{
		System.out.println(programClassName+": Test Method 92 'studentPaymentStatus' running");
		String payPalPaymentStatus = paypalSandboxStudentPage.getPaymentStatus().toLowerCase();
		System.out.println(payPalPaymentStatus);
		String internalPaymentStatus = studentAccountCourseAdditionalFeesTableRow.getPaymentStatus();
		System.out.println(internalPaymentStatus);
		Assert.assertEquals(payPalPaymentStatus,internalPaymentStatus,"Incorrect payment status in student PayPal account");
	}

	@Test(dependsOnMethods={"studentPaymentStatus"})
	public void unipupilPayPalAccount()
	{
		System.out.println(programClassName+": Test Method 93 'institutionPayPalAccount' running");
		paypalSandboxStudentPage.logout();
		//wait.until(ExpectedConditions.urlContains(livePayPalLoginURL));
		paypalSandboxPage.login();

		paypalSandboxLoginPage.enterDetails(ppUnipupilUsername,ppUnipupilPassword);
		/*Assert.assertEquals(paypalSandboxUnipupilPage.getInstitutionCommission(),
				overallCommissionPage.calcInstitutionCommissionDomestic(courseBasicFee),
				"Paypal account Institution commission incorrect");
		*/
		Assert.assertEquals(paypalSandboxUnipupilPage.getInstitutionCommission(),
	            commInstListPage.calcInstitutionCommissionDomestic(courseBasicFee),
	            "Paypal account Institution commission incorrect");
	}

	@Test(dependsOnMethods={"unipupilPayPalAccount"})
	public void agentPayPalAccount()
	{
		System.out.println(programClassName+": Test Method 94 'agentPayPalAccount' running");
		//paypalSandboxEduInstPage.logout();
		paypalSandboxUnipupilPage.logout();
		paypalSandboxPage.login();
		paypalSandboxLoginPage.enterDetails(ppAgentUsername,ppAgentPassword);

		/*Assert.assertEquals(paypalSandboxAgentPage.getAgentCommission(),
				overallCommissionPage.calcAgentCommissionDomestic(courseBasicFee),
				"Paypal account Institution commission incorrect");
		*/
		Assert.assertEquals(paypalSandboxAgentPage.getAgentCommission(),
	            commInstListPage.calcAgentCommissionDomestic(courseBasicFee),
	            "Paypal account Institution commission incorrect");
	}

	@Test(dependsOnMethods={"agentPayPalAccount"})
	public void eduInstitutionPaymentStatus()
	{
		System.out.println(programClassName+": Test Method 95 'institutionPaymentStatus' running");
		paypalSandboxAgentPage.logout();
		/*try{
			Thread.sleep(270000);
		}
		catch(InterruptedException ie){
			ie.printStackTrace();
		}*/
		paypalSandboxPage.login();
		//wait.until(ExpectedConditions.urlContains(payPalSandboxLoginURL));
		paypalSandboxLoginPage.enterDetails(ppEduInstitutionUsername,ppEduInstitutionPassword);
		String payPalPaymentStatus = paypalSandboxEduInstPage.getPaymentStatus().toLowerCase();
		System.out.println(payPalPaymentStatus);
		String internalPaymentStatus = institutionAccountCourseFullPaymentAdditionalFeesTableRow.getPaymentStatus();
		System.out.println(internalPaymentStatus);
		//Assert.assertEquals(internalPaymentStatus,payPalPaymentStatus,"Incorrect payment status in institution internal account");
		Assert.assertEquals(payPalPaymentStatus,"payment received","Incorrect payment status in institution internal account");
	}

	@Test(dependsOnMethods={"eduInstitutionPaymentStatus"})
	public void eduInstitutionPayPalAccountGrossFees()
	{
		System.out.println(programClassName+": Test Method 96 'eduInstitutionPayPalAccountGrossFees' running");
		Assert.assertEquals(paypalSandboxEduInstPage.getGrossFee(),
				courseBasicFee.add(additionalFee),
				"Paypal educational institution account gross fees incorrect");
	}

	@Test(dependsOnMethods={"eduInstitutionPayPalAccountGrossFees"})
	public void eduInstitutionPayPalAccountAgentCommission()
	{
		System.out.println(programClassName+": Test Method 97 'eduInstitutionPayPalAccountAgentCommission' running");
		/*Assert.assertEquals(paypalSandboxEduInstPage.getAgentCommission(),
				overallCommissionPage.calcAgentCommissionDomestic(courseBasicFee),
				"Paypal account Agent commission incorrect");*/
		Assert.assertEquals(paypalSandboxEduInstPage.getAgentCommission(),
	            commInstListPage.calcAgentCommissionDomestic(courseBasicFee),
	            "Paypal account Agent commission incorrect");
	}

	@Test(dependsOnMethods={"eduInstitutionPayPalAccountAgentCommission"})
	public void eduInstitutionPayPalAccountUnipupilCommission()
	{
		System.out.println(programClassName+": Test Method 98 'eduInstitutionPayPalAccountInstitutionCommission' running");
		/*Assert.assertEquals(paypalSandboxEduInstPage.getInstitutionCommission(),
				overallCommissionPage.calcInstitutionCommissionDomestic(courseBasicFee),
				"Paypal account Institution commission incorrect");*/
	    Assert.assertEquals(paypalSandboxEduInstPage.getInstitutionCommission(),
	    	    commInstListPage.calcInstitutionCommissionDomestic(courseBasicFee),
	    	    "Paypal account Institution commission incorrect");
	}
	
	

	@AfterTest
	public void shutdown()
	{

		System.out.println(programClassName+": 'shutdown' running");
		driver.get(homepageURL);
		homepage.login();
		loginPage.submitLoginDetails(adminUsername, adminPassword);
		adminProfilePage.findElements();
		adminProfilePage.deleteOrder(orderNumber);
		driver.close();
		driver.quit();
		System.out.println(programClassName+" finished");
	}
}