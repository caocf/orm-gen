package bh.toolkit.srcgen.connector.rdb;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CaseFilterSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;
import bh.toolkit.srcgen.model.mybatis.*;
import bh.toolkit.srcgen.registry.MapperSql;
import bh.toolkit.srcgen.registry.OneToOneIdx;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class DB2PageQueryBuilder implements IPageQueryBuilder {

    @Override
    public void definePagingQuery(Select select, ArtifactSpec artifactSpec, SelectSpec selectSpec, List<OneToOneIdx> descOtoIdxList, MapperSql mapperSql, String sqlId,
                                  CaseFilterSpec topCaseFilterSpec, ObjectFactory mapperObjFactory) {

        // TODO
        // CtxSpec ctxSpec = artifactSpec.getCtxSpec();
        /*
		 * TODO if (!appendUserFilter) { userfilter = null; }
		 */
        // //////////
        // Append the case of "fetch first X rows".
        // //////////
        int joinType = mapperSql.getJoinType();
        Choose overallChoose = mapperObjFactory.createChoose();
        select.getChoose().add(overallChoose);
        When overallChooseWhen = mapperObjFactory.createWhen();
        overallChoose.getWhen().add(overallChooseWhen);
        overallChooseWhen.setTest(MapperElm.ATTR_START_ROW + JavaSrcElm.EXPR_INT_IS_INVALID + MapperElm.SQL_OR_FULL + MapperElm.ATTR_END_ROW
                + JavaSrcElm.EXPR_INT_IS_INVALID);

        // Refer to common select clause and from clause.
        Include selectFromInclude = mapperObjFactory.createInclude();
        selectFromInclude.setRefid(sqlId);
        overallChooseWhen.getInclude().add(selectFromInclude);

        // Append filter clause, the computed filter was embedded inside where
        // clause.
        StringBuffer filterClause = mapperSql.getWhereClause();
        Trim overallChooseWhenTrim = null;
        if (StringUtils.isNotBlank(filterClause)) {
            // Append the existing filter.
            overallChooseWhenTrim = mapperObjFactory.createTrim();
            overallChooseWhen.getTrim().add(overallChooseWhenTrim);
            overallChooseWhenTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + filterClause + MapperElm.CDATA_ANCHOR_END);

            // Append where clause and order by clause.
            overallChooseWhenTrim = mapperObjFactory.createTrim();
            overallChooseWhen.getTrim().add(overallChooseWhenTrim);
            If overallChooseWhenTrimIf = mapperObjFactory.createIf();
            overallChooseWhenTrim.getIf().add(overallChooseWhenTrimIf);
            overallChooseWhenTrimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);
            Trim overallChooseWhenTrimIfTrim = mapperObjFactory.createTrim();
            overallChooseWhenTrimIf.getTrim().add(overallChooseWhenTrimIfTrim);
            overallChooseWhenTrimIfTrim.setPrefix(MapperElm.SQL_AND_FULL);
            overallChooseWhenTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
            overallChooseWhenTrimIfTrim
                    .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

            // TODO 需要仿照MySQL的针对caseFilterSpec的处理
            // if (ctxSpec != null) {
            // TODO
            // overallChooseWhen.getChoose().add(UserFilterBulider.buildFilter(MapperElm.SQL_AND_FULL,
            // mapperObjFactory, userfilter));
            // }

        } else {
            // Append where clause.
            overallChooseWhenTrim = mapperObjFactory.createTrim();
            overallChooseWhen.getTrim().add(overallChooseWhenTrim);
            If overallChooseWhenTrimIf = mapperObjFactory.createIf();
            overallChooseWhenTrim.getIf().add(overallChooseWhenTrimIf);
            overallChooseWhenTrimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);
            Trim overallChooseWhenTrimIfTrim = mapperObjFactory.createTrim();
            overallChooseWhenTrimIf.getTrim().add(overallChooseWhenTrimIfTrim);
            // TODO Changed at 2015.05.27，需要密切观察是否会产生连带影响
            if (joinType == MapperSql.JOIN_TYPE_OUTER || joinType == MapperSql.JOIN_TYPE_NONE) {
                overallChooseWhenTrimIfTrim.setPrefix(MapperElm.SQL_WHERE_SIMPLE);
            } else {
                overallChooseWhenTrimIfTrim.setPrefix(MapperElm.SQL_AND_SIMPLE);
            }
            overallChooseWhenTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_OR);
            overallChooseWhenTrimIfTrim
                    .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

            // TODO 需要仿照MySQL的针对caseFilterSpec的处理
            // if (ctxSpec != null) {
			/*
			 * TODO if (overallChooseWhenTrimIfTrim.getPrefix().equals(MapperElm. SQL_AND_FULL)) { overallChooseWhenTrim.getChoose().add(UserFilterBulider. buildFilter(MapperElm.SQL_AND_FULL,
			 * mapperObjFactory, userfilter)); } else { overallChooseWhenTrimIfTrim.getChoose().add(UserFilterBulider .buildFilter(MapperElm.SQL_AND_FULL, mapperObjFactory, userfilter));
			 * 
			 * If noWhereIf = mapperObjFactory.createIf(); noWhereIf.setTest(MapperElm.ATTR__WHERE_CLAUSE + JavaSrcElm.EXPR_OBJ_IS_NULL); overallChooseWhenTrim.getIf().add(noWhereIf);
			 * noWhereIf.getChoose().add(UserFilterBulider.buildFilter( overallChooseWhenTrimIfTrim.getPrefix(), mapperObjFactory, userfilter)); }
			 */
            // }

        }

        // Get the default order by config.
        StringBuffer defaultOrderByStrBuf = new StringBuffer();
        String defaultOrderByStr = selectSpec.getDefaultOrderBy();
        if (StringUtils.isNotBlank(defaultOrderByStr) == true) {
            String[] defaultOrderByArray = defaultOrderByStr.split(MapperElm.COMMA);
            if (defaultOrderByArray != null && defaultOrderByArray.length != 0) {
                String tableName = selectSpec.getTableName();
                String tableAlias = selectSpec.getTableAlias();
                String columnName = null;
                String columnFullName = null;
                String orderByPhase = null;
                String orderByColumn = null;
                String theOrder = null;
                int idxOfWs = -1;
                for (int i = 0; i < defaultOrderByArray.length; i++) {
                    orderByPhase = defaultOrderByArray[i];
                    idxOfWs = orderByPhase.trim().indexOf(JavaSrcElm.WHITE_SPACE);
                    if (idxOfWs != -1) {
                        orderByColumn = orderByPhase.substring(0, idxOfWs);
                        theOrder = orderByPhase.substring(idxOfWs, orderByPhase.length()).toLowerCase();
                    } else {
                        orderByColumn = orderByPhase;
                        theOrder = "";
                    }
                    columnName = JavaFormatter.getDBStyle(orderByColumn);
                    columnFullName = MapperFormatter.getColumnFullName(artifactSpec, tableName, tableAlias, columnName);
                    if (i != 0) {
                        defaultOrderByStrBuf.append(MapperElm.COMMA);
                        defaultOrderByStrBuf.append(MapperElm.WHITE_SPACE);
                    }
                    defaultOrderByStrBuf.append(columnFullName + theOrder);
                }
            }
        }

        // Append the given order by clause or the default order by.
        Choose overallChooseWhenChoose = mapperObjFactory.createChoose();
        overallChooseWhen.getChoose().add(overallChooseWhenChoose);
        When overallChooseWhenChooseWhen = mapperObjFactory.createWhen();
        overallChooseWhenChoose.getWhen().add(overallChooseWhenChooseWhen);
        overallChooseWhenChooseWhen.setTest(MapperElm.EXPR_ORDERBY_CLAUSE_ISNOT_NULL);
        Trim overallChooseWhenChooseWhenTrim = mapperObjFactory.createTrim();
        overallChooseWhenChooseWhen.getTrim().add(overallChooseWhenChooseWhenTrim);
        overallChooseWhenChooseWhenTrim.setPrefix(MapperElm.SQL_ORDERBY_SIMPLE + MapperElm.WHITE_SPACE);
        overallChooseWhenChooseWhenTrim
                .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_ORDERBY_CLAUSE + MapperElm.CDATA_ANCHOR_END);
        Otherwise overallChooseWhenChooseOthereise = mapperObjFactory.createOtherwise();
        overallChooseWhenChoose.setOtherwise(overallChooseWhenChooseOthereise);
        Trim overallChooseWhenChooseOtherwiseTrim = mapperObjFactory.createTrim();
        overallChooseWhenChooseOthereise.getTrim().add(overallChooseWhenChooseOtherwiseTrim);
        overallChooseWhenChooseOtherwiseTrim.setPrefix(MapperElm.SQL_ORDERBY_SIMPLE + MapperElm.WHITE_SPACE);
        overallChooseWhenChooseOtherwiseTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + defaultOrderByStrBuf + MapperElm.CDATA_ANCHOR_END);
        overallChooseWhen.setCDataEnd(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.FETCH_FIRST_ROWS + MapperElm.CDATA_ANCHOR_END);

        // //////////
        // Append the case of "between X and Y".
        // //////////
        // Include column alias clause.
        Otherwise overallChooseOtherwise = mapperObjFactory.createOtherwise();
        overallChoose.setOtherwise(overallChooseOtherwise);
        Trim overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
        overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
        overallChooseOtherwiseTrim.setPrefix(MapperElm.SQL_SELECT + MapperElm.WHITE_SPACE);
        overallChooseOtherwiseTrim.setSuffix(MapperElm.COMMA + MapperElm.WHITE_SPACE + MapperElm.SQL_RN);
        Include columnAliasInclude = mapperObjFactory.createInclude();
        overallChooseOtherwiseTrim.getInclude().add(columnAliasInclude);
        columnAliasInclude.setRefid(sqlId + MapperElm.MAPPER_COL_ALIAS);

        // Include column name clause.
        overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
        overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
        overallChooseOtherwiseTrim.setPrefix(MapperElm.SQL_FROM_FULL + MapperElm.LEFT_PARENTHESIS + MapperElm.SQL_SELECT + MapperElm.WHITE_SPACE);
        overallChooseOtherwiseTrim.setSuffix(MapperElm.COMMA + MapperElm.WHITE_SPACE + MapperElm.SQL_ROW_NUMBER + MapperElm.WHITE_SPACE);
        Include columnNameInclude = mapperObjFactory.createInclude();
        overallChooseOtherwiseTrim.getInclude().add(columnNameInclude);
        columnNameInclude.setRefid(sqlId + MapperElm.MAPPER_COL_NM);

        // Include the given order by clause or the default order by.
        overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
        overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
        overallChooseOtherwiseTrim.setPrefix(
                MapperElm.SQL_OVER + MapperElm.WHITE_SPACE + MapperElm.LEFT_PARENTHESIS + MapperElm.SQL_ORDERBY_SIMPLE + MapperElm.WHITE_SPACE);
        // orderByTrim.setPrefixOverrides(MapperElm.WHITE_SPACE +
        // MapperElm.SQL_ORDERBY_SIMPLE + MapperElm.WHITE_SPACE);
        overallChooseOtherwiseTrim.setSuffix(MapperElm.RIGHT_PARENTHESIS + MapperElm.WHITE_SPACE + MapperElm.SQL_AS + MapperElm.WHITE_SPACE
                + MapperElm.SQL_RN + MapperElm.WHITE_SPACE);
        Choose overallChooseOtherwiseTrimChoose = mapperObjFactory.createChoose();
        overallChooseOtherwiseTrim.getChoose().add(overallChooseOtherwiseTrimChoose);
        When overallChooseOtherwiseTrimChooseWhen = mapperObjFactory.createWhen();
        overallChooseOtherwiseTrimChoose.getWhen().add(overallChooseOtherwiseTrimChooseWhen);
        overallChooseOtherwiseTrimChooseWhen.setTest(MapperElm.EXPR_ORDERBY_CLAUSE_ISNOT_NULL);
        overallChooseOtherwiseTrimChooseWhen
                .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_ORDERBY_CLAUSE + MapperElm.CDATA_ANCHOR_END);
        Otherwise overallChooseOtherwiseTrimChooseOtherwise = mapperObjFactory.createOtherwise();
        overallChooseOtherwiseTrimChoose.setOtherwise(overallChooseOtherwiseTrimChooseOtherwise);
        overallChooseOtherwiseTrimChooseOtherwise.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + defaultOrderByStrBuf + MapperElm.CDATA_ANCHOR_END);

        // Include from clause.
        overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
        overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
        Include fromInclude = mapperObjFactory.createInclude();
        overallChooseOtherwiseTrim.getInclude().add(fromInclude);
        fromInclude.setRefid(sqlId + MapperElm.MAPPER_FROM);

        // Append filter clause, the computed filter was embedded inside where
        // clause.
        if (StringUtils.isNotBlank(filterClause)) {

            // Append the existing filter.
            overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
            overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
            overallChooseOtherwiseTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + filterClause + MapperElm.CDATA_ANCHOR_END);

            // Append where clause and order by clause.
            overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
            overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
            If overallChooseOtherwiseTrimIf = mapperObjFactory.createIf();
            overallChooseOtherwiseTrim.getIf().add(overallChooseOtherwiseTrimIf);
            overallChooseOtherwiseTrimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);
            Trim overallChooseOtherwiseTrimIfTrim = mapperObjFactory.createTrim();
            overallChooseOtherwiseTrimIf.getTrim().add(overallChooseOtherwiseTrimIfTrim);
            overallChooseOtherwiseTrimIfTrim.setPrefix(MapperElm.SQL_AND_FULL);
            overallChooseOtherwiseTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
            overallChooseOtherwiseTrimIfTrim
                    .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

            // TODO 需要仿照MySQL的针对caseFilterSpec的处理
            // if (ctxSpec != null) {
            // TODO
            // overallChooseOtherwise.getChoose().add(UserFilterBulider.buildFilter(MapperElm.SQL_AND_FULL,
            // mapperObjFactory, userfilter));
            // }

        } else {
            // Append where clause.
            overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
            overallChooseOtherwise.getTrim().add(overallChooseOtherwiseTrim);
            If overallChooseOtherwiseTrimIf = mapperObjFactory.createIf();
            overallChooseOtherwiseTrim.getIf().add(overallChooseOtherwiseTrimIf);
            overallChooseOtherwiseTrimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);
            Trim overallChooseOtherwiseTrimIfTrim = mapperObjFactory.createTrim();
            overallChooseOtherwiseTrimIf.getTrim().add(overallChooseOtherwiseTrimIfTrim);
            // TODO Changed at 2015.05.27，需要密切观察是否会产生连带影响
            if (joinType == MapperSql.JOIN_TYPE_OUTER || joinType == MapperSql.JOIN_TYPE_NONE) {
                overallChooseOtherwiseTrimIfTrim.setPrefix(MapperElm.SQL_WHERE_SIMPLE);
            } else {
                overallChooseOtherwiseTrimIfTrim.setPrefix(MapperElm.SQL_AND_SIMPLE);
            }
            overallChooseOtherwiseTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_OR);
            overallChooseOtherwiseTrimIfTrim
                    .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

            // TODO 需要仿照MySQL的针对caseFilterSpec的处理
            // if (ctxSpec != null) {
            // if
            // (overallChooseOtherwiseTrimIfTrim.getPrefix().equals(MapperElm.SQL_AND_FULL))
            // {
            // overallChooseOtherwiseTrimIfTrim.getChoose().add(
            // UserFilterBulider.buildFilter(MapperElm.SQL_AND_FULL,
            // mapperObjFactory, userfilter));
            // } else {
            // overallChooseOtherwiseTrimIfTrim.getChoose().add(
            // UserFilterBulider.buildFilter(MapperElm.SQL_AND_FULL,
            // mapperObjFactory, userfilter));
            //
            // If noWhereIf = mapperObjFactory.createIf();
            // noWhereIf.setTest(MapperElm.ATTR__WHERE_CLAUSE +
            // JavaSrcElm.EXPR_OBJ_IS_NULL);
            // overallChooseOtherwiseTrim.getIf().add(noWhereIf);
            // noWhereIf.getChoose().add(
            // UserFilterBulider.buildFilter(overallChooseOtherwiseTrimIfTrim.getPrefix(),
            // mapperObjFactory, userfilter));
            // }
            // }

        }

        overallChooseOtherwise.setCDataEnd(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.RIGHT_PARENTHESIS + MapperElm.WHITE_SPACE
                + MapperElm.SQL_WHERE_SIMPLE + MapperElm.WHITE_SPACE + MapperElm.SQL_RN + MapperElm.SQL_BETWEEN + MapperElm.ANCHOR_START_ROW
                + MapperElm.SQL_AND_FULL + MapperElm.ANCHOR_END_ROW + MapperElm.CDATA_ANCHOR_END);

    }

}
