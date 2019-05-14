package com.cox.automotive;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This is an integration test to test the whole application.
 *
 * @author jeff.snyder
 */
class ChallengeIntegrationTest {

  private static Long MAX_MILLISECONDS_EXPECTED = 8000L;

  @Test
  void mainRunWithAPI() {
    PrintStream saveOut = System.out;

    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setOut(new PrintStream(out));

      Challenge.main(new String[]{});
    } finally {
      System.setOut(saveOut);
    }

    String [] result = out.toString().split("\r\n");
    System.out.println(result[0]);
    System.out.println(result[1]);
    assertEquals(2, result.length, "Result did not contain enough data for a successful result.");
    assertEquals("Congratulations.", result[0], "Result indicates that it did not pass.");
    try {
      Long millisecond = Long.parseLong(result[1]);
      assertTrue(millisecond < MAX_MILLISECONDS_EXPECTED, "Test took too long: " + millisecond + " milliseconds");
    } catch (Exception e) {
      fail("Number of milliseconds was not returned.");
    }
  }
}