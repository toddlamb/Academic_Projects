package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to read in serialized java data.
 *
 * @param <t> Type of Data
 */
public class SerializedDataManager<t> {

  private List<t> dataList;

  /**
   * @param file File location of serialized data
   * @throws IOException Read/Write errors.
   * @throws ClassNotFoundException If class supplied to de-serialize is incorrect.
   */
  public SerializedDataManager(File file) throws IOException, ClassNotFoundException {
    this.dataList = new ArrayList<>();
    if (file.isFile() && file.canRead()) {
      ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
      this.dataList = (ArrayList<t>) objectInputStream.readObject();
    }
  }

  public List<t> getDataList() {
    return dataList;
  }

  /**
   * Splits a datalist into list of lists
   *
   * @param list_count The number of lists in result list
   * @return A list of lists containing original list data.
   */
  public List<List<t>> split_list(int list_count) {
    int partition_size = this.dataList.size() / list_count;
    List<List<t>> result = new ArrayList<>(list_count);
    for (int i = 0; i < list_count; i++) {
      List<t> sub_list = subList(i, i + partition_size);
      result.add(sub_list);
    }
    if (this.dataList.size() % list_count != 0) {
      List<t> remainderList = subList((partition_size * list_count) - 1, this.dataList.size() - 1);
      result.get(result.size() - 1).addAll(remainderList);
    }
    return result;
  }


  /**
   * Creates a sublist containing elements from datalist at start through datalist at end.
   *
   * @param start The starting index for the original list to begin generating the sublist
   * @param end The ending index for the original list to begin generating the sublist
   * @return a sublist containing elements from datalist at start through datalist at end.
   */
  private List<t> subList(int start, int end) {
    List<t> result = new ArrayList<>(end - start);
    for (int i = start; i < end; i++) {
      result.add(this.dataList.get(i));
    }
    return result;
  }

}
