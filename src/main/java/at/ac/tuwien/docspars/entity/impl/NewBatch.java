package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ProcessMetrics;

public class NewBatch extends Batch {

  public NewBatch() {
    super();
  }

  @Override
  public void updateMetrics(ProcessMetrics metrics) {
    metrics.addBatch(this);
  }

  @Override
  public void persist(PersistanceService service) {
    service.addBatch(this);
  }

  @Override
  public String toString() {
    return "Add " + super.toString();
  }
}
