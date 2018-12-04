package com.w2a.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.w2a.base.TestBase;

public class TestUtil extends TestBase {

	public static String screenshotPath;
	public static String screenshotName;

	public static void captureScreenshot() throws IOException {

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		FileUtils.copyFile(scrFile,
				new File(System.getProperty("user.dir") + "/target/surefire-reports/html/" + screenshotName));

	}

	@DataProvider(name="dp")
	public Object[][] getData(Method m) {

		String sheetName = m.getName(); //sheet name is same as method name
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		
		//Object[][] data = new Object[rows - 1][cols];
		
		Object[][] data = new Object[rows - 1][1]; //reading data from excel using Hashmap based on column name
		
		Hashtable<String,String> table = null; 
		for (int rowNum = 2; rowNum <= rows; rowNum++) { // 2

			table = new Hashtable<String,String>(); //for each row, a new Hasttable of String type will be created 
			//eg. key=firstname, value= Raman   key=lastname, value= Arora  key=postcode  value=A234wd key=runmode value=Y

			
			for (int colNum = 0; colNum < cols; colNum++) {

				// data[0][0]
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				//accepts key and value pair
				//key > column= colNum and row = 1st row . so key will always remain first row with different columns
				//value > column = colNum and row = rowNum. value will be filled for all rows with different columns
		
				data[rowNum - 2][0] = table; //column number should be 0 as method accepts only 1 column
				//data[rowNum - 2][colNum] = excel.getCellData(sheetName,colNum,rowNum);  //-2
			}

		}

		return data;

	}
	
	
	public static boolean isTestRunnable(String testName, ExcelReader excel){
		
		String sheetName="test_suite";
		int rows = excel.getRowCount(sheetName);
		
		
		for(int rNum=2; rNum<=rows; rNum++){
			
			String testCase = excel.getCellData(sheetName, "TCID", rNum);
			
			if(testCase.equalsIgnoreCase(testName)){
				
				String runmode = excel.getCellData(sheetName, "Runmode", rNum);
				
				if(runmode.equalsIgnoreCase("Y"))
					return true;
				else
					return false;
			}
			
			
		}
		return false;
	}
	
}
