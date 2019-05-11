package com.cox.automotive;

import io.swagger.client.ApiException;
import io.swagger.client.api.DealersApi;
import io.swagger.client.model.DealersResponse;
import io.swagger.client.model.VehicleAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Test the dealer updates and answers
 *
 * @author jeff.snyder
 */
class DealersTest {

  @Spy
  private Dealers dealers = new Dealers();

  @Mock
  private DealersApi dealersApi;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void updateDealerInitial_apiException() throws Exception {
    final String datasetId = "AAA";
    final Integer dealerId = 1;
    final VehicleAnswer vehicleAnswer = new VehicleAnswer();

    doReturn(dealersApi).when(dealers).getDealersApi();
    when(dealersApi.dealersGetDealer(datasetId, dealerId)).thenThrow(new ApiException());

    assertThrows(RuntimeException.class, () -> dealers.updateDealer(datasetId, dealerId, vehicleAnswer));
  }

  @Test
  public void updateDealerInitial_nullResponse() throws Exception {
    final String datasetId = "AAA";
    final Integer dealerId = 1;
    final VehicleAnswer vehicleAnswer = new VehicleAnswer();

    doReturn(dealersApi).when(dealers).getDealersApi();
    when(dealersApi.dealersGetDealer(datasetId, dealerId)).thenReturn(null);

    assertThrows(RuntimeException.class, () -> dealers.updateDealer(datasetId, dealerId, vehicleAnswer));
  }

  @Test
  public void updateDealerInitial_invalidResponse() throws Exception {
    final String datasetId = "AAA";
    final Integer dealerId = 1;
    final VehicleAnswer vehicleAnswer = new VehicleAnswer();
    vehicleAnswer.setYear(1989);
    vehicleAnswer.setVehicleId(1);
    vehicleAnswer.setModel("Grand Am");
    vehicleAnswer.setMake("Pontiac");

    DealersResponse dealersResponse = new DealersResponse();
    dealersResponse.setDealerId(dealerId + 1);
    dealersResponse.setName("Joes Car Palace");

    doReturn(dealersApi).when(dealers).getDealersApi();
    when(dealersApi.dealersGetDealer(datasetId, dealerId)).thenReturn(dealersResponse);

    assertThrows(RuntimeException.class, () -> dealers.updateDealer(datasetId, dealerId, vehicleAnswer));
  }

  @Test
  public void updateDealerInitial_validResponse() throws Exception {
    final String datasetId = "AAA";
    final Integer dealerId = 1;
    final VehicleAnswer vehicleAnswer = new VehicleAnswer();
    vehicleAnswer.setYear(1989);
    vehicleAnswer.setVehicleId(1);
    vehicleAnswer.setModel("Grand Am");
    vehicleAnswer.setMake("Pontiac");

    DealersResponse dealersResponse = new DealersResponse();
    dealersResponse.setDealerId(dealerId);
    dealersResponse.setName("Joes Car Palace");

    doReturn(dealersApi).when(dealers).getDealersApi();
    when(dealersApi.dealersGetDealer(datasetId, dealerId)).thenReturn(dealersResponse);

    dealers.updateDealer(datasetId, dealerId, vehicleAnswer);

    assertEquals(1, dealers.getDealerAnswers().size());
  }

  @Test
  public void updateDealerSameDealer_validResponses() throws Exception {
    final String datasetId = "AAA";
    final Integer dealerId = 1;

    VehicleAnswer vehicleAnswer = new VehicleAnswer();
    vehicleAnswer.setYear(1989);
    vehicleAnswer.setVehicleId(1);
    vehicleAnswer.setModel("Grand Am");
    vehicleAnswer.setMake("Pontiac");

    DealersResponse dealersResponse = new DealersResponse();
    dealersResponse.setDealerId(dealerId);
    dealersResponse.setName("Joes Car Palace");

    doReturn(dealersApi).when(dealers).getDealersApi();
    when(dealersApi.dealersGetDealer(datasetId, dealerId)).thenReturn(dealersResponse);

    dealers.updateDealer(datasetId, dealerId, vehicleAnswer);

    vehicleAnswer = new VehicleAnswer();
    vehicleAnswer.setYear(2000);
    vehicleAnswer.setVehicleId(2);
    vehicleAnswer.setModel("Sunbird");
    vehicleAnswer.setMake("Pontiac");

    dealers.updateDealer(datasetId, dealerId, vehicleAnswer);

    assertEquals(1, dealers.getDealerAnswers().size());
  }

  @Test
  public void updateDealerDifferentDealers_validResponses() throws Exception {
    final String datasetId = "AAA";
    final Integer dealerId = 1;

    VehicleAnswer vehicleAnswer = new VehicleAnswer();
    vehicleAnswer.setYear(1989);
    vehicleAnswer.setVehicleId(1);
    vehicleAnswer.setModel("Grand Am");
    vehicleAnswer.setMake("Pontiac");

    DealersResponse dealersResponse = new DealersResponse();
    dealersResponse.setDealerId(dealerId);
    dealersResponse.setName("Joes Car Palace");

    doReturn(dealersApi).when(dealers).getDealersApi();
    when(dealersApi.dealersGetDealer(datasetId, dealerId)).thenReturn(dealersResponse);

    dealers.updateDealer(datasetId, dealerId, vehicleAnswer);

    vehicleAnswer = new VehicleAnswer();
    vehicleAnswer.setYear(2000);
    vehicleAnswer.setVehicleId(2);
    vehicleAnswer.setModel("Sunbird");
    vehicleAnswer.setMake("Pontiac");

    Integer newDealerId = dealerId + 1;
    DealersResponse dealersResponse2 = new DealersResponse();
    dealersResponse2.setDealerId(newDealerId);
    dealersResponse2.setName("King Pontiac");

    when(dealersApi.dealersGetDealer(datasetId, newDealerId)).thenReturn(dealersResponse2);
    dealers.updateDealer(datasetId, newDealerId, vehicleAnswer);

    assertEquals(2, dealers.getDealerAnswers().size());
  }
}