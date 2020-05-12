package com.example.admin.common;

import com.example.admin.bean.User;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class POIUtils {

    /**
     * 解析 excel 文件
     * @param file
     * @return
     */
    public static List<User> excelToUsers(MultipartFile file){
        String filename = file.getOriginalFilename();
        List<User> list = new ArrayList<>();
        User user;
        Workbook workbook = null;
        try {
            if ("xls".equals(FilenameUtils.getExtension(filename))){
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            if ("xlsx".equals(FilenameUtils.getExtension(filename))){
                workbook = new XSSFWorkbook(file.getInputStream());
            }
            //int numberOfSheets = workbook.getNumberOfSheets();
            //只取第一个 sheet
            Sheet sheet = workbook.getSheetAt(0);
            //获取表格的行数
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            //第一行是表头，从第二行开始取数据
            for (int i=1;i<physicalNumberOfRows;i++){
                //获取行
                Row row = sheet.getRow(i);
                //跳过空行
                if (row == null){
                    continue;
                }
                user = new User();
                user.setPassword("$10$oE39aG10kB/rFu2vQeCJTu/V/v4n6DRR0f8WyXRiAYvBpmadoOBE.");
                //获取列数
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                Cell cell = null;
                String cellValue = "";
                DecimalFormat df = new DecimalFormat("#");
                for (int j=0;j<physicalNumberOfCells;j++){
                    cell = row.getCell(j);
                    if (cell.getCellTypeEnum() == CellType.STRING){
                        cellValue = cell.getStringCellValue();
                    }
                    if (cell.getCellTypeEnum() == CellType.NUMERIC){
//                        if (DateUtil.isCellDateFormatted(cell)){
//                            cellValue = DateFormatConverter.convert(Locale.CHINA,"yyyy-MM-dd");
//                        }
                        //这里只有手机号，不做其他类型判断
                        cellValue = df.format(cell.getNumericCellValue());
                    }
                    switch (j){
                        case 0:
                            user.setUsername(cellValue);
                            break;
                        case 1:
                            user.setTruename(cellValue);
                            break;
                        case 2:
                            user.setPhone(cellValue);
                            break;
                        default:
                            break;
                    }
                }
                list.add(user);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 导出用户数据
     * @param userList
     * @return
     */
    public static ResponseEntity<byte[]> generateUserExcel(List<User> userList){
        //创建一个 excel 2003 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //设置表头样式
        HSSFCellStyle headerStyle = setHeaderStyle(workbook);
        //设置内容样式
        HSSFCellStyle dataStyle = setDataStyle(workbook);
        //创建 sheet
        HSSFSheet sheet = workbook.createSheet("用户信息");
        //设置列宽
        sheet.setColumnWidth(0,10*256);
        sheet.setColumnWidth(1,10*256);
        sheet.setColumnWidth(2,16*256);
        //创建 row 第一行是表格的列名
        HSSFRow row = sheet.createRow(0);

        HSSFCell titleCell = row.createCell(0);
        titleCell.setCellValue("用户名");
        titleCell.setCellStyle(headerStyle);

        titleCell = row.createCell(1);
        titleCell.setCellValue("真实姓名");
        titleCell.setCellStyle(headerStyle);

        titleCell = row.createCell(2);
        titleCell.setCellValue("手机号");
        titleCell.setCellStyle(headerStyle);

        HSSFCell dataCell;
        for (int i=0;i<userList.size();i++){
            User user = userList.get(i);
            HSSFRow dataRow = sheet.createRow(i+1);

            dataCell = dataRow.createCell(0);
            dataCell.setCellValue(user.getUsername());
            dataCell.setCellStyle(dataStyle);

            dataCell = dataRow.createCell(1);
            dataCell.setCellValue(user.getTruename());
            dataCell.setCellStyle(dataStyle);

            dataCell = dataRow.createCell(2);
            dataCell.setCellValue(user.getPhone());
            dataCell.setCellStyle(dataStyle);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment",new String("用户信息表.xls".getBytes("UTF-8"),"ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e){
            e.printStackTrace();
        }

        return new ResponseEntity<byte[]>(baos.toByteArray(),headers, HttpStatus.CREATED);
    }

    /**
     * 设置表头样式
     * @param wb
     * @return
     */
    public static HSSFCellStyle setHeaderStyle(HSSFWorkbook wb){
        //创建标题样式
        HSSFCellStyle headerStyle = wb.createCellStyle();
        //设置边框 上下左右 细边线
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        //设置单元格对齐方式 左对齐 垂直居中
        headerStyle.setAlignment(HorizontalAlignment.LEFT);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置字体 宋体 加粗
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerStyle.setFont(font);

        return headerStyle;
    }

    /**
     * 设置内容样式
     * @param wb
     * @return
     */
    public static HSSFCellStyle setDataStyle(HSSFWorkbook wb){
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);

        return cellStyle;
    }

}
