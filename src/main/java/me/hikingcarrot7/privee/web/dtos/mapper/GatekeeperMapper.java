package me.hikingcarrot7.privee.web.dtos.mapper;

import me.hikingcarrot7.privee.models.Gatekeeper;
import me.hikingcarrot7.privee.web.dtos.gatekeeper.GatekeeperDTO;
import me.hikingcarrot7.privee.web.dtos.gatekeeper.UpdateGatekeeperDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface GatekeeperMapper {

  @Mappings({
      @Mapping(target = "id", ignore = true),
  })
  Gatekeeper toGatekeeper(GatekeeperDTO gatekeeperDTO);

  Gatekeeper toGatekeeper(UpdateGatekeeperDTO updateGatekeeperDTO);

  @Mapping(target = "password", ignore = true)
  GatekeeperDTO toGatekeeperDTO(Gatekeeper gatekeeper);

  List<GatekeeperDTO> toGatekeeperDTOs(List<Gatekeeper> gatekeepers);

}
