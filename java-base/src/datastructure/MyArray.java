package datastructure;

/**
 * @author: wensw
 * @description: 自定义数组实现
 * 1、数据存储：正常存储数据、去重存储数据，并返回已存在值对应下标
 * 2、数据获取：按下标获取数据、按数据获取下标
 * 3、数据移除：按值删除、按下标删除
 * 4、数据展示：打印数据信息
 */
public class MyArray {

    private int size;

    private Object[] element;

    public MyArray() {
        this.element = new Object[10];
    }

    public void add(Object value) {
        if (size == 0) {
            element[0] = value;
        } else {
            element[++size] = value;
        }
    }

    public int addDistinctSorted(Object value) {
        int i = -1;
        if (size == 0) {
            element[0] = value;
        } else {
            for (i = 0; i < size; i++) {
                if (element[i] == value || element[i].equals(value)) {
                    break;
                }
            }
        }
        return i;
    }


}
