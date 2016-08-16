package bh.toolkit.srcgen.registry;

public class MapperSql {

	public static final int JOIN_TYPE_PUBLIC_INNER = 1;
	public static final int JOIN_TYPE_PRIVATE_INNER = 2;
	public static final int JOIN_TYPE_OUTER = 3;
	public static final int JOIN_TYPE_NONE = 4;

	private int joinType = JOIN_TYPE_NONE;
	private StringBuffer selectClause = new StringBuffer();
	private StringBuffer selectPrefix = new StringBuffer(); // 标识当前查询语句是否有前缀
	private StringBuffer columnNameClause = new StringBuffer();
	private StringBuffer columnAliasClause = new StringBuffer();
	private StringBuffer fromClause = new StringBuffer();
	private StringBuffer joinClause = new StringBuffer();
	private StringBuffer primaryKeyClause = new StringBuffer();
	private StringBuffer whereClause = new StringBuffer();

	public StringBuffer getSelectClause() {
		return selectClause;
	}

	public void setSelectClause(StringBuffer selectClause) {
		this.selectClause = selectClause;
	}

	public StringBuffer getFromClause() {
		return fromClause;
	}

	public void setFromClause(StringBuffer fromClause) {
		this.fromClause = fromClause;
	}

	public StringBuffer getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(StringBuffer whereClause) {
		this.whereClause = whereClause;
	}

	public StringBuffer getColumnNameClause() {
		return columnNameClause;
	}

	public void setColumnNameClause(StringBuffer columnNameClause) {
		this.columnNameClause = columnNameClause;
	}

	public StringBuffer getColumnAliasClause() {
		return columnAliasClause;
	}

	public void setColumnAliasClause(StringBuffer columnAliasClause) {
		this.columnAliasClause = columnAliasClause;
	}

	public StringBuffer getJoinClause() {
		return joinClause;
	}

	public void setJoinClause(StringBuffer joinClause) {
		this.joinClause = joinClause;
	}

	public StringBuffer getPrimaryKeyClause() {
		return primaryKeyClause;
	}

	public void setPrimaryKeyClause(StringBuffer primaryKeyClause) {
		this.primaryKeyClause = primaryKeyClause;
	}

	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	public StringBuffer getSelectPrefix() {
		return selectPrefix;
	}

	public void setSelectPrefix(StringBuffer selectPrefix) {
		this.selectPrefix = selectPrefix;
	}

}
