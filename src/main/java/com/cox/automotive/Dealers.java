package com.cox.automotive;

import io.swagger.client.ApiException;
import io.swagger.client.api.DealersApi;
import io.swagger.client.model.DealerAnswer;
import io.swagger.client.model.DealersResponse;
import io.swagger.client.model.VehicleAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This contains the information about the dealers (name and vehicles) for a particular dataset
 *
 * @author jeff.snyder
 */
public class Dealers {

  // Contains the association of dealer id to its dealer for a dataset
  // This is a concurrent hash map to allow parallel processing of vehicles being added to the dealer (thread-safe)
  private final Map<Integer, DealerAnswer> dealerMap = new ConcurrentHashMap<>();

  /**
   * Adds the vehicle data to the dealer.
   *
   * @param datasetId     current dataset being processed
   * @param dealerId      dealer id associated with the vehicle
   * @param vehicleAnswer vehicle information to be associated with the dealer
   */
  public void updateDealer(final String datasetId, final Integer dealerId, final VehicleAnswer vehicleAnswer) {
    DealerAnswer dealer = new DealerAnswer();
    DealerAnswer result = dealerMap.putIfAbsent(dealerId, dealer);

    // The result is null only if the answer was not already there, so get the dealership information in this case
    if (result == null) {
      // Need to create a new API each time as they are not thread safe
      final DealersApi dealersApi = getDealersApi();
      try {
        // For each unique dealer, get its information
        DealersResponse dealersResponse = dealersApi.dealersGetDealer(datasetId, dealerId);
        if (dealersResponse == null) {
          throw new ApiException("Invalid response received from API to get the dealer.");
        }

        if (!dealerId.equals(dealersResponse.getDealerId())) {
          throw new ApiException("Wrong dealer in response. (expected, received): (" + dealerId + "," + dealersResponse.getDealerId() + ")");
        }

        // Update the dealer information with the dealer id and name
        dealer.setDealerId(dealerId);
        dealer.setName(dealersResponse.getName());
      } catch (ApiException e) {
        throw new RuntimeException("Unable to retrieve dealer information for (dataset, dealer): (" + datasetId + "," + dealerId + ")", e);
      }
    } else {
      // The dealer was already in the map, so use that one as the dealer
      dealer = result;
    }

    // Need to make sure each vehicle is added in a thread safe way
    synchronized (DealerAnswer.class) {
      dealer.addVehiclesItem(vehicleAnswer);
    }
  }

  public List<DealerAnswer> getDealerAnswers() {
    return new ArrayList<>(dealerMap.values());
  }

  protected DealersApi getDealersApi() {
    return new DealersApi();
  }
}