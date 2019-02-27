/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.excel.actions;

import io.cloudslang.content.excel.services.DeleteCellService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.excel.utils.Constants.FILE_NAME;
import static io.cloudslang.content.excel.utils.Constants.SHEET1;
import static io.cloudslang.content.excel.utils.Constants.SHEET2;
import static io.cloudslang.content.excel.utils.Constants.SHEET3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by alexandra boicu 20/2/2019
 */
public class DeleteCellTest {
    private static DeleteCell toTest;

    @BeforeClass
    /**
     * Create an Excel document, add 3 sheets and write some information in each (5 rows and 10 columns)
     * @throws IOException
     */
    public static void setUp() throws IOException {
        toTest = new DeleteCell();

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

        sheet = workbook.createSheet(SHEET2);
        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue(22);
            }
        }

        sheet = workbook.createSheet(SHEET3);
        for (int i = 0; i < 5; i++) {
            rowhead = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                rowhead.createCell(j).setCellValue(22);
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

    @Test
    /**
     * Test deleteCell by deleting a cell from an excel document.
     * @throws Exception
     */
    public void testExecute() throws Exception {
        Map<String, String> result;
        result = toTest.execute(FILE_NAME, SHEET1, "0", "0");
        assertEquals("0", result.get(RETURN_CODE));

        //check if the cell was deleted
        FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheet(SHEET1);
        HSSFRow row = sheet.getRow(0);
        HSSFCell cell = row.getCell(0);
        assertNull(cell);

        //verify some others cell were not affected
        cell = row.getCell(1);
        assertEquals(22, (long) cell.getNumericCellValue());

        row = sheet.getRow(1);
        cell = row.getCell(0);
        assertEquals(22, (long) cell.getNumericCellValue());

        sheet = workbook.getSheet(SHEET2);
        row = sheet.getRow(0);
        cell = row.getCell(0);
        assertEquals(22, (long) cell.getNumericCellValue());
    }

    @Test
    /**
     * Test deleteCell by deleting several cells from an excel document.
     * @throws Exception
     */
    public void testExecute2() throws Exception {
        Map<String, String> result;

        result = toTest.execute(FILE_NAME, SHEET2, "2,3", "2,3");
        assertEquals("0", result.get(RETURN_CODE));

        //check if the cells were deleted
        FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheet(SHEET2);
        HSSFRow row = sheet.getRow(2);
        HSSFCell cell = row.getCell(2);
        assertNull(cell);

        cell = row.getCell(3);
        assertNull(cell);

        row = sheet.getRow(3);
        cell = row.getCell(2);
        assertNull(cell);

        cell = row.getCell(3);
        assertNull(cell);
        //verify some others cell were not affected
        cell = row.getCell(1);
        assertEquals(22, (long) cell.getNumericCellValue());

        row = sheet.getRow(1);
        cell = row.getCell(0);
        assertEquals(22, (long) cell.getNumericCellValue());

        sheet = workbook.getSheet(SHEET1);
        row = sheet.getRow(2);
        cell = row.getCell(2);
        assertEquals(22, (long) cell.getNumericCellValue());
    }

    @Test
    /**
     * Test deleteCell by deleting all cells from a sheet of an excel document.
     * @throws Exception
     */
    public void testExecute3() throws Exception {
        Map<String, String> result;

        result = toTest.execute(FILE_NAME, SHEET3, "", "");
        assertEquals("0", result.get(RETURN_CODE));

        //check if all cells were deleted
        FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheet(SHEET3);
        HSSFRow row;
        HSSFCell cell;
        for (int i = 0; i < 5; i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < 10; j++) {
                cell = row.getCell(0);
                assertNull(cell);
            }
        }

        //verify some others cell from other sheet was not affected
        sheet = workbook.getSheet(SHEET1);
        row = sheet.getRow(1);
        cell = row.getCell(1);
        assertEquals(22, (long) cell.getNumericCellValue());
    }

    @Test
    /**
     * Test deleteCell with invalid index.
     * Bad behaviour, the exception should be added in ActionResult.
     * @throws Exception
     */
    public void testExecute4() {
        Map<String, String> result;
        result = toTest.execute(FILE_NAME, SHEET2, "1", "A");
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("The value 'A' in columnIndex input is not a number.", result.get(RETURN_RESULT));
        assertEquals("The value 'A' in columnIndex input is not a number.", result.get(EXCEPTION));
    }

    @Test
    /**
     * Test deleteCell with invalid index.
     * @throws Exception
     */
    public void testExecute5() {
        Map<String, String> result;
        result = toTest.execute(FILE_NAME, SHEET2, "1", "1200");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("0", result.get(RETURN_RESULT));
    }

    @Test
    /**
     * test DeleteCell method on a 1x2 region from sheet1.
     * @throws IOException
     */

    public void testDeleteCell() throws IOException {
        FileInputStream fis = new FileInputStream(new File(FILE_NAME));
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheet(SHEET1);
        ArrayList<Integer> rowIndex = new ArrayList<Integer>();
        ArrayList<Integer> columnIndex = new ArrayList<Integer>();

        rowIndex.add(4);
        columnIndex.add(6);
        columnIndex.add(7);

        HSSFRow row = sheet.getRow(4);
        HSSFCell cell = row.getCell(6);
        assertNotNull(cell);

        DeleteCellService.deleteCell(sheet, rowIndex, columnIndex);

        cell = row.getCell(6);
        assertNull(cell);
        cell = row.getCell(7);
        assertNull(cell);
        cell = row.getCell(5);
        assertNotNull(cell);
    }
}
