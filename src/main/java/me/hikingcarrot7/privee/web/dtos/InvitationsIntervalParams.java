package me.hikingcarrot7.privee.web.dtos;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.QueryParam;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Setter
@NoArgsConstructor
public class InvitationsIntervalParams {
  private static final String DATE_FORMAT = "yyyy-MM-dd";

  @QueryParam("startDate")
  private String startDate;

  @QueryParam("endDate")
  private String endDate;

  @PostConstruct
  public void init() {
    checkIfValidInterval();
  }

  private void checkIfValidInterval() {
    if (mustIgnoreInterval()) {
      return;
    }
    if (Objects.isNull(startDate)) {
      throw new IllegalArgumentException("The startDate date must be provided");
    }
    if (parseDate(startDate).isAfter(parseDate(endDate))) {
      throw new IllegalArgumentException("The startDate date must be before the endDate date");
    }
  }

  public boolean mustIgnoreInterval() {
    return Objects.isNull(startDate) && Objects.isNull(endDate);
  }

  public LocalDate getStartDate() {
    return parseDate(startDate);
  }

  public LocalDate getEndDate() {
    if (Objects.isNull(endDate)) {
      return LocalDate.now();
    } else {
      return parseDate(endDate);
    }
  }

  private LocalDate parseDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    return LocalDate.parse(date, formatter);
  }

}
