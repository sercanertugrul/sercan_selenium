package test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;


public class BeymenTest {

    static WebDriver driver;
    public static String text;
    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sercan.ertugrul\\Desktop\\HSYS\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("disable-extensions"); //extentions kapatma
        options.addArguments("disable-popup-blocking"); //popupları kapatma
        options.addArguments("disable-infobars"); // disabling infobars
        driver = new ChromeDriver(options);


    }

    @Test
    public void Test() throws InterruptedException, IOException {

        driver.manage().window().maximize();

        driver.get("https://beymen.com");
        Thread.sleep(5000);


         driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("genderManButton")).click();
        Thread.sleep(1000);
        if (driver.findElements(By.xpath("//input[@placeholder='Ürün, Marka Arayın']")).size() > 0) {

            driver.findElement(By.xpath("//input[@placeholder='Ürün, Marka Arayın']")).click();
            System.out.println("Giriş işlemi başarılı");


        }
        else {
            driver.quit();

        }
        WebElement searchEl = driver.findElement(By.className("o-header__search--input"));



        ReadExcel(1, 0);
        searchEl.sendKeys(text);
        Thread.sleep(5000);
        searchEl.sendKeys(Keys.CONTROL, "a");
        searchEl.sendKeys(Keys.DELETE);
        Thread.sleep(1000);


        ReadExcel(1, 1);
        searchEl.sendKeys(text);
        searchEl.sendKeys(Keys.ENTER);
        Thread.sleep(5000);

        driver.findElement(By.xpath("(//*[@id='productList']/div[2]/div/div/div[1]/a)[1]")).click();

        Thread.sleep(5000);

        String desc =driver.findElement(By.xpath("//span[@class='o-productDetail__description']")).getText();
        driver.findElement(By.xpath("//span[text()='XXL']")).click();
        driver.findElement(By.id("addBasket")).click();
        Thread.sleep(5000);
        String fiyat = driver.findElement(By.xpath("(//span[@class='m-productCard__newPrice'])[1]")).getText();
        System.out.println(fiyat);

        driver.findElement(By.xpath("//span[contains(text(),'Sepetim')]")).click();
        Thread.sleep(5000);

        if(driver.findElement(By.xpath("(//li[@class='m-orderSummary__item'])[1]")).getText() != fiyat){

            System.out.println("Ürün fiyatı sepetteki fiyattan farklıdır");
        }
        Thread.sleep(2000);
        WebElement piece = driver.findElement(By.id("quantitySelect0-key-0"));
        piece.click();
        Thread.sleep(2000);
        piece.sendKeys(Keys.ARROW_DOWN);
        piece.click();
        Thread.sleep(5000);

         driver.findElement(By.id("removeCartItemBtn0-key-0")).click();
        Thread.sleep(5000);

        PrintWriter out = new PrintWriter("C:\\Users\\sercan.ertugrul\\Desktop\\fiyat.txt");

        out.println("Ürün Adı : " +desc);
        out.println("Ürün Fiyatı : " +fiyat);

        out.close();


        if (driver.findElements(By.xpath("//a[@title='Alışverişe Devam Et']")).size() > 0) {


            System.out.println("Sepetinizde ürün bulunmamaktadır");


        }
        else {
            driver.quit();

        }




    }
@After

public void quit() throws InterruptedException {

    driver.quit();
}
    public static String ReadExcel(int rowNum, int columNum) throws IOException {
        int currentRowNumber = 1;
        int currentColumnNumber = 0;
        String readingValue = "";
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\sercan.ertugrul\\Desktop\\beymen.xlsx"));

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet spreadsheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = spreadsheet.iterator();

        while (rowIterator.hasNext()) {
            XSSFRow row = (XSSFRow) rowIterator.next();
            if (rowNum != currentRowNumber) {
                currentRowNumber++;
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (columNum != currentColumnNumber) {
                    currentColumnNumber++;
                    continue;
                }
                readingValue = cell.getStringCellValue();
                break;
            }
            break;
        }

        fis.close();
        text = readingValue;
        return text;
    }


}
