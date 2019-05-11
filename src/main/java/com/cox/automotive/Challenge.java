package com.cox.automotive;

import io.swagger.client.model.DealerAnswer;

import java.util.List;

public class Challenge {

  public static void main(String args[]) {
    Challenge challenge = new Challenge();
    try {
      challenge.processDataset();
    } catch (RuntimeException e) {
      System.out.println("Unable to run challenge: " + e.getMessage());
    }
  }

  /**
   * Process a dataset and submit the response
   */
  public void processDataset() {

    final Dataset dataset = new Dataset();
    final Vehicles vehicles = new Vehicles();

    String datasetId = dataset.getDatasetId();
    List<Integer> vehicleIds = vehicles.getVehicleIds(datasetId);
    List<DealerAnswer> dealers = vehicles.getDealersWithTheirVehicles(datasetId, vehicleIds);

    dataset.submitAnswer(datasetId, dealers);
  }
}