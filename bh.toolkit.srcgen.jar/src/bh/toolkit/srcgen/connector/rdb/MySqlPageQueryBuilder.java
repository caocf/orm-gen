package bh.toolkit.srcgen.connector.rdb;

import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CaseFilterSpec;
import bh.toolkit.srcgen.model.artifact.ResultFilterSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;
import bh.toolkit.srcgen.model.mybatis.*;
import bh.toolkit.srcgen.registry.MapperSql;
import bh.toolkit.srcgen.registry.OneToOneIdx;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MySqlPageQueryBuilder implements IPageQueryBuilder {

    @Override
    public void definePagingQuery(Select select, ArtifactSpec artifactSpec, SelectSpec selectSpec, List<OneToOneIdx> descOtoIdxList, MapperSql mapperSql, String sqlId,
                                  CaseFilterSpec topCaseFilterSpec, ObjectFactory mapperObjFactory) {

        // //////////
        // Append the case of "limit X ".
        // //////////
        int joinType = mapperSql.getJoinType();

        // Refer to common select clause and from clause.
        Include selectFromInclude = mapperObjFactory.createInclude();
        selectFromInclude.setRefid(sqlId);
        select.getInclude().add(selectFromInclude);

        // 当filterClause不为空时，针对外关联的情况，filter已经在join子句中体现
        StringBuffer filterClause = mapperSql.getWhereClause();
        Trim overallTrim;
        if (StringUtils.isNotBlank(filterClause)) {

            // Append the existing filter.
            overallTrim = mapperObjFactory.createTrim();
            select.getTrim().add(overallTrim);
            overallTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + filterClause + MapperElm.CDATA_ANCHOR_END);

            // 判断whereClause是否为空
            overallTrim = mapperObjFactory.createTrim();
            select.getTrim().add(overallTrim);
            If overallTrimIf = mapperObjFactory.createIf();
            overallTrim.getIf().add(overallTrimIf);
            overallTrimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);

            // 将whereClause设置进入trim中
            Trim overallTrimIfTrim = mapperObjFactory.createTrim();
            overallTrimIf.getTrim().add(overallTrimIfTrim);
            overallTrimIfTrim.setPrefix(MapperElm.SQL_AND_FULL);
            overallTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
            overallTrimIfTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

            // Added @2015.10.25, in order to support "caseFilterSpec".
            // 针对在查询中配置了<caseFilterSpec/>的情况
            if (topCaseFilterSpec != null) {
                composeOverallTrim(overallTrim, topCaseFilterSpec, mapperObjFactory);
            }

        } else { // 当filterClause为空时

            // 判断whereClause是否为空
            overallTrim = mapperObjFactory.createTrim();
            select.getTrim().add(overallTrim);
            If overallTrimIf = mapperObjFactory.createIf();
            overallTrim.getIf().add(overallTrimIf);
            overallTrimIf.setTest(MapperElm.EXPR_WHERE_CLAUSE_ISNOT_NULL);

            // 将whereClause设置进入trim中，针对外关联的情况，whereClause子句要以“where”起始
            Trim overallTrimIfTrim = mapperObjFactory.createTrim();
            overallTrimIf.getTrim().add(overallTrimIfTrim);
            if (joinType == MapperSql.JOIN_TYPE_OUTER || joinType == MapperSql.JOIN_TYPE_NONE) {
                overallTrimIfTrim.setPrefix(MapperElm.SQL_WHERE_FULL);
                overallTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
            } else {
                overallTrimIfTrim.setPrefix(MapperElm.SQL_AND_FULL);
                overallTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
            }
            // overallTrimIfTrim.setPrefixOverrides(MapperElm.SQL_AND_OR);
            overallTrimIfTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_WHERE_CLAUSE + MapperElm.CDATA_ANCHOR_END);

            // Added @2015.10.25, in order to support "caseFilterSpec".
            // 针对在查询中配置了<caseFilterSpec/>的情况
            if (topCaseFilterSpec != null) {
                composeOverallTrim(overallTrim, topCaseFilterSpec, mapperObjFactory);
            }

        }

        // Get the default order by config.
        StringBuffer defaultOrderByStrBuf = new StringBuffer();
        computeDefaultOrderByClause(defaultOrderByStrBuf, artifactSpec, selectSpec);

        // 考虑下级一对一查询
        if (descOtoIdxList != null && descOtoIdxList.size() > 0) {
            for (OneToOneIdx descOneToOneIdx : descOtoIdxList) {
                selectSpec = descOneToOneIdx.getOneToOne().getSelectSpec();
                computeDefaultOrderByClause(defaultOrderByStrBuf, artifactSpec, selectSpec);
            }
        }

        // Append the given order by clause or the default order by.
        Choose overallChoose = mapperObjFactory.createChoose();
        select.getChoose().add(overallChoose);
        When overallChooseWhen = mapperObjFactory.createWhen();
        overallChoose.getWhen().add(overallChooseWhen);
        overallChooseWhen.setTest(MapperElm.EXPR_ORDERBY_CLAUSE_ISNOT_NULL);
        Trim overallChooseWhenTrim = mapperObjFactory.createTrim();
        overallChooseWhen.getTrim().add(overallChooseWhenTrim);
        overallChooseWhenTrim.setPrefix(MapperElm.SQL_ORDERBY_SIMPLE + MapperElm.WHITE_SPACE);
        overallChooseWhenTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.ANCHOR_MAPPERSQL_ORDERBY_CLAUSE + MapperElm.CDATA_ANCHOR_END);
        Otherwise overallChooseOthereise = mapperObjFactory.createOtherwise();
        overallChoose.setOtherwise(overallChooseOthereise);
        Trim overallChooseOtherwiseTrim = mapperObjFactory.createTrim();
        overallChooseOthereise.getTrim().add(overallChooseOtherwiseTrim);
        overallChooseOtherwiseTrim.setPrefix(MapperElm.SQL_ORDERBY_SIMPLE + MapperElm.WHITE_SPACE);
        overallChooseOtherwiseTrim.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + defaultOrderByStrBuf + MapperElm.CDATA_ANCHOR_END);

        Choose newChoose = mapperObjFactory.createChoose();
        select.getChoose().add(newChoose);
        When newWhen = mapperObjFactory.createWhen();
        newChoose.getWhen().add(newWhen);
        newWhen.setTest(MapperElm.ATTR_START_ROW + JavaSrcElm.EXPR_INT_IS_INVALID + MapperElm.SQL_OR_FULL + MapperElm.ATTR_PAGE_SIZE
                + JavaSrcElm.EXPR_INT_IS_INVALID);
        newWhen.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.LIMIT_ROWS + MapperElm.CDATA_ANCHOR_END);
        Otherwise newChooseOtherwise = mapperObjFactory.createOtherwise();
        newChoose.setOtherwise(newChooseOtherwise);
        newChooseOtherwise.setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + MapperElm.SQL_LIMIT + MapperElm.WHITE_SPACE + MapperElm.ANCHOR_START_ROW
                + MapperElm.COMMA + MapperElm.WHITE_SPACE + MapperElm.ANCHOR_PAGE_SIZE + MapperElm.CDATA_ANCHOR_END);

    }

    private void composeOverallTrim(Trim overallTrim, CaseFilterSpec topCaseFilterSpec, ObjectFactory mapperObjFactory) {

        // 增加对topCaseFilterSpec中test条件的判断
        If overallTrimIf = mapperObjFactory.createIf();
        overallTrim.getIf().add(overallTrimIf);
        overallTrimIf.setTest(topCaseFilterSpec.getTest());

        // 增加针对caseFilterSpec的各种分支
        Choose overallTrimIfChoose = mapperObjFactory.createChoose();
        overallTrimIf.getChoose().add(overallTrimIfChoose);
        When overallTrimIfChooseWhen = null;
        Trim overallTrimIfChooseWhenTrim = null;
        List<ResultFilterSpec> ResultFilterSpecList = topCaseFilterSpec.getResultFilter();
        for (ResultFilterSpec resultFilterSpec : ResultFilterSpecList) {

            // 每一个分支用when元素标示
            overallTrimIfChooseWhen = mapperObjFactory.createWhen();
            overallTrimIfChoose.getWhen().add(overallTrimIfChooseWhen);
            overallTrimIfChooseWhen.setTest(resultFilterSpec.getTest());

            // 在when下面创建trim
            overallTrimIfChooseWhenTrim = mapperObjFactory.createTrim();
            overallTrimIfChooseWhen.getTrim().add(overallTrimIfChooseWhenTrim);
            overallTrimIfChooseWhenTrim.setPrefix(MapperElm.SQL_AND_FULL);
            overallTrimIfChooseWhenTrim.setPrefixOverrides(MapperElm.SQL_AND_SIMPLE + MapperElm.WHITE_SPACE);
            overallTrimIfChooseWhenTrim
                    .setCDataBegin(MapperElm.CDATA_ANCHOR_BEGIN + resultFilterSpec.getClause() + MapperElm.CDATA_ANCHOR_END);

        }

    }

    private void computeDefaultOrderByClause(StringBuffer defaultOrderByStrBuf, ArtifactSpec artifactSpec, SelectSpec selectSpec) {

        String defaultOrderByStr = selectSpec.getDefaultOrderBy();
        if (StringUtils.isNotBlank(defaultOrderByStr)) {
            String[] defaultOrderByArray = defaultOrderByStr.split(MapperElm.COMMA);
            if (defaultOrderByArray.length != 0) {
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

    }

}
