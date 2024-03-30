package me.hikingcarrot7.privee.services.reports;

import org.apache.poi.ss.usermodel.*;

public class ExcelWorkbookUtils {

  public static CellStyle dateCellStyle(Workbook workbook) {
    CellStyle dateCellStyle = workbook.createCellStyle();
    CreationHelper creationHelper = workbook.getCreationHelper();
    short dateFormat = creationHelper.createDataFormat().getFormat("MM/dd/yyyy");
    dateCellStyle.setDataFormat(dateFormat);
    return dateCellStyle;
  }

  public static CellStyle centeredCellStyle(Workbook workbook) {
    CellStyle centeredCellStyle = workbook.createCellStyle();
    centeredCellStyle.setAlignment(HorizontalAlignment.CENTER);
    return centeredCellStyle;
  }

  public static String getCellAddressAsString(Cell cell) {
    return cell.getAddress().formatAsString();
  }

}
