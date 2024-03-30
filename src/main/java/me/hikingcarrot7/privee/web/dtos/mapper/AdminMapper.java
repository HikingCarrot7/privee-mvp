package me.hikingcarrot7.privee.web.dtos.mapper;

import me.hikingcarrot7.privee.models.Admin;
import me.hikingcarrot7.privee.web.dtos.admin.AdminDTO;
import me.hikingcarrot7.privee.web.dtos.admin.UpdateAdminDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface AdminMapper {

  Admin toAdmin(AdminDTO adminDTO);

  Admin toAdmin(UpdateAdminDTO updateAdminDTO);

  @Mapping(target = "password", ignore = true)
  AdminDTO toAdminDTO(Admin admin);

  List<AdminDTO> toAdminDTOs(List<Admin> admins);

}
