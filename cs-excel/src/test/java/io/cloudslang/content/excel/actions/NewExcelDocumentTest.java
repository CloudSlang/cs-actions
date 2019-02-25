package io.cloudslang.content.excel.actions;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.excel.utils.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by alexandra boicu 20/2/2019
 */
public class NewExcelDocumentTest {

    private static NewExcelDocument toTest;

    @Before
    public void setUp() throws IOException {
        toTest = new NewExcelDocument();

        final File f = new File(FILE_NAME_2);
        if (f.exists()) {
            f.delete();
        }
    }

    @After
    /**
     * delete the xml document that was created at setUp.
     */
    public void CleanUp() {
        final File f = new File(FILE_NAME_2);
        f.delete();
    }

    @Test
    /**
     * test functionality of execute method.
     * @throws Exception
     */
    public void testExecute() throws Exception {
        Map<String, String> result = toTest.execute(FILE_NAME_2, "sheet1,sheet12", ",");

        assertEquals("0", result.get(RETURN_CODE));

        File f = new File(FILE_NAME_2);
        assertTrue(f.exists());
        FileInputStream fis = new FileInputStream(f);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        assertNotNull(workbook);
        HSSFSheet sheet = workbook.getSheet("sheet1");
        assertNotNull(sheet);
        sheet = workbook.getSheet("sheet12");
        assertNotNull(sheet);
    }

    @Test
    /**
     * test execute method with Filename input to an existing file.
     * @throws Exception
     */
    public void testExecute2() throws Exception {

        File f = new File(FILE_NAME_2);
        if (!f.exists()) {
            f.createNewFile();
        }
        Map<String, String> result = toTest.execute(FILE_NAME_2, "sheet1,sheet12", ",");
        f.delete();

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("File already exists", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * test execute method with Filename input with bad extension.
     * @throws Exception
     */
    public void testExecute3() {
        Map<String, String> result = toTest.execute(BAD_EXT_FILE_NAME_2, "sheet1,sheet12", ",");
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals(BAD_CREATE_EXCEL_FILE_MSG, result.get(RETURN_RESULT));
    }

    @Test
    /**
     * test execute method with empty sheetnames.
     * this will create default "sheet1, sheet2, sheet3".
     * @throws Exception
     */
    public void testExecute4() {
        Map<String, String> result = toTest.execute(FILE_NAME_2, "", "");
        assertEquals("0", result.get(RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).toString().contains("created successfully"));
    }

    @Test
    /**
     * test execute method with empty sheetnames.
     * @throws Exception
     */
    public void testExecute5() {
        Map<String, String> result = toTest.execute(FILE_NAME_2, ",", ",");
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals(EXCEPTION_WORKSHEET_NAME_EMPTY, result.get(RETURN_RESULT));
    }
}
