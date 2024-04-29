package com.oysb.utils.io.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/* loaded from: classes2.dex */
public class ExcelHelper {

    /* loaded from: classes2.dex */
    public interface ExcelReadListener {
        boolean onReadCell(int i, int i2, Boolean bool);
    }

    public static List<String[]> readExcel(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        try {
            Workbook workbook = Workbook.getWorkbook(new FileInputStream(str));
            Sheet sheet = workbook.getSheet(str2);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            for (int i = 0; i < rows; i++) {
                String[] strArr = new String[rows];
                arrayList.add(strArr);
                for (int i2 = 0; i2 < columns; i2++) {
                    strArr[i2] = sheet.getCell(i, i2).getContents();
                }
            }
            workbook.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return arrayList;
    }

    public static void readExcel(String str, String str2, ExcelReadListener excelReadListener) {
        try {
            Workbook workbook = Workbook.getWorkbook(new FileInputStream(str));
            Sheet sheet = workbook.getSheet(str2);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            Boolean bool = false;
            for (int i = 0; i < rows; i++) {
                for (int i2 = 0; i2 < columns; i2++) {
                    excelReadListener.onReadCell(i, i2, bool);
                    if (bool.booleanValue()) {
                        break;
                    }
                }
                if (bool.booleanValue()) {
                    break;
                }
            }
            workbook.close();
        } catch (Exception unused) {
        }
    }

    public void createExcel(String str, String str2, List<String[]> list) {
        try {
            WritableWorkbook createWorkbook = Workbook.createWorkbook(new FileOutputStream(str));
            WritableSheet createSheet = createWorkbook.createSheet(str2, 0);
            int rows = createSheet.getRows();
            int columns = createSheet.getColumns();
            for (int i = 0; i < rows; i++) {
                for (int i2 = 0; i2 < columns; i2++) {
                    createSheet.addCell(new Label(i, i2, list.get(i)[i2]));
                }
            }
            createWorkbook.close();
        } catch (Exception unused) {
        }
    }
}
