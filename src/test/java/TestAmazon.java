import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;
import java.util.List;

    public class TestAmazon {

        public WebDriver driver;
        Properties config;
        @BeforeTest
        public void setup() throws InterruptedException {
            ChromeOptions options=new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver=new ChromeDriver(options);
            Thread.sleep(5000);
            driver.get("https://www.amazon.com/");
            config = new Properties();
            try {
                FileInputStream input = new FileInputStream("src/resources/Properties/Config.properties");
                config.load(input);
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void readExcel() throws IOException, InterruptedException {
            FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"\\src\\resources\\Excel\\Search_items.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("SearchTerm");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell searchTerm = row.getCell(0);
                Cell expectedResult = row.getCell(1);

                String searchData = searchTerm.getStringCellValue();
                String expectedOutput = expectedResult.getStringCellValue();

                WebElement searchBar = driver.findElement(By.id("twotabsearchtextbox"));

                searchBar.sendKeys(searchData);
                Thread.sleep(3000);
                WebElement optionsList = driver.findElement(By.xpath("//div[@class='left-pane-results-container']"));
                List<WebElement> options=optionsList.findElements(By.xpath(".//div//div[@class='s-suggestion-container']"));
                if (options.size()>0){
                    Assert.assertEquals(expectedOutput,"Y");
                }
                else {
                    Assert.assertEquals(expectedOutput, "N");
                }
                searchBar.clear();
            }
        }
        @Test
        public void autocompletesuggestion() throws IOException, InterruptedException {
            String sheetName = "SearchTerm";
            Object[][] data = TestExcelUtil.readData(sheetName);
            WebElement searchBar,optionsList;
            String searchData,expectedOutput;
            List<WebElement> options;

            for(Object[] row: data){
                searchData =(String) row[0];
                expectedOutput = (String) row[1];

                searchBar = driver.findElement(By.id(config.getProperty("search.searchBar.id")));
                searchBar.sendKeys(searchData);
                Thread.sleep(3000);

                optionsList = driver.findElement(By.xpath(config.getProperty("search.optionsList.xpath")));
                options=optionsList.findElements(By.xpath(config.getProperty("search.options.xpath")));
                if (options.size()>0){
                    for(WebElement option : options){
                        Assert.assertTrue(option.getText().contains(searchData));
                    }
                }
                searchBar.clear();
            }
        }
}
