package com.cox.automotive;

import io.swagger.client.ApiException;
import io.swagger.client.api.DataSetApi;
import io.swagger.client.model.Answer;
import io.swagger.client.model.AnswerResponse;
import io.swagger.client.model.DatasetIdResponse;
import io.swagger.client.model.DealerAnswer;

import java.util.List;

/**
 * Dataset retrieval and answering
 *
 * @author jeff.snyder
 */
public class Dataset {

  /**
   * @return get the dataset id for the current run
   */
  public String getDatasetId() {

    final DataSetApi dataSetApi = getDataSetApi();
    try {
      // Get the dataset id
      DatasetIdResponse response = dataSetApi.dataSetGetDataSetId();
      if (response == null) {
        throw new ApiException("Invalid response received from API to get the dataset id.");
      }
      String id = response.getDatasetId();
      if ((id == null) || id.isEmpty()) {
        throw new ApiException("Empty dataset id returned.");
      }
      return id;
    } catch (ApiException e) {
      throw new RuntimeException("Unable to retrieve the dataset id.", e);
    }
  }

  /**
   * Submit the answer for the final check
   *
   * @param datasetId dataset being processed
   * @param dealers answer containing the dealers and their associated vehicles
   */
  public void submitAnswer(final String datasetId, final List<DealerAnswer> dealers) {
    // Compile answer and return it
    Answer answer = new Answer();
    answer.setDealers(dealers);

    final DataSetApi dataSetApi = getDataSetApi();
    try {
      AnswerResponse answerResponse = dataSetApi.dataSetPostAnswer(datasetId, answer);
      System.out.println(answerResponse.getMessage());
      System.out.println(answerResponse.getTotalMilliseconds());
    } catch (ApiException e) {
      throw new RuntimeException("Unable to set the answer for the dataset: " + datasetId);
    }
  }

  protected DataSetApi getDataSetApi() {
    return new DataSetApi();
  }
}