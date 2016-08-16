package bh.toolkit.srcgen.connector.rdb;

import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CaseFilterSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;
import bh.toolkit.srcgen.model.mybatis.ObjectFactory;
import bh.toolkit.srcgen.model.mybatis.Select;
import bh.toolkit.srcgen.registry.MapperSql;
import bh.toolkit.srcgen.registry.OneToOneIdx;

import java.util.List;

public interface IPageQueryBuilder {

    public void definePagingQuery(Select select, ArtifactSpec artifactSpec, SelectSpec selectSpec, List<OneToOneIdx> descOtoIdxList, MapperSql mapperSql, String sqlId,
                                  CaseFilterSpec topCaseFilterSpec, ObjectFactory mapperObjFactory);

}
