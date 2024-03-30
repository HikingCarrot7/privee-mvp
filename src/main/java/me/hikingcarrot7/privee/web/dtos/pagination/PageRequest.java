package me.hikingcarrot7.privee.web.dtos.pagination;

import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class PageRequest {
  @QueryParam("page")
  @DefaultValue("1")
  @Positive
  private int page;

  @QueryParam("size")
  @DefaultValue("10")
  @Positive
  private int size;
}
