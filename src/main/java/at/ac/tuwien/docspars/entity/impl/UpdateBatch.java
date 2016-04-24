package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.util.ProcessMetrics;

public class UpdateBatch extends Batch {

  public UpdateBatch() {}

  @Override
  public String toString() {
    return "Update " + super.toString();
  }

  @Override
  public void updateMetrics(ProcessMetrics metrics) {
    metrics.updateBatch(this);
  }

}
