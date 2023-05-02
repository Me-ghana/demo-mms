package gov.nasa.gsfc.mms;

import gov.nasa.gsfc.mms.activities.gnc.GncChangeControlMode;
import gov.nasa.gsfc.mms.models.gnc.GncControlMode;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import static gov.nasa.gsfc.mms.generated.ActivityActions.call;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MerlinExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class SampleUnitTest {
  private final Mission mission;

  public SampleUnitTest(Registrar registrar) {
    this.mission = new Mission(registrar, new Configuration());
  }

  @Test
  public void myFirstTest() {
    // Set pre-condition
    mission.gncControlMode.set(GncControlMode.REACTION_WHEELS);

    // Create activity
    final var activity = new GncChangeControlMode(GncControlMode.THRUSTERS);

    // Call activity
    call(mission, activity);

    // Check post-condition
    assertEquals(GncControlMode.THRUSTERS, mission.gncControlMode.get());
  }
}
