package me.hikingcarrot7.privee.bootstrap;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import lombok.extern.java.Log;
import me.hikingcarrot7.privee.models.CloudFile;
import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.models.invitation.InvitationStatus;
import me.hikingcarrot7.privee.services.AdminService;
import me.hikingcarrot7.privee.services.GatekeeperService;
import me.hikingcarrot7.privee.services.InvitationService;
import me.hikingcarrot7.privee.services.ResidentService;
import me.hikingcarrot7.privee.web.dtos.InvitationDTO;
import me.hikingcarrot7.privee.web.dtos.admin.AdminDTO;
import me.hikingcarrot7.privee.web.dtos.gatekeeper.GatekeeperDTO;
import me.hikingcarrot7.privee.web.dtos.mapper.AdminMapper;
import me.hikingcarrot7.privee.web.dtos.mapper.GatekeeperMapper;
import me.hikingcarrot7.privee.web.dtos.mapper.InvitationMapper;
import me.hikingcarrot7.privee.web.dtos.mapper.ResidentMapper;
import me.hikingcarrot7.privee.web.dtos.resident.ResidentDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

@Singleton
@Startup
@Log
public class Seeder {
  @Inject private ResidentService residentService;
  @Inject private ResidentMapper residentMapper;

  @Inject private AdminService adminService;
  @Inject private AdminMapper adminMapper;

  @Inject private GatekeeperService gatekeeperService;
  @Inject private GatekeeperMapper gatekeeperMapper;

  @Inject private InvitationService invitationService;
  @Inject private InvitationMapper invitationMapper;

  @Inject
  @ConfigProperty(name = "privee.seed_database", defaultValue = "false")
  private boolean seedDatabase;

  @PostConstruct
  public void init() {
    if (!seedDatabase) {
      return;
    }
    log.log(Level.INFO, "Seeding database...");

    ResidentDTO resident1 = ResidentDTO.builder()
        .firstName("Ricardo Nicolás")
        .lastName("Canul Ibarra")
        .phone("9992676251")
        .email("ricardoibarra2044@gmail.com")
        .password("123456789")
        .build();
    Resident savedResident1 = residentService.saveResident(residentMapper.toResident(resident1));

    AdminDTO admin1 = AdminDTO.builder()
        .firstName("Admin")
        .lastName("Admin")
        .phone("9992676250")
        .email("admin@gmail.com")
        .password("123456789")
        .build();
    adminService.saveAdmin(adminMapper.toAdmin(admin1));

    GatekeeperDTO gatekeeper1 = GatekeeperDTO.builder()
        .firstName("Gatekeeper")
        .lastName("Gatekeeper")
        .email("gatekeeper@gmail.com")
        .password("123456789")
        .phone("9992676253")
        .build();
    gatekeeperService.saveGatekeeper(gatekeeperMapper.toGatekeeper(gatekeeper1));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    InvitationDTO invitation1 = InvitationDTO.builder()
        .expirationDate(LocalDateTime.parse("2024-03-30 11:30:00", formatter))
        .guestName("Guest")
        .guestEmail("guest@gmail.com")
        .vehiclePlate("ABC-1234")
        .qrCode(CloudFile.builder()
            .originalFilename("qr-code-a1e8a1dd-9fe3-4299-a800-f92a986afd37")
            .publicId("privee/qr-code-a1e8a1dd-9fe3-4299-a800-f92a986afd3716118894081953295265_slqtyw")
            .url("https://res.cloudinary.com/dzmzrbuta/image/upload/v1711766954/privee/qr-code-a1e8a1dd-9fe3-4299-a800-f92a986afd3716118894081953295265_slqtyw.jpg")
            .build()
        )
        .status(InvitationStatus.PENDING)
        .token("1622dabd-7a8d-468c-8c54-e4f4ce1359de")
        .build();
    invitationService.createInvitation(savedResident1.getId(), invitationMapper.toInvitation(invitation1));
  }

}
