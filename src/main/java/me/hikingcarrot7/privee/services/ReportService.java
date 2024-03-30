package me.hikingcarrot7.privee.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.models.ExcelReportResult;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.services.reports.XslxResidentInvitationHistoryReporter;
import me.hikingcarrot7.privee.web.dtos.InvitationsIntervalParams;

import java.util.List;

@ApplicationScoped
public class ReportService {
  @Inject private ResidentService residentService;
  @Inject private InvitationService invitationService;

  public ExcelReportResult generateXlsxReportForResidentInvitationHistory(
      String residentId,
      InvitationsIntervalParams intervalParams
  ) {
    Resident resident = residentService.getResidentById(residentId);
    List<Invitation> invitations = invitationService.getInvitationsOnInterval(residentId, intervalParams);
    var reporter = new XslxResidentInvitationHistoryReporter(resident, invitations);
    return reporter.generateReport();
  }

}
