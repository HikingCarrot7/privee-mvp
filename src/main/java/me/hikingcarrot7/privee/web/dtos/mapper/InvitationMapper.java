package me.hikingcarrot7.privee.web.dtos.mapper;

import me.hikingcarrot7.privee.models.invitation.Invitation;
import me.hikingcarrot7.privee.web.dtos.InvitationDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(
    componentModel = "jakarta",
    builder = @Builder(disableBuilder = true),
    uses = ResidentMapper.class
)
public interface InvitationMapper {

  @Mappings({
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "token", ignore = true),
      @Mapping(target = "status", ignore = true),
      @Mapping(target = "qrCode", ignore = true),
  })
  Invitation toInvitation(InvitationDTO invitationDTO);

  InvitationDTO toInvitationDTO(Invitation invitation);

  List<InvitationDTO> toInvitationDTOs(List<Invitation> invitations);

}
