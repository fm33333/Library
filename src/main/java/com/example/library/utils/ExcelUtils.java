package com.example.library.utils;

import com.example.library.domain.ApiCode;
import com.example.library.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 *
 * @author 冯名豪
 * @date 2022/08/24
 */
@Slf4j
public final class ExcelUtils {

    public static final String EXCEL_XLS = "xls";
    public static final String EXCEL_XLSX = "xlsx";

    private ExcelUtils() {

    }

    /**
     * 生成表格byte数组
     *
     * @param filename  文件名称
     * @param titles    表头名称
     * @param rowData   行数据
     * @param sheetName sheet名
     * @return byte数组
     */
    public static byte[] getExcelByte(String filename, String[] titles, List<Map<String, String>> rowData, String sheetName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeExcel(baos, filename, titles, rowData, sheetName);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("表格生成失败", e);
            throw new BusinessException(ApiCode.ERROR, "表格生成失败");
        }
    }

    /**
     * 创建Excel文件
     *
     * @param output
     * @param filename 文件名称
     * @param titles   表头名称
     * @param rowData  行数据
     * @throws BusinessException
     */
    public static void writeExcel(OutputStream output, String filename, String[] titles,
                                  List<Map<String, String>> rowData, String sheetName) {
        writeExcelWithSheets(output, filename, Collections.singletonList(titles), Collections.singletonList(rowData), Collections.singletonList(sheetName));
    }

    /**
     * 创建Excel文件(多sheet页)
     *
     * @param output
     * @param filename      文件名称
     * @param titleLists    表头名称
     * @param rowDataList   行数据
     * @param sheetNameList sheet名称数据
     * @throws BusinessException
     */
    public static void writeExcelWithSheets(OutputStream output, String filename, List<String[]> titleLists,
                                            List<List<Map<String, String>>> rowDataList, List<String> sheetNameList) {
        Workbook workbook = getWorkbook(filename);
        Map<String, CellStyle> styles = createStyles(workbook);

        for (int sheetNum = 0; sheetNum < titleLists.size(); sheetNum++) {
            String[] titles = titleLists.get(sheetNum);
            Iterable<Map<String, String>> rowData = rowDataList.get(sheetNum);
            String sheetName = sheetNameList.get(sheetNum);

            Sheet sheet = workbook.createSheet(sheetName);
            // 创建标题行
            Row oneRow = sheet.createRow(0);

            // 存储标题在Excel文件中的序号
            Map<String, Integer> titleOrder = new HashMap<>(titles.length);
            for (int i = 0; i < titles.length; i++) {
                sheet.setColumnWidth(i, 35 * 256);
                oneRow.setHeightInPoints(35);
                Cell cell = oneRow.createCell(i);
                cell.setCellStyle(styles.get("header"));
                String title = titles[i];
                cell.setCellValue(title);
                titleOrder.put(title, i);
            }

            // 行号
            int rowIndex = 1;

            // 写入正文
            for (Map<String, String> rowDatum : rowData) {
                Row row = sheet.createRow(rowIndex);
                row.setHeightInPoints(22);
                for (Map.Entry<String, String> map : rowDatum.entrySet()) {
                    // 获取列名
                    String title = map.getKey();
                    // 根据列名获取序号
                    int i = titleOrder.get(title);
                    // 在指定序号处创建cell
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(styles.get("title"));

                    // 获取列的值
                    String val = map.getValue();
                    cell.setCellValue(val);
                }
                rowIndex++;
            }
        }

        // 输出文件
        try {
            workbook.write(output);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 根据文件路径获取Workbook对象
     *
     * @param filename 文件名称
     */
    private static Workbook getWorkbook(String filename) {
        String extension = FilenameUtils.getExtension(filename);
        if (!StringUtils.hasText(extension)) {
            log.error("后缀名为为空:{}", filename);
            throw new BusinessException(ApiCode.ERROR);
        }
        if (!EXCEL_XLS.equals(extension) && !EXCEL_XLSX.equals(extension)) {
            log.error("该文件非Excel文件:{}", filename);
            throw new BusinessException(ApiCode.ERROR, "该文件非Excel文件");
        }

        Workbook workbook;
        if (filename.endsWith(EXCEL_XLS)) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook();
        }
        return workbook;
    }

    /**
     * 设置格式
     */
    private static Map<String, CellStyle> createStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>(2);
        String fontName = "微软雅黑";

        // 头样式
        CellStyle headerStyle = cellStyle(workbook);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font headerFont = workbook.createFont();
        // 加粗
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setFontName(fontName);
        headerStyle.setFont(headerFont);
        styles.put("header", headerStyle);

        // 行样式
        CellStyle titleStyle = cellStyle(workbook);
        Font titleFont = workbook.createFont();
        titleFont.setFontName(fontName);
        titleFont.setFontHeightInPoints((short) 10);
        titleStyle.setFont(titleFont);
        styles.put("title", titleStyle);
        return styles;
    }

    private static CellStyle cellStyle(Workbook workbook) {
        // 文件头样式
        CellStyle cellStyle = workbook.createCellStyle();

        // 垂直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 前景色
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

        // 设置边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        return cellStyle;
    }

}
