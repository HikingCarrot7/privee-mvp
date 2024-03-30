package me.hikingcarrot7.privee.metrics;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import java.util.List;

@ApplicationScoped
public class TestMetric {

  @PostConstruct
  public void init() {
    System.out.println("TestMetric initialized");
  }

  @Gauge(unit = MetricUnits.NONE, name = "testMetricGauge", description = "Test gauge")
  public int getTotal() {
    List<String> elements = List.of("a", "b", "c", "d", "e");
    return elements.size();
  }

}
