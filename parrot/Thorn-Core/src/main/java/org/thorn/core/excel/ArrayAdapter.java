package org.thorn.core.excel;

import java.util.List;

import org.thorn.core.util.LocalStringUtils;

/**
 * @ClassName: ArrayAdapter
 * @Description:
 * @author chenyun
 * @date 2012-5-2 下午03:22:03
 */
public class ArrayAdapter {

	/** 文件表头 */
	private List<Object[]> title;

	/** 合并项数组 */
	private List<Integer[]> mergeCell;

	/** 中文头 */
	private String[] header;

	/** 与中文头对应的英文属性 */
	private String[] orderArray;

	/** 将对象转换为的对象数组 */
	private List<Object[]> dataSource;

	private int[] orderMapping;
	
	public ArrayAdapter(String[] header, String[] orderArray,
			List<Object[]> dataSource, List<Object[]> title,
			List<Integer[]> mergeCell) {
		this(header, orderArray, dataSource);
		this.title = title;
		this.mergeCell = mergeCell;
	}
	
	public ArrayAdapter(String[] header, String[] orderArray,
			List<Object[]> dataSource) {

		this.header = header;
		this.orderArray = orderArray;
		this.dataSource = dataSource;

		if (this.header == null || this.header.length == 0) {
			this.header = this.orderArray;
		}
		initOrderMapping();
	}

	public ArrayAdapter(String[][] array, List<Object[]> dataSource) {

		this.header = array[0];
		this.orderArray = array[1];
		this.dataSource = dataSource;

		if (this.header == null || this.header.length == 0) {
			this.header = this.orderArray;
		}
		initOrderMapping();
	}

	private void initOrderMapping() {
		// dataSource第一行为属性
		String[] sourceOrder = (String[]) dataSource.get(0);
		orderMapping = new int[sourceOrder.length];

		for (int i = 0; i < orderArray.length; i++) {

			for (int j = 0; j < sourceOrder.length; j++) {

				if (LocalStringUtils.equals(orderArray[i], sourceOrder[j])) {
					orderMapping[i] = j;
					break;
				}
			}
		}
	}

	public String[] getHeader() {
		return this.header;
	}
	
	
	public List<Object[]> getTitle() {
		return title;
	}

	public List<Integer[]> getMergeCell() {
		return mergeCell;
	}

	public int getDataSourceOfSize() {
		int size = this.dataSource.size();
		// remove the list header
		if (size > 1) {
			size--;
		}

		return size;
	}

	public Object[] getRow(int rowNum) {
		Object[] obj = dataSource.get(rowNum + 1);

		Object[] orderObj = new Object[obj.length];

		for (int i = 0; i < orderObj.length; i++) {
			orderObj[i] = obj[orderMapping[i]];
		}

		return orderObj;
	}

	public Object getObject(int rowNum, int columnNum) {
		Object[] obj = dataSource.get(rowNum + 1);

		return obj[orderMapping[columnNum]];
	}

}
