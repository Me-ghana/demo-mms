package gov.nasa.gsfc.mms;

import gov.nasa.gsfc.mms.generated.GeneratedModelType;
import gov.nasa.jpl.aerie.merlin.driver.ActivityDirective;
import gov.nasa.jpl.aerie.merlin.driver.ActivityDirectiveId;
import gov.nasa.jpl.aerie.merlin.driver.DirectiveTypeRegistry;
import gov.nasa.jpl.aerie.merlin.driver.MissionModel;
import gov.nasa.jpl.aerie.merlin.driver.MissionModelBuilder;
import gov.nasa.jpl.aerie.merlin.driver.SimulationDriver;
import gov.nasa.jpl.aerie.merlin.driver.SimulationResults;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;

public class SampleSimulationTest {
  @Test
  void testSimulation() {
    final var simulationStartTime = Instant.EPOCH;
    final var simulationDuration = Duration.of(10, HOURS);

    final Map<ActivityDirectiveId, ActivityDirective> schedule = new HashMap<>();

    schedule.put(new ActivityDirectiveId(1L), new ActivityDirective(
      Duration.ZERO,
      "GncChangeControlMode",
      Map.of("gncControlMode", SerializedValue.of("THRUSTERS")),
      null,
      true
    ));

    schedule.put(new ActivityDirectiveId(2L), new ActivityDirective(
      Duration.of(2, HOURS),
      "GncChangeControlMode",
      Map.of("gncControlMode", SerializedValue.of("REACTION_WHEELS")),
      null,
      true
    ));

    schedule.put(new ActivityDirectiveId(3L), new ActivityDirective(
      Duration.of(7, HOURS),
      "GncChangeControlMode",
      Map.of("gncControlMode", SerializedValue.of("THRUSTERS")),
      null,
      true
    ));


    final var results = simulate(new Configuration(), simulationStartTime, simulationDuration, schedule);
    for (final var segment : results.discreteProfiles.get("/gncControlMode").getRight()) {
      System.out.println(segment.extent() + " " + segment.dynamics());
    }
  }

  public SimulationResults simulate(
    Configuration configuration,
    Instant simulationStartTime,
    Duration simulationDuration,
    Map<ActivityDirectiveId, ActivityDirective> schedule
  ) {
    return SimulationDriver.simulate(
      makeMissionModel(new MissionModelBuilder(), simulationStartTime, configuration),
      schedule,
      simulationStartTime,
      simulationDuration,
      simulationStartTime,
      simulationDuration
    );
  }

  private static MissionModel<?> makeMissionModel(final MissionModelBuilder builder, final Instant planStart, final Configuration config) {
    final var factory = new GeneratedModelType();
    final var registry = DirectiveTypeRegistry.extract(factory);
    final var model = factory.instantiate(planStart, config, builder);
    return builder.build(model, registry);
  }
}
