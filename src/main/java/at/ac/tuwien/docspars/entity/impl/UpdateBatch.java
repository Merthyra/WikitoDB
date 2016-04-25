package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ProcessMetrics;

public class UpdateBatch extends Batch {

  public UpdateBatch() {}

  @Override
  public void updateMetrics(ProcessMetrics metrics) {
    metrics.updateBatch(this);
  }

  @Override
  public void persist(PersistanceService service) {
    service.updateBatch(this);
  }

  @Override
  public String toString() {
    return "Update " + super.toString();
  }

}
