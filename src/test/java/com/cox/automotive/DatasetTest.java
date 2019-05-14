package com.cox.automotive;

import io.swagger.client.ApiException;
import io.swagger.client.api.DataSetApi;
import io.swagger.client.model.AnswerResponse;
import io.swagger.client.model.DatasetIdResponse;
import io.swagger.client.model.DealerAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * This is to test dataset processing.
 *
 * @author jeff.snyder
 */class DatasetTest {

  @Spy
  private Dataset dataset = new Dataset();

  @Mock
  private DataSetApi dataSetApi;

  @BeforeEach
  void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void getDatasetId_apiException() throws Exception {
    doReturn(dataSetApi).when(dataset).getDataSetApi();
    when(dataSetApi.dataSetGetDataSetId()).thenThrow(new ApiException());

    assertThrows(RuntimeException.class, () -> dataset.getDatasetId());
  }

  @Test
  void getDatasetId_nullResponse() throws Exception {
    doReturn(dataSetApi).when(dataset).getDataSetApi();
    when(dataSetApi.dataSetGetDataSetId()).thenReturn(null);

    assertThrows(RuntimeException.class, () -> dataset.getDatasetId());
  }

  @Test
  void getDatasetId_invalidResponse() throws Exception {

    DatasetIdResponse response = new DatasetIdResponse();
    response.setDatasetId(null);

    doReturn(dataSetApi).when(dataset).getDataSetApi();
    when(dataSetApi.dataSetGetDataSetId()).thenReturn(response);

    assertThrows(RuntimeException.class, () -> dataset.getDatasetId());
  }

  @Test
  void getDatasetId_validResponse() throws Exception {

    String datasetId = "AAA";
    DatasetIdResponse response = new DatasetIdResponse();
    response.setDatasetId(datasetId);

    doReturn(dataSetApi).when(dataset).getDataSetApi();
    when(dataSetApi.dataSetGetDataSetId()).thenReturn(response);

    assertEquals(datasetId, dataset.getDatasetId());
  }

  @Test
  void submitAnswer_apiException() throws Exception {

    String datasetId = "AAA";
    List<DealerAnswer> dealerAnswers = new ArrayList<>();
    DealerAnswer dealerAnswer = new DealerAnswer();
    dealerAnswers.add(dealerAnswer);

    doReturn(dataSetApi).when(dataset).getDataSetApi();
    when(dataSetApi.dataSetPostAnswer(anyString(), any())).thenThrow(new ApiException());

    assertThrows(RuntimeException.class, () -> dataset.submitAnswer(datasetId, dealerAnswers));
  }

  @Test
  void submitAnswer_validResponse() throws Exception {

    String datasetId = "AAA";
    List<DealerAnswer> dealerAnswers = new ArrayList<>();
    DealerAnswer dealerAnswer = new DealerAnswer();
    dealerAnswers.add(dealerAnswer);

    AnswerResponse answerResponse = new AnswerResponse();
    answerResponse.setSuccess(true);
    answerResponse.setTotalMilliseconds(1111);
    answerResponse.setMessage("Congratulations.");

    doReturn(dataSetApi).when(dataset).getDataSetApi();
    when(dataSetApi.dataSetPostAnswer(anyString(), any())).thenReturn(answerResponse);

    dataset.submitAnswer(datasetId, dealerAnswers);
  }
}