package me.hikingcarrot7.privee.web.dtos.mapper;

import me.hikingcarrot7.privee.models.Resident;
import me.hikingcarrot7.privee.web.dtos.resident.ResidentDTO;
import me.hikingcarrot7.privee.web.dtos.resident.UpdateResidentDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta", builder = @Builder(disableBuilder = true))
public interface ResidentMapper {

  Resident toResident(ResidentDTO residentDTO);

  Resident toResident(UpdateResidentDTO updateResidentDTO);

  @Mapping(target = "password", ignore = true)
  ResidentDTO toResidentDTO(Resident resident);

  List<ResidentDTO> toResidentDTOs(List<Resident> residents);

}
