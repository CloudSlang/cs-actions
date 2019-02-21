package io.cloudslang.content.excel.actions;

import io.cloudslang.content.excel.services.ExcelServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.ROWS_COUNT;
import static org.apache.poi.ss.usermodel.CellType.ERROR;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.junit.Assert.assertEquals;

public class GetRowIndexByConditionTest {
    private static final String SHEET1 = "Sheet1";
    private static final String SHEET2 = "Sheet2";
    private static final String SHEET3 = "Sheet3";
    private static final String SHEET4 = "Sheet4";
    private static final String FILE_NAME = System.getProperty("java.io.tmpdir") + "testFile.xls";
    private static GetRowIndexByCondition toTest;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    /**
     * Create an Excel document, add 4 sheets and write some information in each (5 rows and 10 columns)
     * @throws IOException
     */
    public static void setUp() throws IOException {
        toTest = new GetRowIndexByCondition();

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

        //set merge region
        CellRangeAddress region = new CellRangeAddress(0, 2, 0, 2);
        sheet.addMergedRegion(region); //will merge from A1 to C3.

        //set a formula cell
        String strFormula = "SUM(A1:A3)";
        rowhead = sheet.createRow(5);
        HSSFCell cell = rowhead.createCell(5);
        cell.setCellType(FORMULA);
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

        sheet = workbook.createSheet(SHEET4);
        rowhead = sheet.createRow(0);
        cell = rowhead.createCell(1);
        cell.setCellValue("50%");
        rowhead = sheet.createRow(1);
        cell = rowhead.createCell(1);
        cell.setCellValue(new Date());
        rowhead = sheet.createRow(1);
        cell = rowhead.createCell(1);
        cell.setCellValue("0");
        rowhead = sheet.createRow(1);
        cell = rowhead.createCell(1);
        cell.setCellValue("03:22:00");
        rowhead = sheet.createRow(1);
        cell = rowhead.createCell(1);
        cell.setCellValue(Calendar.getInstance());

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
    public void instance() {
        toTest = new GetRowIndexByCondition();
    }

    @Test
    /**
     * Test execute method with string value and default operator, no header.
     * @throws Exception
     */
    public void testExecute() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                "no",
                "",
                "1",
                "",
                "abc"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("5", result.get(ROWS_COUNT));
        assertEquals("0,1,2,3,4", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execute method with '<' operator and with header.
     * @throws Exception
     */
    public void testExecute2() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "no",
                "",
                "5",
                "<",
                "22"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("1", result.get(ROWS_COUNT));
        assertEquals("5", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execute method with '>' operator and cells containing percentage format.
     * @throws Exception
     */
    public void testExecute3() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "",
                "",
                "1",
                ">",
                "66%"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("2", result.get(ROWS_COUNT));
        assertEquals("3,4", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execute method with '<=' operator and date format.
     * @throws Exception
     */
    public void testExecute4() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET4,
                "",
                "",
                "1",
                "<=",
                "1990/05/05"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("0", result.get(ROWS_COUNT));
        assertEquals("", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execute method with '>=' operator and time format.
     * @throws Exception
     */
    public void testExecute5() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET4,
                "",
                "",
                "1",
                "==",
                "03:00:00"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("0", result.get(ROWS_COUNT));
        assertEquals("", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execite method with invalid operator.
     * @throws Exception
     */
    public void testExecute6() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                "",
                "1",
                "3",
                "<>",
                "ab"
        );
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("The <> for operator input is not a valid operator.", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execite method with invalid header.
     * @throws Exception
     */
    public void testExecute7() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                "maybe",
                "",
                "1",
                "",
                ""
        );
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("The maybe for hasHeader input is not valid.The valid values are yes/no.", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execite method with empty COLUMNINDEXTOQUERY_INPUT.
     * @throws Exception
     */
    public void testExecute8() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                "",
                "",
                "",
                "",
                "3"
        );
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("The columnIndextoQuery can't be null or empty.", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execite method with Invalid column index value.
     * @throws Exception
     */
    public void testExecute9() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET4,
                "",
                "",
                "A",
                "",
                ""
        );
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("The A for columnIndextoQuery input is not a valid number value.", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execite method with '!=' operator and datetime format(YYYY/MM/DD HH:MM:SS).
     * @throws Exception
     */
    public void testExecute11() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET4,
                "",
                "",
                "1",
                "!=",
                "1999/09/09 03:00:00"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("1", result.get(ROWS_COUNT));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execute method with string values.
     * @throws Exception
     */
    public void testExecute12() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET2,
                "",
                "",
                "1",
                "!=",
                "ab"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("4", result.get(ROWS_COUNT));
        assertEquals("1,2,3,4", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test execite method on an array with merged cells.
     * @throws Exception
     */
    public void testExecute13() {
        Map<String, String> result = toTest.execute(FILE_NAME,
                SHEET1,
                "",
                "",
                "1",
                "==",
                "22"
        );
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("2", result.get(ROWS_COUNT));
        assertEquals("3,4", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * Test getMergeCell on a sheet that contains region A1:C3 marged.
     * This will set A2, B2, C2 as error type because cIndex is 1.
     * When cIndex is 0 -> A1,B1,C1 are affected.
     */
    public void testgetMergedCell() throws Exception {
        FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheet(SHEET1);

        ExcelServiceImpl.getMergedCell(sheet, 0, 1);

        HSSFRow row = sheet.getRow(0);
        HSSFCell cell = row.getCell(0);
        assertEquals(NUMERIC, cell.getCellType());
        cell = row.getCell(1);
        assertEquals(ERROR, cell.getCellType());
        cell = row.getCell(2);
        assertEquals(NUMERIC, cell.getCellType());
        row = sheet.getRow(1);
        cell = row.getCell(0);
        assertEquals(NUMERIC, cell.getCellType());
        cell = row.getCell(1);
        assertEquals(ERROR, cell.getCellType());
        cell = row.getCell(2);
        assertEquals(NUMERIC, cell.getCellType());
        row = sheet.getRow(2);
        cell = row.getCell(0);
        assertEquals(NUMERIC, cell.getCellType());
        cell = row.getCell(1);
        assertEquals(ERROR, cell.getCellType());
        cell = row.getCell(2);
        assertEquals(NUMERIC, cell.getCellType());
        row = sheet.getRow(3);
        cell = row.getCell(2);
        assertEquals(NUMERIC, cell.getCellType());

        ExcelServiceImpl.getMergedCell(sheet, 0, 0);
        row = sheet.getRow(0);
        cell = row.getCell(0);
        //0 0 is top left of merged region (not affected).
        assertEquals(NUMERIC, cell.getCellType());
        row = sheet.getRow(1);
        cell = row.getCell(0);
        assertEquals(ERROR, cell.getCellType());
        row = sheet.getRow(2);
        cell = row.getCell(0);
        assertEquals(ERROR, cell.getCellType());
    }
}
