package gov.nasa.gsfc.mms.activities.gnc;

import gov.nasa.gsfc.mms.models.gnc.GncControlMode;
import gov.nasa.gsfc.mms.Mission;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

@ActivityType("GncChangeControlMode")
public final class GncChangeControlMode {
  @Parameter
  public GncControlMode gncControlMode = GncControlMode.THRUSTERS;

  public GncChangeControlMode() {
  }

  public GncChangeControlMode(GncControlMode gncControlMode) {
    this.gncControlMode = gncControlMode;
  }

  @EffectModel
  public void run(final Mission mission) {
    mission.gncControlMode.set(this.gncControlMode);
  }
}
