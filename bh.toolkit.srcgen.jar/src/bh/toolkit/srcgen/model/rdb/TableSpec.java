package bh.toolkit.srcgen.model.rdb;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TableSpec {

	private String schema;
	private String id;
	private String shortName;
	private String fullName;
	private String comments;
	private List<ColumnSpec> columnList;
	private Hashtable<String, ColumnSpec> columnIdx;

	public TableSpec() {
		columnList = new ArrayList<ColumnSpec>();
		columnIdx = new Hashtable<String, ColumnSpec>();
	}

	public void addColumn(ColumnSpec column) {
		columnList.add(column);
		columnIdx.put(column.getName(), column);
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<ColumnSpec> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnSpec> columnList) {
		this.columnList = columnList;
	}

	public Hashtable<String, ColumnSpec> getColumnIdx() {
		return columnIdx;
	}

	public void setColumnIdx(Hashtable<String, ColumnSpec> columnIdx) {
		this.columnIdx = columnIdx;
	}

}
