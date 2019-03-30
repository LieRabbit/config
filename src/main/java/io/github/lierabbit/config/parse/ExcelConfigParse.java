package io.github.lierabbit.config.parse;


import io.github.lierabbit.config.domain.Resource;
import io.github.lierabbit.config.domain.ResourceItem;
import io.github.lierabbit.config.utils.FileUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Excel解析器
 * 注意：excel填入数字的时候建议使用文本格式，因为当你输入1的时候poi读取出来的数字是1.0，也就是浮点数，当你的类字段为整数时转换失败！！！
 * 例如
 * String str = "1.0"
 * Long value = Long.valueOf(str)
 * 将抛出 {@link NumberFormatException}
 *
 * @author xyy
 * @since 2019-03-27 14:37
 */
public class ExcelConfigParse implements ConfigParse {
    private static final String[] EXCEL_SUFFIX_ARRAY = {"xlsx", "xls"};

    @Override
    public boolean supports(File file) {
        String suffix = FileUtils.getSuffix(file);
        for (String excelSuffix : EXCEL_SUFFIX_ARRAY)
            if (excelSuffix.equals(suffix))
                return true;
        return false;
    }

    @Override
    public Collection<Resource> parse(File file) {
        List<Resource> resourceList = new LinkedList<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
            for (Sheet sheet : workbook) {
                int lastRowNum = sheet.getLastRowNum();
                // 行数小于2是空表
                if (lastRowNum < 2)
                    continue;
                Resource resource = new Resource();
                // 资源名默认为表名
                resource.setName(sheet.getSheetName());
                List<ResourceItem> itemList = new LinkedList<>();
                resource.setItemList(itemList);
                // 字段名
                String[] fieldNames = null;
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        // 第一行是注释
                    } else if (row.getRowNum() == 1) {
                        // 第二行是字段名
                        fieldNames = new String[row.getLastCellNum()];
                        int i = 0;
                        for (Cell cell : row)
                            fieldNames[i++] = getStringValue(cell);
                    } else {
                        // 第三行开始是配置内容
                        ResourceItem item = new ResourceItem();
                        int i = 0;
                        for (Cell cell : row) {
                            assert fieldNames != null;
                            item.addElement(fieldNames[i++], getStringValue(cell));
                        }
                        itemList.add(item);
                    }
                }
                resourceList.add(resource);
            }

        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resourceList;
    }

    /**
     * 获取单元格的字符串值
     *
     * @param cell
     * @return
     */
    private String getStringValue(Cell cell) {
        String str;
        switch (cell.getCellType()) {
            case STRING:
                str = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    str = cell.getDateCellValue().toString();
                } else {
                    str = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                str = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                str = cell.getCellFormula();
                break;
            case BLANK:
                str = null;
                break;
            default:
                str = null;
        }

        return str;
    }
}
