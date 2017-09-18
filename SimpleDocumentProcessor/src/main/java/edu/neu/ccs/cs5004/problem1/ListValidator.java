package edu.neu.ccs.cs5004.problem1;

import java.util.ArrayList;
import java.util.List;

/**
 * A compliance method
 * Created by toddl on 4/2/2017.
 */
public class ListValidator {
  private List<ListTypes> listTypeMap;

  protected enum ListTypes {
    ITEMIZATION, ENUMERATION;
  }

  /**
   * Creates a new list validator.
   */
  protected ListValidator() {
    listTypeMap = new ArrayList<ListTypes>();
  }

  /**
   * A constructor used for testing purposes only.
   *
   * @param listTypeMap The list used for document list type validation given a nesting level.
   */
  public ListValidator(List<ListTypes> listTypeMap) {
    this.listTypeMap = listTypeMap;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    ListValidator that = (ListValidator) object;

    return listTypeMap.equals(that.listTypeMap);
  }

  @Override
  public int hashCode() {
    return listTypeMap.hashCode();
  }

  @Override
  public String toString() {
    return "ListValidator{listTypeMap=" + listTypeMap + '}';
  }

  /**
   * Validates that a new list line complies with the nesting rules of mixed list types.
   *
   * @param newLineType  The type of list line being validated.
   * @param nestingLevel The nesting level of this list line in the document list.
   *
   * @return True if the new line complies with precedent of existing list lines and false
   *     otherwise.
   */
  protected Boolean validate(ListTypes newLineType, Integer nestingLevel) {
    if (nestingLevel > listTypeMap.size()) {
      for (int i = nestingLevel - listTypeMap.size(); i > 0; i--) {
        listTypeMap.add(null);
      }
    }
    ListTypes existing = listTypeMap.get(nestingLevel - 1);
    if (existing == null) {
      listTypeMap.set(nestingLevel - 1, newLineType);
      return true;
    } else {
      return existing.equals(newLineType);
    }
  }


}
