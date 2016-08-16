package bh.toolkit.srcgen.model.rdb;

public class ColumnSpec {

	private String id; // 唯一标识一个二维关系数据库的列，可以采用自定义字符串拼接
	private String name; // 字段名
	private String comments; // 备注
	private boolean isPrimaryKey; // 是否为主键
	private String defaultValue; // 默认值
	private String jdbcTypeName; // JDBC数据类型名
	private int jdbcType; // JDBC数据类型，与java.sql.JDBCType的定义一致

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}

	/**
	 * @return the jdbcTypeName
	 */
	public String getJdbcTypeName() {
		return jdbcTypeName;
	}

	/**
	 * @param jdbcTypeName
	 *            the jdbcTypeName to set
	 */
	public void setJdbcTypeName(String jdbcTypeName) {
		this.jdbcTypeName = jdbcTypeName;
	}

}
