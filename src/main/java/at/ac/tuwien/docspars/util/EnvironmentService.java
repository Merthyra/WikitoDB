package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.PersistVariant;

public class EnvironmentService {

  private final PersistVariant mode;
  private static EnvironmentService service;

  private EnvironmentService(PersistVariant mode) {
    this.mode = mode;
  }


  public EnvironmentService getInstance(PersistVariant mode) {
    if (service == null) {
      service = new EnvironmentService(mode);
    }
    return EnvironmentService.service;
  }



  // Batch
  // Properties


}
