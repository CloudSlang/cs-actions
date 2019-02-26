package io.cloudslang.content.excel.actions;

import io.cloudslang.content.constants.ReturnCodes;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.excel.services.ModifyCellService.isMergedCell;
import static io.cloudslang.content.excel.utils.Constants.*;
import static org.junit.Assert.*;

/**
 * Created by danielmanciu 21.02.2019
 */
public class ModifyCellTest {
    private static ModifyCell toTest;

    @BeforeClass
    /**
     * Create an Excel document, add 3 sheets and write some information in each (5 rows and 10 columns)
     * @throws IOException
     */
    public static void setUp() throws IOException {
        // generate xml
        final HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(SHEET1);
        HSSFRow rowhead;

        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue(22);
            }
        }

        //set string cells
        sheet = workbook.createSheet(SHEET2);
        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue("abc");
            }
        }

        //set merge region
        CellRangeAddress region = new CellRangeAddress(0, 2, 0, 2);
        sheet.addMergedRegion(region); //will merge from A1 to C3.

        sheet = workbook.createSheet(SHEET3);
        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue(0);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(FILE_NAME);

        workbook.write(fileOut);
        fileOut.close();
    }

    @AfterClass
    /**
     * delete de xml document that was created at setUp.
     */
    public static void CleanUp() {
        File f = new File(FILE_NAME);
        f.delete();
    }

    @Before
    public void before() {
        toTest = new ModifyCell();
    }

    @Test
    /**
     * test execute method on a simple cell.
     * @throws Exception
     */
    public void testExecute() throws Exception {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "1",
                "1",
                "7",
                DEFAULT_COLUMN_DELIMITER);

        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));

        //check if the cell was modified
        final FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        final HSSFWorkbook workbook = new HSSFWorkbook(fis);
        final HSSFSheet sheet = workbook.getSheet(SHEET1);
        final HSSFRow row = sheet.getRow(1);
        final HSSFCell cell = row.getCell(1);

        assertEquals(7, (long) cell.getNumericCellValue());
    }

    @Test
    /**
     * test execute method on a cell from a group of merged cells.
     * @throws Exception
     */
    public void testExecute2() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                "1",
                "1",
                "7",
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("0", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * test execute method with invalid row index.
     * @throws Exception
     */
    public void testExecute3() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET3,
                "-1",
                "1",
                "7",
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("The value '-1' in rowIndex input is not a number.", result.get(RETURN_RESULT));
        assertEquals("The value '-1' in rowIndex input is not a number.", result.get(EXCEPTION));
    }

    @Test
    /**
     * test execute method with invalid columnIndex.
     * @throws Exception
     */
    public void testExecute4() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET3,
                "1",
                "-1",
                "7",
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("The value '-1' in columnIndex input is not a number.", result.get(RETURN_RESULT));
        assertEquals("The value '-1' in columnIndex input is not a number.", result.get(EXCEPTION));
    }

    @Test
    /**
     * test execute method with empty newValue.
     * @throws Exception
     */
    public void testExecute5() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET3,
                "1",
                "1",
                "",
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("The newValue input can't be empty.", result.get(RETURN_RESULT));
        assertEquals("The newValue input can't be empty.", result.get(EXCEPTION));
    }

    @Test
    /**
     * test execute method with the size of data input different than size of columnIndex input.
     * @throws Exception
     */
    public void testExecute6() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET3,
                "1",
                "1",
                "3,4,5,6",
                ",");

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("The data input is not valid. The size of data input should be the same as size of columnIndex input, which is 1.", result.get(RETURN_RESULT));
        assertEquals("The data input is not valid. The size of data input should be the same as size of columnIndex input, which is 1.", result.get(EXCEPTION));
    }

    @Test
    /**
     * test execute method with several inputs.
     * @throws Exception
     */
    public void testExecute7() throws Exception {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "1",
                "1,2,3,4",
                "3,4,5,6",
                ",");

        assertEquals("1", result.get(RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));

        //check if the cell was modified
        final FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        final HSSFWorkbook workbook = new HSSFWorkbook(fis);
        final HSSFSheet sheet = workbook.getSheet(SHEET1);
        final HSSFRow row = sheet.getRow(1);

        HSSFCell cell = row.getCell(1);
        assertEquals(3, (long) cell.getNumericCellValue());

        cell = row.getCell(2);
        assertEquals(4, (long) cell.getNumericCellValue());

        cell = row.getCell(3);
        assertEquals(5, (long) cell.getNumericCellValue());

        cell = row.getCell(4);
        assertEquals(6, (long) cell.getNumericCellValue());

        cell = row.getCell(5); //test other cell was not affected
        assertEquals(22, (long) cell.getNumericCellValue());
    }

    @Test
    public void testWithDate() throws Exception {
        final String dateString = "22 Dec 1994";
        final DateFormat format = new SimpleDateFormat("dd MMM yyy");
        final Date date = format.parse(dateString);

        System.out.println(date);

        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "1",
                "1",
                dateString,
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));

        final FileInputStream fileInputStream = new FileInputStream(new File(FILE_NAME));
        final HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        final HSSFSheet sheet = workbook.getSheet(SHEET1);
        final HSSFRow row = sheet.getRow(1);
        final HSSFCell cell = row.getCell(1);
        assertEquals(date, cell.getDateCellValue());
    }

    @Test
    /**
     * test isMergedCell method on a merged cell
     * @throws IOException
     */
    public void testIsMergedCell() throws IOException {
        final FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        final HSSFWorkbook workbook = new HSSFWorkbook(fis);
        final HSSFSheet sheet = workbook.getSheet(SHEET2);

        boolean res = isMergedCell(sheet, 1, 1);
        assertTrue(res);
    }

    @Test
    /**
     * test isMergedCell method on a cell that is not in a merged region.
     * @throws IOException
     */
    public void testIsMergedCell2() throws IOException {
        FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheet(SHEET1);

        boolean res = isMergedCell(sheet, 1, 1);
        assertFalse(res);
    }
}
