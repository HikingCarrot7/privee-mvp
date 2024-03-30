package me.hikingcarrot7.privee.scheduling;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import me.hikingcarrot7.privee.services.ResidentService;
import me.hikingcarrot7.privee.utils.PerformanceLog;

@Singleton
public class NotificationScheduler {
  @Inject private ResidentService residentService;

  @PerformanceLog
  @Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
  public void automaticallyScheduled() {
    System.out.println("===============> Notification Scheduler <===============");
  }

}
