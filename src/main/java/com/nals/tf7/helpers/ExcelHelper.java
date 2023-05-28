package com.nals.tf7.helpers;

import com.nals.tf7.errors.NotFoundException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class ExcelHelper {

    private ExcelHelper() {
    }

    public static String getFullNameByStudentId(final String filePath, final String studentId)
        throws IOException {
        Path file = Paths.get(filePath);
        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(file))) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell studentIdCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                Cell fullNameCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (studentIdCell != null && studentIdCell.getCellType() == CellType.STRING) {
                    String id = studentIdCell.getStringCellValue();
                    if (Objects.equals(id, studentId)) {
                        return fullNameCell.getStringCellValue();
                    }
                }
            }
        }

        throw new NotFoundException("Student ID not found");
    }
}
