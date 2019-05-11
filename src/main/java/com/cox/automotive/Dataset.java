package com.cox.automotive;

import com.sun.istack.internal.NotNull;
import io.swagger.client.ApiException;
import io.swagger.client.api.DataSetApi;
import io.swagger.client.model.Answer;
import io.swagger.client.model.AnswerResponse;
import io.swagger.client.model.DatasetIdResponse;
import io.swagger.client.model.DealerAnswer;

import java.util.List;

/**
 * com.cox.automotive.Dataset retrieval and answering
 */
public class Dataset {

  /**
   * @return get the dataset id for the current run
   */
  @NotNull
  public String getDatasetId() {

    final DataSetApi dataApi = new DataSetApi();
    try {
      // Get the dataset id
      DatasetIdResponse response = dataApi.dataSetGetDataSetId();
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

  public void submitAnswer(String datasetId, List<DealerAnswer> dealers) {
    // Compile answer and return it
    Answer answer = new Answer();
    answer.setDealers(dealers);

    final DataSetApi dataApi = new DataSetApi();
    try {
      AnswerResponse answerResponse = dataApi.dataSetPostAnswer(datasetId, answer);
      System.out.println(answerResponse.getMessage());
      System.out.println(answerResponse.getTotalMilliseconds());
    } catch (ApiException e) {
      throw new RuntimeException("Unable to set the answer for the dataset: " + datasetId);
    }
  }
}