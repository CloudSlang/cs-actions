package io.cloudslang.content.excel.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import io.cloudslang.content.constants.ReturnCodes;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.excel.utils.Constants.*;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GetCellTest {
    private static GetCell toTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    /**
     * Create an Excel document, add 3 sheets and write some information in each (5 rows and 10 columns)
     * @throws IOException
     */
    public static void setUp() throws IOException {
        // generate xml
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(SHEET1);
        HSSFRow rowhead;

        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue(22);
            }
        }

        //set a formula cell
        String strFormula = "SUM(A1:A3)";
        rowhead = sheet.createRow(5);
        HSSFCell cell = rowhead.createCell(5);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(strFormula);

        //set string cells
        sheet = workbook.createSheet(SHEET2);
        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue("abc");
            }
        }

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
        toTest = new GetCell();
    }

    @Test
    /**
     * Test execite method for row 1 col 1 from sheet1.
     * @throws Exception
     */
    public void testExecute() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                YES,
                DEFAULT_FIRST_ROW_INDEX,
                "1",
                "1",
                DEFAULT_ROW_DELIMITER,
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(COLUMNS_COUNT));
        assertEquals("1", result.get(ROWS_COUNT));
        assertEquals("22", result.get(RETURN_RESULT));
        assertEquals("22.0", result.get(HEADER));
    }

    @Test
    /**
     * test execute method on a formulla cell and with no header.
     * @throws Exception
     */
    public void testExecute2() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                NO,
                DEFAULT_FIRST_ROW_INDEX,
                "5",
                "5",
                DEFAULT_ROW_DELIMITER,
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(COLUMNS_COUNT));
        assertEquals("1", result.get(ROWS_COUNT));
        assertEquals("66.0", result.get(RETURN_RESULT));
        assertNull(result.get(HEADER));
    }

    @Test
    /**
     * test execute with invalid header.
     * @throws Exception
     */
    public void testExecute3() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "maybe",
                DEFAULT_FIRST_ROW_INDEX,
                "5",
                "5",
                DEFAULT_ROW_DELIMITER,
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("The maybe for hasHeader input is not valid.The valid values are yes/no.", result.get(RETURN_RESULT));
        assertEquals("The maybe for hasHeader input is not valid.The valid values are yes/no.", result.get(EXCEPTION));
    }

    @Test
    /**
     * test execute method on a null cell.
     * @throws Exception
     */
    public void testExecute4() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                NO,
                DEFAULT_FIRST_ROW_INDEX,
                "12",
                "12",
                DEFAULT_ROW_DELIMITER,
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("0", result.get(COLUMNS_COUNT));
        assertEquals("0", result.get(ROWS_COUNT));
        assertEquals("", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * test execute method with several cells input from sheet2.
     * @throws Exception
     */
    public void testExecute5() {
        final Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                NO,
                DEFAULT_FIRST_ROW_INDEX,
                "2,3",
                "2,3",
                DEFAULT_ROW_DELIMITER,
                DEFAULT_COLUMN_DELIMITER);

        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("2", result.get(COLUMNS_COUNT));
        assertEquals("2", result.get(ROWS_COUNT));
        assertEquals("abc,abc|abc,abc", result.get(RETURN_RESULT));
    }

}