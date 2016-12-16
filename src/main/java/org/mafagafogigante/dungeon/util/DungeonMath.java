package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * A collection of mathematical utility methods.
 */
public final class DungeonMath {

  private static final int SECOND_IN_NANOSECONDS = 1000000000;
  private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-8;
  private static final String TIMEOUT = "TIMEOUT";

  private DungeonMath() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Evaluates the weighted average of two values.
   *
   * @param first the first value
   * @param second the second value
   * @param firstContribution how much the second value contributes to the average
   * @return the weighted average between the two values
   */
  public static double weightedAverage(double first, double second, Percentage firstContribution) {
    return first + (second - first) * firstContribution.toDouble();
  }

  /**
   * Compares two doubles with the default tolerance margin.
   */
  public static int fuzzyCompare(double first, double second) {
    return fuzzyCompare(first, second, DEFAULT_DOUBLE_TOLERANCE);
  }

  /**
   * Compares two doubles with a specified tolerance margin.
   */
  private static int fuzzyCompare(double first, double second, double epsilon) {
    if (first + epsilon < second) {
      return -1;
    } else if (first - epsilon > second) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Parses the "fibonacci" command.
   */
  public static void parseFibonacci(String[] arguments) {
    if (arguments.length != 0) {
      int number;
      String argument = arguments[0];
      try {
        number = Integer.parseInt(argument);
      } catch (NumberFormatException warn) {
        Messenger.printInvalidNumberFormatOrValue();
        return;
      }
      if (number < 1) {
        Messenger.printInvalidNumberFormatOrValue();
        return;
      }
      String result = fibonacci(number);
      if (result.equals(TIMEOUT)) {
        Writer.write("Calculation exceeded the time limit.");
      } else {
        Writer.write(functionEvaluationString("fibonacci", String.valueOf(number), fibonacci(number)));
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  /**
   * Finds the n-th element of the fibonacci sequence if it can be computed in less than one second.
   *
   * @param number the position of the element on the sequence
   * @return a String representation of the number or the {@code TIMEOUT} constant
   */
  private static String fibonacci(int number) {
    // Allow this method to run for one second.
    final long interruptTime = System.nanoTime() + SECOND_IN_NANOSECONDS;
    BigInteger first = BigInteger.ZERO;
    BigInteger second = BigInteger.ONE;
    // Swap variable.
    BigInteger swap;
    for (int i = 1; i < number; i++) {
      swap = first;
      first = second;
      second = second.add(swap);
      if (System.nanoTime() >= interruptTime) {
        return TIMEOUT;
      }
    }
    return first.toString();
  }

  /**
   * Makes a pretty String representation of a function evaluation.
   *
   * <p>Example: {@code functionName(argument) = result}
   *
   *
   * <p>If the String exceeds the maximum number of columns, a backslash is used to break lines.
   *
   * @param functionName the name of the function
   * @param argument the argument passed to the function
   * @param result the result of the evaluation
   * @return a String longer than the three provided Strings combined
   */
  private static String functionEvaluationString(String functionName, String argument, String result) {
    String original = String.format("%s(%s) = %s", functionName, argument, result);
    return insertBreaksAtTheColumnLimit(original);
  }

  /**
   * Inserts line breaks (a backslash followed by a newline) at all the indices that are multiples of the column count.
   *
   * @param string the original String
   */
  private static String insertBreaksAtTheColumnLimit(String string) {
    if (string.length() <= GameWindow.COLS) {
      return string;
    }
    StringBuilder builder = new StringBuilder();
    int charactersOnThisLine = 0;
    for (char character : string.toCharArray()) {
      if (charactersOnThisLine == GameWindow.COLS) {
        builder.insert(builder.length() - 1, "\\\n");
        charactersOnThisLine = 1; // The last number "fell" to the newest line after the insertion.
      }
      builder.append(character);
      charactersOnThisLine++;
    }
    return builder.toString();
  }

  /**
   * Safely casts a long into an integer.
   *
   * @param value the long that will be converted, should be in the range [Integer.MIN_VALUE, Integer.MAX_VALUE]
   * @return an integer equal to the provided long
   * @throws IllegalArgumentException if the long does not fit into an integer
   */
  public static int safeCastLongToInteger(long value) {
    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(value + " does not fit into an integer.");
    } else {
      return (int) value;
    }
  }

  /**
   * Returns the sum of an array of integers.
   *
   * @param integers the array of integers, not null
   * @return the sum
   */
  public static int sum(@NotNull int[] integers) {
    int total = 0;
    for (int integer : integers) {
      total += integer;
    }
    return total;
  }

}
