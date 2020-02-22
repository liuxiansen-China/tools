package cn.gjing.tools.excel.valid;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author Gjing
 **/
public interface ExcelDateValidation {
    /**
     * Custom time validation rules
     *
     * @param dateValid DateValid
     * @param sheet     sheet
     * @param firstRow  First row
     * @param lastRow   Last row
     * @param firstCol  First col
     * @param lastCol   Last col
     */
    default void valid(DateValid dateValid, Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {

    }

}
