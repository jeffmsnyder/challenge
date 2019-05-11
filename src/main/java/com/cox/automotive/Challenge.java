package com.cox.automotive;

import io.swagger.client.model.DealerAnswer;

import java.util.List;

/**
 * Runs the challenge to get the dealers and their vehicles from a given dataset.
 *
 * @author jeff.snyder
 */
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
  private void processDataset() {

    final Dataset dataset = new Dataset();
    final Vehicles vehicles = new Vehicles();

    String datasetId = dataset.getDatasetId();
    List<Integer> vehicleIds = vehicles.getVehicleIds(datasetId);
    List<DealerAnswer> dealers = vehicles.getDealersWithTheirVehicles(datasetId, vehicleIds);

    dataset.submitAnswer(datasetId, dealers);
  }
}