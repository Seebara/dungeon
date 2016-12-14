package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.Name;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * A collection of Selectable objects that match a given query.
 */
public class Matches<T extends Selectable> {

  private final List<T> matches = new ArrayList<>();
  private final boolean disjoint;
  private int differentNames = 0;
  private boolean differentNamesUpToDate = true;

  private Matches(boolean disjoint) {
    this.disjoint = disjoint;
  }

  /**
   * Converts a Collection to Matches.
   */
  public static <T extends Selectable> Matches<T> fromCollection(Collection<T> collection) {
    return fromCollection(collection, true);
  }

  /**
   * Converts a Collection to possibly not disjoint Matches.
   */
  static <T extends Selectable> Matches<T> fromCollection(Collection<T> collection, boolean disjoint) {
    Matches<T> newInstance = new Matches<>(disjoint);
    for (T t : collection) {
      newInstance.addMatch(t);
    }
    return newInstance;
  }

  /**
   * Returns whether or not the Matches are disjoint.
   *
   * <p>If they are, any element constitutes a match, otherwise, only all elements together constitute a match.
   */
  public boolean isDisjoint() {
    return disjoint;
  }

  private void addMatch(T match) {
    matches.add(match);
    differentNamesUpToDate = false;
  }

  public T getMatch(int index) {
    return matches.get(index);
  }

  public List<T> toList() {
    return new ArrayList<>(matches);
  }

  /**
   * Returns true if there is a match with the given name, false otherwise.
   *
   * @param name the name used for comparison
   * @return true if there is a match with the given name, false otherwise
   */
  public boolean hasMatchWithName(Name name) {
    for (T match : matches) {
      if (match.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the number of matches.
   */
  public int size() {
    return matches.size();
  }

  /**
   * Returns how many different names the matches have. For instance, if the matches consist of two Entity objects with
   * identical names, this method will return 1.
   *
   * <p>This method will calculate how many different names are in the list of matches or use the last calculated value,
   * if the matches list did not change since the last calculation. Therefore, after adding all matches and calling this
   * method once, subsequent method calls should be substantially faster.
   */
  public int getDifferentNames() {
    if (!differentNamesUpToDate) {
      updateDifferentNamesCount();
    }
    return differentNames;
  }

  /**
   * Updates the differentNames variable after iterating over the list of matches.
   */
  private void updateDifferentNamesCount() {
    HashSet<Name> uniqueNames = new HashSet<>();
    for (T match : matches) {
      uniqueNames.add(match.getName());
    }
    differentNames = uniqueNames.size();
    differentNamesUpToDate = true;
  }

}
