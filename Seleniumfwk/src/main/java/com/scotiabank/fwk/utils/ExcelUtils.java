package com.scotiabank.fwk.utils;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Utility methods that help with Excel files.
 * 
 * @author Venkat
 *
 */
public class ExcelUtils {

	/**
	 * Reads and returns data from the excel file from sheet sheetName. If
	 * sheetName is null, it reads the first sheet.
	 * 
	 * @param excelFilename
	 * @param sheetName
	 * @return
	 * @throws Exception
	 * @author Venkat
	 */
	public static List<List<HSSFCell>> readDataFromFile(String excelFilename,
			String sheetName) throws Exception {
		List<List<HSSFCell>> workSheetData = new ArrayList<List<HSSFCell>>();

		FileInputStream fis = null;
		try {
			//
			// Create a FileInputStream that will be use to read the
			// excel file.
			//
			fis = new FileInputStream(excelFilename);

			//
			// Create an excel workbook from the file system.
			//
			HSSFWorkbook workbook = new HSSFWorkbook(fis);

			HSSFSheet sheet;
			// If the sheet name is null, get the first sheet.
			if (sheetName == null) {
				sheet = workbook.getSheetAt(0);
			} else {
				sheet = workbook.getSheet(sheetName);
			}

			//
			// each sheet's rows and on each row's cells. We store the
			// data read on an ArrayList so that we can print
			//
			Iterator<?> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				Iterator<?> cells = row.cellIterator();

				List<HSSFCell> data = new ArrayList<HSSFCell>();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
					data.add(cell);
				}

				workSheetData.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return workSheetData;
	}

	
	
	/**
	 * Overloaded method
	 * 
	 * @param excelFileName
	 * @return
	 * @throws Exception
	 * @author Venkat
	 */
	public static List<List<HSSFCell>> readDataFromFile(String excelFileName)
			throws Exception {
		return readDataFromFile(excelFileName, null);
	}
}

