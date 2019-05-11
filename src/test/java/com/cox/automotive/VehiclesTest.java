package com.cox.automotive;

import io.swagger.client.ApiException;
import io.swagger.client.api.VehiclesApi;
import io.swagger.client.model.VehicleAnswer;
import io.swagger.client.model.VehicleIdsResponse;
import io.swagger.client.model.VehicleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * This is to test vehicle processing.
 *
 * @author jeff.snyder
 */
class VehiclesTest {

  @Spy
  private Vehicles vehicles = new Vehicles();

  @Mock
  private VehiclesApi vehiclesApi;

  @Mock
  private Dealers dealers;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void getVehicleIds_apiException() throws Exception {
    final String datasetId = "AAA";

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    when(vehiclesApi.vehiclesGetIds(datasetId)).thenThrow(new ApiException());

    assertThrows(RuntimeException.class, () -> vehicles.getVehicleIds(datasetId));
  }

  @Test
  void getVehicleIds_nullResponse() throws Exception {
    final String datasetId = "AAA";

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    when(vehiclesApi.vehiclesGetIds(datasetId)).thenReturn(null);

    assertThrows(RuntimeException.class, () -> vehicles.getVehicleIds(datasetId));
  }

  @Test
  void getVehicleIds_validResponse() throws Exception {
    final String datasetId = "AAA";
    List<Integer> vehicleIds = Arrays.asList(1, 2, 3, 4);

    VehicleIdsResponse vehicleIdsResponse = new VehicleIdsResponse();
    vehicleIdsResponse.setVehicleIds(vehicleIds);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    when(vehiclesApi.vehiclesGetIds(datasetId)).thenReturn(vehicleIdsResponse);

    vehicles.getVehicleIds(datasetId);

    assertEquals(4, vehicles.getVehicleIds(datasetId).size());
  }

  @Test
  void getVehicleIds_validResponse_removeDuplicates() throws Exception {
    final String datasetId = "AAA";
    List<Integer> vehicleIds = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

    VehicleIdsResponse vehicleIdsResponse = new VehicleIdsResponse();
    vehicleIdsResponse.setVehicleIds(vehicleIds);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    when(vehiclesApi.vehiclesGetIds(datasetId)).thenReturn(vehicleIdsResponse);

    vehicles.getVehicleIds(datasetId);

    assertEquals(4, vehicles.getVehicleIds(datasetId).size());
  }

  @Test
  void getVehicleIds_validResponse_removeNulls() throws Exception {
    final String datasetId = "AAA";
    List<Integer> vehicleIds = Arrays.asList(1, null, 3, 4, null, 6);

    VehicleIdsResponse vehicleIdsResponse = new VehicleIdsResponse();
    vehicleIdsResponse.setVehicleIds(vehicleIds);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    when(vehiclesApi.vehiclesGetIds(datasetId)).thenReturn(vehicleIdsResponse);

    vehicles.getVehicleIds(datasetId);

    assertEquals(4, vehicles.getVehicleIds(datasetId).size());
  }

  @Test
  void getDealersWithTheirVehicles_apiException() throws Exception {
    final String datasetId = "AAA";
    final Integer vehicleId = 1;
    List<Integer> vehicleIds = Collections.singletonList(vehicleId);

    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setMake("Pontiac");
    vehicleResponse.setModel("Grand Am");
    vehicleResponse.setYear(1989);
    vehicleResponse.setVehicleId(vehicleId);
    vehicleResponse.setDealerId(1);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    doReturn(dealers).when(vehicles).getDealers();

    when(vehiclesApi.vehiclesGetVehicle(datasetId, vehicleId)).thenThrow(new ApiException());

    assertThrows(RuntimeException.class, () -> vehicles.getDealersWithTheirVehicles(datasetId, vehicleIds));
  }

  @Test
  void getDealersWithTheirVehicles_nullResponse() throws Exception {
    final String datasetId = "AAA";
    final Integer vehicleId = 1;
    List<Integer> vehicleIds = Collections.singletonList(vehicleId);

    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setMake("Pontiac");
    vehicleResponse.setModel("Grand Am");
    vehicleResponse.setYear(1989);
    vehicleResponse.setVehicleId(vehicleId);
    vehicleResponse.setDealerId(1);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    doReturn(dealers).when(vehicles).getDealers();

    when(vehiclesApi.vehiclesGetVehicle(datasetId, vehicleId)).thenReturn(null);

    assertThrows(RuntimeException.class, () -> vehicles.getDealersWithTheirVehicles(datasetId, vehicleIds));
  }

  @Test
  void getDealersWithTheirVehicles_invalidResponse() throws Exception {
    final String datasetId = "AAA";
    final Integer vehicleId = 1;
    List<Integer> vehicleIds = Collections.singletonList(vehicleId);

    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setMake("Pontiac");
    vehicleResponse.setModel("Grand Am");
    vehicleResponse.setYear(1989);
    vehicleResponse.setVehicleId(vehicleId + 1);
    vehicleResponse.setDealerId(1);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    doReturn(dealers).when(vehicles).getDealers();

    when(vehiclesApi.vehiclesGetVehicle(datasetId, vehicleId)).thenReturn(null);

    assertThrows(RuntimeException.class, () -> vehicles.getDealersWithTheirVehicles(datasetId, vehicleIds));
  }

  @Test
  void getDealersWithTheirVehicles_validResponse() throws Exception {
    final String datasetId = "AAA";
    final Integer vehicleId = 1;
    final Integer dealerId = 2;
    List<Integer> vehicleIds = Collections.singletonList(vehicleId);

    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setMake("Pontiac");
    vehicleResponse.setModel("Grand Am");
    vehicleResponse.setYear(1989);
    vehicleResponse.setVehicleId(vehicleId);
    vehicleResponse.setDealerId(dealerId);

    doReturn(vehiclesApi).when(vehicles).getVehiclesApi();
    doReturn(dealers).when(vehicles).getDealers();

    when(vehiclesApi.vehiclesGetVehicle(datasetId, vehicleId)).thenReturn(vehicleResponse);

    ArgumentCaptor<VehicleAnswer> vehicleAnswerArgumentCaptor = ArgumentCaptor.forClass(VehicleAnswer.class);
    doNothing().when(dealers).updateDealer(anyString(), anyInt(), vehicleAnswerArgumentCaptor.capture());

    vehicles.getDealersWithTheirVehicles(datasetId, vehicleIds);

    List<VehicleAnswer> answers = vehicleAnswerArgumentCaptor.getAllValues();
    assertEquals(1, answers.size());
    VehicleAnswer answer = answers.get(0);
    assertEquals(vehicleResponse.getVehicleId(), answer.getVehicleId());
    assertEquals(vehicleResponse.getMake(), answer.getMake());
    assertEquals(vehicleResponse.getModel(), answer.getModel());
    assertEquals(vehicleResponse.getYear(), answer.getYear());
  }
}