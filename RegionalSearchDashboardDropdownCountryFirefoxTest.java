/*
 * This script logs in a user and opens a connection to the database. The script first clicks the 
 * Regional Search Dashboard link.  When the Dashboard appears the script selects Country from
 * the Dashboard's dropdown list and enters 'ireland' in the text field. For each continent 
 * and education institution type it performs a database query to find the corresponding number of 
 * institutions and compares it with the number displayed on the webpage. It also performs a similar
 * comparison for all institutions within each continent. Assertions are used to test if the displayed 
 * numbers match those predicted from the database. The database connection is closed, the user logged
 *  out and the browser session terminated.
 */

package Unipupil.UniMavenTests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.Assert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class RegionalSearchDashboardDropdownCountryFirefoxTest
{
    String className = "RegionalSearchDashboardDropdownCountryFirefoxTest";
	
	// Create a new instance of the Firefox webdriver
    //public WebDriver driver = new FirefoxDriver();
    
    WebDriver driver; 
    
    
 	// URL Variables
 	String homepageURL = "https://unipupil.com/en";
 	
    //xpath variables
    
    // Element names
	
    WebDriverWait wait;
    
    public String username = "Scripting Student";
	public String password = "$tudyT1me";

	// Database connection variables
	public String dbConnectionString = "jdbc:mysql://unipupil.com:3306/unipupil_live2?user=root&password=fbdgshic";
	public String dbUsername = "jenkins";
	public String dbPassword = "";
	public Connection conn = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	
	//URL variables
	public String loginPageURL= "https://unipupil.com/en/user/login";
  	public String userProfilePageURL = "https://unipupil.com/en/users/scripting-student";
	public String dashboardPageURL = "https://unipupil.com/en/continent";
	
	String[] continentCodes = {"AF","AS","EU","NA","SA","OC"};
    String[] instTypeCodes = {"uni","col","hig","sec","pri","ear","oth"};
    String[] continentIDs = {"africa","asia","europe","north_america","south_america","oceania"};
	
    // WebElement variables
    Select regSearchDropdown;
    WebElement loginLink; 
    WebElement regSearchDashboardLink; 
    WebElement logoutLink; 
    
    // Variable for storing part of query string common to both 
    // continent header and institution type row total queries
    public String queryFragment;
    //Value of City option in Regional Search Dashboard dropdown menu
    public String optionValue = "dest";
    // search string entered into dashboard dropdown's text field
    public String searchString = "ireland";
    
    // Variable for checking that database connection opened
    public boolean setupComplete = true;
    
    public void testSearch(WebDriver driver,Connection conn,String queryFragment)
	{
    	System.out.println("testSearch running");
		String query = "";
		try
		{
			List<WebElement> continents = driver.findElements(By.xpath(".//div[@class='search-pera-box']"));
			for(int i = 0;i< continents.size();i++)
			{
				WebElement contTotalElement = driver.findElement(By.xpath(".//*[@id='"+continentIDs[i]+"']/h3"));
				String contTotalString = contTotalElement.getText();
				// tokenises string showing total number of institutions per continent
				String[] tokens = contTotalString.split("\\s");
				// stores total of institutions per continent in header
				int contTotal  = Integer.parseInt(tokens[0]);
    		
				List<WebElement> instTypeCounts = continents.get(i).findElements(By.xpath(".//ul//li[@class='center']"));
    		
				int runningTotal = 0;
				for(int j=0;j<instTypeCounts.size();j++)
				{
					//Write institution type and number of institutions to console
					tokens = instTypeCounts.get(j).getText().split("\\s");
					int numInsts = Integer.parseInt(tokens[0]);
					runningTotal+=numInsts;
    			
					stmt = conn.createStatement();
					
					// completes query by adding remainder of where where clause to common query fragment
					query = queryFragment + "AND con.field_institution_continent_value = '"+continentCodes[i]+"' "+
                                "AND typ.field_institution_edu_level_value = '"+instTypeCodes[j]+"'";
					rs = stmt.executeQuery(query);
    			
					// if there's a result (should be count of institutions)
					if(rs.next())
					{
						// Go to first row in result set (should be only one in this case)
						rs.first();
						//Go to first column in row (should be only one in this case)
						int count = rs.getInt(1);
						// Test that the number of institutions returned by the database query
						// equals the number displayed on rhs of the institution type row
						Assert.assertEquals(count, numInsts,"Number displayed not equal to number expected from database");
					}
    			
				} // for j
				// Check that the running total of institution types matches
				// the header total on the box
				Assert.assertEquals(contTotal, runningTotal,"Number of institutions in header not equal to number expected from database");
			
				query = queryFragment + "AND con.field_institution_continent_value = '"+continentCodes[i]+"' ";
				rs = stmt.executeQuery(query);
				if(rs.next())
				{
					// Go to first row in result set (should be only one in this case)
					rs.first();
					//Go to first column in row (should be only one in this case)
					int contInstTotal = rs.getInt(1);
					// Test that the number of institutions of all types per continent returned by the database query
					// equals the running total calculated for  the continent box
					Assert.assertEquals(contInstTotal,runningTotal,"Number of institutions of all types per continent from database not equal to total calculated from row totals");
				} 
			} // for i
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}	
	}
    
  	
  	//Identify input box, input search string and click magnifying glass image
  	

    @BeforeTest
	public void setup()
	{
		System.out.println(className+" starting");
		System.out.println("Setup running");
		
        try 
        {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } 
		catch (Exception ex) 
        {
            //ex.printStackTrace();
			System.out.println(ex.getMessage());
			setupComplete = false;
        }
        
        // Create database connection
        try 
        {
        	conn = DriverManager.getConnection("jdbc:mysql://unipupil.com:3306/unipupil_live2","root" ,"fbdgshic");
        } 
        catch (SQLException ex) 
        {
            // handle any errors
        	ex.printStackTrace();
        	setupComplete = false;
        } 
        
        System.setProperty("webdriver.chrome.driver","C:\\ChromeDriver\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        
        // Set the implicit wait time for elements to  load to 20 Seconds
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
        //Maximize the browser
        driver.manage().window().maximize();
        // Open Unipupil Website - Live server
        driver.get("https://unipupil.com/en");
	}
	
	
    @Test
	public void clickLogin()
	{
		System.out.println("Test Method 1 'clickLogin' running");
		Assert.assertEquals(driver.getCurrentUrl(),homepageURL,"Incorrect page: Fail");
		// Set the implicit wait time for elements to  load to 20 Seconds
		loginLink = driver.findElement(By.linkText("Login"));
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
		loginLink.click();
	}
	
	@Test(dependsOnMethods={"clickLogin"})
	public void enterLoginDetails()
	{
		System.out.println("Test Method 2 'enterLoginDetails' running");
		Assert.assertEquals(driver.getCurrentUrl(),loginPageURL,"Incorrect page: Fail");
		driver.findElement(By.id("edit-name")).sendKeys(username); //
        driver.findElement(By.id("edit-pass")).sendKeys(password);
        // Set the implicit wait time for elements to  load to 20 Seconds
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
        // Click the submit button to enter login details
        driver.findElement(By.id("edit-submit")).click();
		
	}
	
	@Test(dependsOnMethods={"enterLoginDetails"})
	public void loggedIn()
	{
		System.out.println("Test Method 3 'loggedIn' running");
		Assert.assertEquals(driver.getCurrentUrl(),userProfilePageURL,"Incorrect page: Fail");
		// Set the implicit wait time for elements to  load to 20 Seconds
		regSearchDashboardLink = driver.findElement(By.xpath(".//*[@id='nice-menu-1']/li[7]/a"));
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		regSearchDashboardLink.click();
	}
	
	
	@Test(dependsOnMethods={"loggedIn"},priority=1)
	public void searchByCountry()
	{
        Assert.assertTrue(setupComplete,"File not created or database connection not established");
		System.out.println("Test Method 4 'searchByCountry'");
		//Select City as search option
		WebElement regSearchDashboardLink = driver.findElement(By.xpath(".//*[@id='nice-menu-1']/li[7]/a"));
        regSearchDashboardLink.click();
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		regSearchDropdown = new Select(driver.findElement(By.id("edit-opt--2")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-opt--2")));  // until input box is found
        regSearchDropdown.selectByValue(optionValue);
		
        queryFragment = "SELECT count(n.nid) AS InstCount " +
						"FROM node n "+
						"LEFT JOIN field_data_field_institution_location il ON n.nid = il.entity_id "+
						"LEFT JOIN location l ON l.lid = il.field_institution_location_lid "+
						"LEFT JOIN location_country lc ON l.country = lc.code "+
						"LEFT JOIN field_data_field_institution_edu_level typ ON il.entity_id = typ.entity_id "+
						"LEFT JOIN field_data_field_institution_reference in_ref ON n.nid = in_ref.field_institution_reference_target_id "+ 
						"LEFT JOIN field_data_field_institution_continent con ON  in_ref.entity_id= con.entity_id "+
						"WHERE lc.name = '"+searchString+"' AND n.status = 1 ";
                    
		wait = new WebDriverWait(driver, 5);
  		WebElement searchInputBox = driver.findElement(By.id("edit-text--2"));
  		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-text--2")));  // until input box is found
        searchInputBox.sendKeys(searchString);
        WebElement magGlass = driver.findElement(By.id("edit-sub--2"));
        magGlass.click();
        // Hold page for 10 sec to see results
        try
        {
          	Thread.sleep(10000);
        }
        catch(InterruptedException ie)
        {
          	ie.printStackTrace();
        }
        testSearch(driver,conn,queryFragment);
	}
 	
	@Test(dependsOnMethods={"loggedIn"},priority=2)
	public void logout()
	{
		System.out.println("Test Method 5 'logout' running");
		// Set the implicit wait time for elements to  load to 20 Seconds
		logoutLink = driver.findElement(By.linkText("Logout"));
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
		logoutLink.click();
	}
	
	@Test(dependsOnMethods={"logout"})
	public void loggedOut()
	{
		System.out.println("Test Method 6 'loggedOut' running");
		Assert.assertEquals(driver.getCurrentUrl(),homepageURL,"Incorrect page: Fail");
	}
	
	@AfterTest
	public void shutdown()
	{
		System.out.println("'shutdown' running");
		
		try
		{
			conn.close();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			//ex.printStackTrace();
		}
		driver.close();
		driver.quit();
		System.out.println(className+ " finished");
	}
}	