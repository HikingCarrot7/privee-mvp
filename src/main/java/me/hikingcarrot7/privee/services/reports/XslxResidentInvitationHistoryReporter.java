package me.hikingcarrot7.privee.services.reports;

import me.hikingcarrot7.privee.models.ExcelReportResult;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static me.hikingcarrot7.privee.services.reports.ExcelWorkbookUtils.centeredCellStyle;

public class XslxResidentInvitationHistoryReporter {
  private final Resident resident;
  private final List<Invitation> invitations;
  private Workbook workbook;
  private Sheet sheet;

  public XslxResidentInvitationHistoryReporter(Resident resident, List<Invitation> invitations) {
    this.resident = resident;
    this.invitations = invitations;
  }

  public ExcelReportResult generateReport() {
    try {
      return tryToGenerateReport();
    } catch (IOException e) {
      throw new RuntimeException("Error generating report", e);
    }
  }

  private ExcelReportResult tryToGenerateReport() throws IOException {
    var byteArray = new ByteArrayOutputStream();
    workbook = new SXSSFWorkbook(1000);
    generateSheetWithInvitationsHistory();
    workbook.write(byteArray);
    workbook.close();
    return new ExcelReportResult(
        "Invitations history for resident " + resident.getFullName(),
        byteArray.toByteArray()
    );
  }

  private void generateSheetWithInvitationsHistory() {
    sheet = workbook.createSheet("History");
    createSheetHeader();
    createInvitationsTableHeader();
    populateInvitationsTable();
  }

  private void createSheetHeader() {
    Font sheetHeaderFont = workbook.createFont();
    sheetHeaderFont.setBold(true);
    sheetHeaderFont.setFontHeightInPoints((short) 18);

    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.cloneStyleFrom(centeredCellStyle(workbook));
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setFont(sheetHeaderFont);

    Row sheetHeader = sheet.createRow(0);
    sheetHeader.createCell(0);
    sheetHeader.setHeight((short) -1);

    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
    Cell cell = sheet.getRow(0).getCell(0);
    cell.setCellValue("Invitations History");
    cell.setCellStyle(cellStyle);
  }

  private void createInvitationsTableHeader() {
    Row tableHeader = sheet.createRow(1);

    Cell createdAtCol = tableHeader.createCell(0);
    createdAtCol.setCellValue("Created At");
    createdAtCol.setCellStyle(centeredCellStyle(workbook));

    Cell expirationDateCol = tableHeader.createCell(1);
    expirationDateCol.setCellValue("Expiration Date");
    expirationDateCol.setCellStyle(centeredCellStyle(workbook));

    Cell guestNameCol = tableHeader.createCell(2);
    guestNameCol.setCellValue("Guest Name");
    guestNameCol.setCellStyle(centeredCellStyle(workbook));

    Cell guestEmailCol = tableHeader.createCell(3);
    guestEmailCol.setCellValue("Guest Email");
    guestEmailCol.setCellStyle(centeredCellStyle(workbook));

    Cell vehiclePlateCol = tableHeader.createCell(4);
    vehiclePlateCol.setCellValue("Vehicle Plate");
    vehiclePlateCol.setCellStyle(centeredCellStyle(workbook));

    Cell statusCol = tableHeader.createCell(5);
    statusCol.setCellValue("Status");
    statusCol.setCellStyle(centeredCellStyle(workbook));
  }

  public void populateInvitationsTable() {
    for (int i = 0; i < invitations.size(); i++) {
      Invitation invitation = invitations.get(i);
      Row row = sheet.createRow(i + 2); // Starts at 2 because of the title and the table header.

      Cell createdAtCell = row.createCell(0);
      createdAtCell.setCellValue(invitation.getCreatedAt().toString());
      createdAtCell.setCellStyle(centeredCellStyle(workbook));

      Cell expirationDateCell = row.createCell(1);
      expirationDateCell.setCellValue(invitation.getExpirationDate().toString());
      expirationDateCell.setCellStyle(centeredCellStyle(workbook));

      Cell guestNameCell = row.createCell(2);
      guestNameCell.setCellValue(invitation.getGuestName());
      guestNameCell.setCellStyle(centeredCellStyle(workbook));

      Cell guestEmailCell = row.createCell(3);
      guestEmailCell.setCellValue(invitation.getGuestEmail());
      guestEmailCell.setCellStyle(centeredCellStyle(workbook));

      Cell vehiclePlateCell = row.createCell(4);
      vehiclePlateCell.setCellValue(invitation.getVehiclePlate());
      vehiclePlateCell.setCellStyle(centeredCellStyle(workbook));

      Cell statusCell = row.createCell(5);
      statusCell.setCellValue(invitation.getStatus().toString());
      statusCell.setCellStyle(centeredCellStyle(workbook));
    }
  }

}
