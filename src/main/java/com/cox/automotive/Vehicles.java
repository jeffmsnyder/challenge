package com.cox.automotive;

import com.sun.istack.internal.NotNull;
import io.swagger.client.ApiException;
import io.swagger.client.api.VehiclesApi;
import io.swagger.client.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is used for vehicle processing.
 */
public class Vehicles {

  /**
   * Get the list of vehicle ids associated with the dataset
   *
   * @param datasetId current dataset being processed
   * @return a list of integers which are the vehicle ids (no duplicates, no null values)
   */
  @NotNull
  public List<Integer> getVehicleIds(String datasetId) {
    // Need to create a new API each time as they are not thread safe
    final VehiclesApi vehiclesApi = new VehiclesApi();
    try {
      // Get vehicles for dataset
      VehicleIdsResponse response = vehiclesApi.vehiclesGetIds(datasetId);
      if (response == null) {
        throw new ApiException("Invalid response received from API to get the vehicle ids.");
      }
      // Consider this the same as an empty list
      if (response.getVehicleIds() == null) {
        return new ArrayList<>();
      }

      // Remove any duplicate or null values from the list before returning it
      return response.getVehicleIds().stream()
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toList());
    } catch (ApiException e) {
      throw new RuntimeException("Unable to retrieve the vehicles for dataset: " + datasetId, e);
    }
  }

  /**
   * Get the dealers with their associated vehicles
   *
   * @param datasetId current dataset being processed
   * @param vehicleIds list of vehicle ids to be processed
   * @return a list of dealers containing the vehicles associated with them
   */
  @NotNull
  public List<DealerAnswer> getDealersWithTheirVehicles(String datasetId, List<Integer> vehicleIds) {

    Dealers dealers = new Dealers();

    vehicleIds.parallelStream()
      .forEach(vehicleId -> {
        VehicleResponse vehicleResponse = getVehicleInfo(datasetId, vehicleId);

        // Create the vehicle answer to associate with the dealer
        VehicleAnswer vehicleAnswer = new VehicleAnswer();
        vehicleAnswer.setMake(vehicleResponse.getMake());
        vehicleAnswer.setModel(vehicleResponse.getModel());
        vehicleAnswer.setVehicleId(vehicleResponse.getVehicleId());
        vehicleAnswer.setYear(vehicleResponse.getYear());

        // Add the vehicle information to the dealer
        dealers.updateDealer(datasetId, vehicleResponse.getDealerId(), vehicleAnswer);
      });

    return dealers.getDealerAnswers();
  }

  /**
   * Retreive the vehicle information
   *
   * @param datasetId current dataset being processed
   * @param vehicleId the list of vehicle ids whose information is to be retrieved
   * @return the vehicle response which contains the vehicle information
   */
  @NotNull
  private VehicleResponse getVehicleInfo(String datasetId, Integer vehicleId) {
    // Need to create a new API each time as they are not thread safe
    final VehiclesApi vehiclesApi = new VehiclesApi();
    try {
      // For each vehicle, get its information
      VehicleResponse vehicleResponse = vehiclesApi.vehiclesGetVehicle(datasetId, vehicleId);
      if (vehicleResponse == null) {
        throw new ApiException("Invalid response received from API to get the vehicle.");
      }

      return vehicleResponse;
    } catch (ApiException e) {
      throw new RuntimeException("Unable to retrieve vehicle information for (dataset, id): (" + datasetId + "," + vehicleId + ")", e);
    }
  }
}