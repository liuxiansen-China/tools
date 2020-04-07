package cn.gjing.tools.excel.write.resolver;

import cn.gjing.tools.excel.BigTitle;
import cn.gjing.tools.excel.Excel;
import cn.gjing.tools.excel.exception.ExcelResolverException;
import cn.gjing.tools.excel.metadata.CustomWrite;
import cn.gjing.tools.excel.metadata.ExcelWriterResolver;
import cn.gjing.tools.excel.write.listener.WriteListener;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Xlsx resolver
 *
 * @author Gjing
 **/
class ExcelWriteXlsxResolver implements ExcelWriterResolver {
    private SXSSFWorkbook workbook;
    private OutputStream outputStream;
    private ExcelExecutor excelExecutor;

    ExcelWriteXlsxResolver(SXSSFWorkbook workbook,Map<Class<? extends WriteListener>, List<WriteListener>> writeListenerMap) {
        this.workbook = workbook;
        this.excelExecutor = new ExcelExecutor(workbook, writeListenerMap);
    }

    @Override
    public void writeTitle(BigTitle bigTitle, Sheet sheet) {
        this.excelExecutor.setBigTitle(bigTitle, sheet);
    }

    @Override
    public ExcelWriterResolver writeHead(List<Field> headFieldList, List<String[]> headNames, Sheet sheet, boolean needHead,
                                         Map<String, String[]> boxValues, Excel excel, boolean needValid) {
        this.excelExecutor.setHead(headFieldList, headNames, sheet, needHead, boxValues, excel,needHead);
        return this;
    }

    @Override
    public ExcelWriterResolver write(List<?> data, Sheet sheet, List<Field> headFieldList) {
        this.excelExecutor.setValue(data, headFieldList, sheet);
        return this;
    }

    @Override
    public void customWrite(CustomWrite processor) {
        processor.process();
    }

    @Override
    public void flush(HttpServletResponse response, String fileName) {
        response.setContentType("application/vnd.ms-excel");
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            this.outputStream = response.getOutputStream();
            this.workbook.write(outputStream);
        } catch (IOException e) {
            throw new ExcelResolverException("Excel cache data refresh failure, " + e.getMessage());
        } finally {
            try {
                if (this.outputStream != null) {
                    this.outputStream.flush();
                    this.outputStream.close();
                }
                if (this.workbook != null) {
                    this.workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
