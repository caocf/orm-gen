package bh.toolkit.srcgen.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.JavaSrcElm;
import bh.toolkit.srcgen.lang.MapperElm;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.CaseFilterSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.artifact.RelationshipSpec;
import bh.toolkit.srcgen.model.artifact.SelectSpec;
import bh.toolkit.srcgen.model.artifact.WorkspaceSpec;
import bh.toolkit.srcgen.model.mybatis.Collection;
import bh.toolkit.srcgen.model.mybatis.Mapper;
import bh.toolkit.srcgen.model.mybatis.Result;
import bh.toolkit.srcgen.model.mybatis.ResultMap;
import bh.toolkit.srcgen.model.rdb.ColumnSpec;
import bh.toolkit.srcgen.model.rdb.TableSpec;
import bh.toolkit.srcgen.registry.CtxCacheFacade;
import bh.toolkit.srcgen.registry.OneToOneIdx;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;
import bh.toolkit.srcgen.util.MapperFormatter;
import bh.toolkit.srcgen.util.ProfileHelper;
import bh.toolkit.srcgen.util.StringHelper;

public class ResultMapBuilder {

	private static Logger logger = Logger.getLogger(ResultMapBuilder.class);

	// private bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory;
	private bh.toolkit.srcgen.model.mybatis.ObjectFactory mapperObjFactory;
	private SelectBuilder selectBuilder;

	public ResultMapBuilder(bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory, bh.toolkit.srcgen.model.mybatis.ObjectFactory mapperObjFactory) {
		// this.artifactObjFactory = artifactObjFactory;
		this.mapperObjFactory = mapperObjFactory;
		this.selectBuilder = new SelectBuilder(mapperObjFactory);
	}

	public void buildAllResultMap(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec) throws AppException {

		try {

			// The list of SingleArtifactSpec.
			// List<DaoSpec> daoSpecList = artifactSpec.getDaoSpec();
			List<SelectSpec> selectSpecList = null;
			SelectSpec selectSpec = null;
			// String className = null;
			String tableName = null;
			String tableAlias = null;

			// The list of SelectSpec in SingleArtifactSpec.
			selectSpecList = daoSpec.getSelectSpec();

			// Build all resultMap.
			for (int i = 0; i < selectSpecList.size(); i++) {

				// Get class name and table name.
				selectSpec = selectSpecList.get(i);
				// className = selectSpec.getClassName();
				tableName = selectSpec.getTableName();
				tableAlias = selectSpec.getTableAlias();

				if (StringUtils.isNotBlank(tableAlias) == true) {
					logger.debug("SELECT: Handle \"selectSpec\" for table \"" + tableName + "(" + tableAlias + ").\"");
				} else {
					logger.debug("SELECT: Handle \"selectSpec\" for table \"" + tableName + "\".");
				}

				// Map DB table and column into VO class.
				addVoClass(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), selectSpec);

				/*
				 * @2015.10.23，暂时用不到这种查询 // Create 'select' element with id: 'countBySqlClause'. selectBuilder.buildCountRootTableBySql(mapper, artifactSpec, daoSpec, selectSpec);
				 */

				/*
				 * @2015.10.23，暂时用不到这种查询 // Create 'resultMap' element with id: 'select<Root Table Name>ResultMap'. buildRootTableResultMap(mapper, artifactSpec, daoSpec,
				 * selectSpec);
				 */

				/*
				 * @2015.10.23，暂时用不到这种查询 // Create 'select' element with id: 'select<Root Table Name>ByPK'. selectBuilder.buildSelectRootTableByPK(mapper, artifactSpec, daoSpec,
				 * selectSpec);
				 */

				/*
				 * @2015.10.23，暂时用不到这种查询 // Create 'select' element with id: 'select<Root Table Name>BySqlClause'. selectBuilder.buildSelectRootTableBySql(mapper, artifactSpec,
				 * daoSpec, selectSpec);
				 */

				// Build all oneToOne statements.
				buildTopJoinStmt(mapper, artifactSpec, daoSpec, selectSpec);

			}

		} catch (Throwable t) {
			t.printStackTrace();
			ExceptionUtil.handleException(t, logger);
		}

	}

	/**
	 * 递归调用，穷尽SelectSpec下所有的SelectSpec，注册对应的VO
	 * 
	 * @param workspaceSpec
	 * @param selectSpec
	 * @throws AppException
	 */
	private void addVoClass(WorkspaceSpec workspaceSpec, SelectSpec selectSpec) throws AppException {

		// 根据当前SelectSpec对应的表，注册VO
		CtxCacheFacade.addVoClass(workspaceSpec, selectSpec.getTableName());

		// 如果存在子SelectSpec，递归调用注册VO
		List<RelationshipSpec> oneToOneList = selectSpec.getOneToOne();
		SelectSpec childSelectSpec = null;
		for (RelationshipSpec oneToOne : oneToOneList) {
			childSelectSpec = oneToOne.getSelectSpec();
			addVoClass(workspaceSpec, childSelectSpec);
		}

	}

	// private void buildRootTableResultMap(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec selectSpec) throws AppException {
	//
	// WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();
	//
	// try {
	//
	// // The expected 'id' of 'resultMap'.
	// String tableName = selectSpec.getTableName();
	// String voSimpleName = JavaFormatter.getJavaStyle(tableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
	// String resultMapId = MapperFormatter.getResultMapIdOfRootTab(selectSpec);
	//
	// logger.debug("RESULTMAP: Prepare to build resultMap with id '" + resultMapId + "'.");
	//
	// // Lookup 'resultMap' element with full id containing name space.
	// ResultMap resultMap = CtxCacheFacade.lookupResultMap(workspaceSpec, daoSpec, resultMapId);
	// if (resultMap != null) {
	// return;
	// }
	//
	// // The list of ResultMap.
	// List<ResultMap> resultMapList = mapper.getResultMap();
	//
	// // Initiate blank 'resultMap' element.
	// resultMap = mapperObjFactory.createResultMap();
	// resultMapList.add(resultMap);
	//
	// // Register the new 'resultMap' element.
	// CtxCacheFacade.addResultMap(workspaceSpec, daoSpec, resultMapId, resultMap);
	//
	// // Populate the 'id' attribute for resultMap with value '<Root Table
	// // Name>ResultMap'.
	// resultMap.setId(resultMapId);
	//
	// // Populate the 'class' attribute for resultMap.
	// String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
	// resultMap.setType(paramFullName);
	//
	// populateResultMap(resultMap, artifactSpec, selectSpec, null, voSimpleName);
	//
	// } catch (Throwable t) {
	// ExceptionUtil.handleException(t, logger);
	// }
	//
	// }

	//
	// <ancesSelectSpec>
	// ....<descOneToOne>
	// ........<descSelectSpec>
	// ............<descOneToMany />
	// ........</descSelectSpec>
	// ....</descOneToOne>
	// ....<descOneToMany />
	// </ancesSelectSpec>
	//
	private void buildOtoResultMap(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec, List<OneToOneIdx> descOtoIdxList, String resultMapId)
			throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// The expected 'id' of 'resultMap'.
			if (resultMapId == null) {
				resultMapId = MapperFormatter.getResultMapIdOfMultiTab(ancesSelectSpec, descOtoIdxList);
			}

			logger.debug("RESULTMAP: Prepare to build resultMap with id '" + resultMapId + "'.");

			// Lookup 'resultMap' element with 'id'.
			ResultMap resultMap = CtxCacheFacade.lookupResultMap(workspaceSpec, daoSpec, resultMapId);
			if (resultMap != null) {
				return;
			}

			// The list of ResultMap.
			List<ResultMap> resultMapList = mapper.getResultMap();

			// Initiate blank 'resultMap' element.
			resultMap = mapperObjFactory.createResultMap();
			resultMapList.add(resultMap);

			// Register the new 'resultMap' element.
			CtxCacheFacade.addResultMap(workspaceSpec, daoSpec, resultMapId, resultMap);

			// Populate the 'id' attribute for resultMap with value '<Root TableName>ResultMap'.
			resultMap.setId(resultMapId);

			// Populate the 'class' attribute for ResultMap.
			String ancesTableName = ancesSelectSpec.getTableName();
			String ancesParamFullName = JavaFormatter.getDtoXFullName(workspaceSpec, ancesTableName);
			resultMap.setType(ancesParamFullName);
			String voSimpleName = JavaFormatter.getJavaStyle(ancesTableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
			populateResultMap(resultMap, artifactSpec, ancesSelectSpec, null, voSimpleName);

			// If contain one to many relationship, then claim internal select.
			List<RelationshipSpec> descOtmList = ancesSelectSpec.getOneToMany();
			if (descOtmList != null && descOtmList.size() != 0) {
				claimInternalSelect(resultMap, artifactSpec, daoSpec, ancesSelectSpec, null);
			}

			// Prepare to populate 'result' elements for oneToOne relationship.
			OneToOneIdx descOtoIdx = null;
			String refToSon = null;
			SelectSpec descSelectSpec = null;

			for (int i = 0; i < descOtoIdxList.size(); i++) {

				// Get attributes of every descendant 'selectSpec' element.
				descOtoIdx = descOtoIdxList.get(i);
				refToSon = descOtoIdx.getOneToOne().getRefToSon(); // Done
				descSelectSpec = descOtoIdx.getOneToOne().getSelectSpec();
				if (StringUtils.isBlank(refToSon) == true) {
					String descVoFullName = JavaFormatter.getVoFullName(workspaceSpec, descSelectSpec.getTableName());
					refToSon = StringHelper.toLowerCase(JavaFormatter.getClassSimpleName(descVoFullName), 0);
				}

				// populateResultMap(resultMap, artifactSpec, ancesSelectSpec, descOtoIdx, descOtoIdx.getAttrNameChain());
				populateResultMap(resultMap, artifactSpec, ancesSelectSpec, descOtoIdx, refToSon);

				// If contain one to many relationship, then claim internal
				// select.
				descOtmList = descSelectSpec.getOneToMany();
				if (descOtmList != null && descOtmList.size() != 0) {

					logger.debug("CLAIM INTERNAL SELECT: one to many " + "relationship exist, claim internal select.");

					claimInternalSelect(resultMap, artifactSpec, daoSpec, null, descOtoIdx);
				}

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	// Inside 'fatherSelectSpec':
	// <fatherSelectSpec>
	// ....<sonOneToMany>
	// ........<sonSelectSpec />
	// ....</sonOneToMany>
	// </fatherSelectSpec>
	//
	private void buildInternalOtmResultMap(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, RelationshipSpec sonOneToManySpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			// The expected 'id' of 'resultMap'.
			String listOfSon = sonOneToManySpec.getListOfSon();
			SelectSpec sonSelectSpec = sonOneToManySpec.getSelectSpec();
			String resultMapId = MapperFormatter.getResultMapIdOfInternalOtm(artifactSpec, daoSpec, sonSelectSpec, listOfSon); // Done.

			logger.debug("RESULTMAP: Prepare to build resultMap with id '" + resultMapId + "'.");

			// Lookup 'resultMap' element with 'id'.
			ResultMap resultMap = CtxCacheFacade.lookupResultMap(workspaceSpec, daoSpec, resultMapId);
			if (resultMap != null) {
				return;
			}

			// The list of ResultMap.
			List<ResultMap> resultMapList = mapper.getResultMap();

			// Initiate blank 'resultMap' element.
			resultMap = mapperObjFactory.createResultMap();
			resultMapList.add(resultMap);

			// Register the new 'resultMap' element.
			CtxCacheFacade.addResultMap(workspaceSpec, daoSpec, resultMapId, resultMap);

			// Populate the 'id' attribute for resultMap with value '<Root Table Name>ResultMap'.
			resultMap.setId(resultMapId);

			// Populate the 'parameterType' attribute for ResultMap.
			String sonTableName = sonSelectSpec.getTableName();
			String sonParamFullName = JavaFormatter.getDtoXFullName(workspaceSpec, sonTableName);
			resultMap.setType(sonParamFullName);

			String tableName = sonSelectSpec.getTableName();
			String voSimpleName = JavaFormatter.getJavaStyle(tableName, false, false) + JavaSrcElm.VO_NAME_SUFFIX;
			populateResultMap(resultMap, artifactSpec, sonSelectSpec, null, voSimpleName);

			// Claim internal select.
			claimInternalSelect(resultMap, artifactSpec, daoSpec, sonSelectSpec, null);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Handle 'oneToOne' under top 'selectSpec'.
	//
	// From top 'selectSpec':
	// <ancesSelectSpec>
	// ....<descOneToOne> <<--
	// ........<descSelectSpec>
	// ........</descSelectSpec>
	// ....</descOneToOne>
	// </ancesSelectSpec>
	//
	private void buildTopJoinStmt(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec ancesSelectSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			logger.debug("Execute 'buildTopLevelJoinStmt' from the top 'selectSpec'");

			// 所有的oneToOne关系
			List<OneToOneIdx> descOtoIdxList = new ArrayList<OneToOneIdx>();

			// 当前查询所有的caseFilterSpec汇集在一个对象中
			List<CaseFilterSpec> caseFilterSpecList = ancesSelectSpec.getCaseFilter();
			CaseFilterSpec topCaseFilterSpec = null;
			for (int i = 0; i < caseFilterSpecList.size(); i++) {
				if (i == 0) {
					topCaseFilterSpec = caseFilterSpecList.get(i); // 取第一个caseFilterSpec为top对象
					break;
				}
			}

			// 计算有多少个oneToOne
			String tableName = ancesSelectSpec.getTableName();
			String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			computeOtoIndex(workspaceSpec, descOtoIdxList, topCaseFilterSpec, ancesSelectSpec, null, paramFullName, null);

			// Create oneToOne 'resultMap' element, and mark the needed internal oneToMany result map inside the created oneToOne 'resultMap'.
			buildOtoResultMap(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList, null);

			// Build all needed oneToMany statements for these oneToOne statements.
			buildInternalStmts(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList);

			// Build multi-table select by PK, including oneToOne or oneToMany
			selectBuilder.buildSelectMultiTabByPK(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList);

			// Build multi-table select by SqlClause, including oneToOne or oneToMany.
			selectBuilder.buildSelectMultiTabBySql(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList, topCaseFilterSpec);

			// Build multi-table select by SqlClause, including oneToOne or oneToMany.
			selectBuilder.buildCountMultiTabBySql(mapper, artifactSpec, daoSpec, ancesSelectSpec, descOtoIdxList, topCaseFilterSpec);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Handle secondary 'oneToOne' under higher 'oneToMany'.
	//
	// From higher 'oneToMany':
	// <fatherSelectSpec>
	// ....<sonOneToMany>
	// ........<sonSelectSpec>
	// ............<descOneToOne> <<-- Current
	// ................<descSelectSpec>
	// ................</descSelectSpec>
	// ............</descOneToOne>
	// ........</sonSelectSpec>
	// ........<fatherAttr />
	// ....</sonOneToMany>
	// </fatherSelectSpec>
	//
	private void buildSecondaryJoinStmt(Mapper mapper, String selectId, String resultMapId, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec fatherSelectSpec,
			RelationshipSpec sonOtmSpec) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			logger.debug("Execute 'buildSecJoinStmt' from higher 'oneToMany'.");

			// Get 'selectSpec' under higher oneToMany RelationshipSpec.
			SelectSpec sonSelectSpec = sonOtmSpec.getSelectSpec();
			List<OneToOneIdx> descOtoIdxList = new ArrayList<OneToOneIdx>();

			// 当前查询所有的caseFilterSpec汇集在一个对象中
			List<CaseFilterSpec> caseFilterSpecList = sonSelectSpec.getCaseFilter();
			CaseFilterSpec topCaseFilterSpec = null;
			for (int i = 0; i < caseFilterSpecList.size(); i++) {
				if (i == 0) {
					topCaseFilterSpec = caseFilterSpecList.get(i); // 取第一个caseFilterSpec为top对象
					break;
				}
			}

			// 计算有多少个oneToOne
			String tableName = sonSelectSpec.getTableName();
			String paramFullName = JavaFormatter.getDtoXFullName(workspaceSpec, tableName);
			computeOtoIndex(workspaceSpec, descOtoIdxList, topCaseFilterSpec, sonSelectSpec, null, paramFullName, null);

			// Create oneToOne 'resultMap' element.
			buildOtoResultMap(mapper, artifactSpec, daoSpec, sonSelectSpec, descOtoIdxList, resultMapId);

			// Build all needed oneToMany statements for these oneToOne statements.
			buildInternalStmts(mapper, artifactSpec, daoSpec, sonSelectSpec, descOtoIdxList);

			// Build internal oneToOne select with given 'selectId' and 'resultMapId'.
			selectBuilder.buildInternalSelectOto(mapper, artifactSpec, daoSpec, fatherSelectSpec, sonOtmSpec, descOtoIdxList, selectId, resultMapId);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	// <fatherSelectSpec>
	// ....<fatherOneToOne>
	// ........<sonSelectSpec>
	// ............<sonOneToOne />
	// ........</sonSelectSpec>
	// ....</fatherOneToOne>
	// ....<fatherOneToOne>
	// ........<sonSelectSpec>
	// ............<sonOneToOne />
	// ........</sonSelectSpec>
	// ....</fatherOneToOne>
	// </fatherSelectSpec>
	private void computeOtoIndex(WorkspaceSpec workspaceSpec, List<OneToOneIdx> otoIndexList, CaseFilterSpec topCaseFilterSpec, SelectSpec fatherSelectSpec,
			OneToOneIdx ancesOtoIndex, String fullClassNameChain, String simpleClassNameChain) throws AppException {

		try {

			logger.debug("COMPUTE OTO: Begin to compute oneToOne relationship.");

			OneToOneIdx fatherOtoIdx = null;
			RelationshipSpec fatherOto = null;
			SelectSpec sonSelectSpec = null;
			String sonClassName = null;
			String sonAttrName = null;
			String sonTableName = null;
			String sonTableAlias = null;

			// 分析在fatherSelectSpec下的oneToOne
			List<RelationshipSpec> fatherOtoList = fatherSelectSpec.getOneToOne();
			// If 'fatherSelectSpec' has more than one oneToOne relationship, then every one of them has peer.
			boolean hasPeer = fatherOtoList.size() > 1 ? true : false;
			int fatherOtoIdxLoc = otoIndexList.size() - 1;
			List<RelationshipSpec> sonOtoList = null;
			for (int i = 0; i < fatherOtoList.size(); i++) {

				// Record the father of current oneToOne relationship.
				fatherOtoIdx = new OneToOneIdx();
				fatherOtoIdx.setFatherOtoIndex(ancesOtoIndex);
				fatherOtoIdx.setFatherOtoIndexLoc(fatherOtoIdxLoc);

				// Register 'fatherOneToOne' into otoIndexList.
				otoIndexList.add(fatherOtoIdx);

				// Set other attributes for 'fatherOtoIdx'.
				fatherOto = fatherOtoList.get(i);
				fatherOtoIdx.setOneToOne(fatherOto);
				fatherOtoIdx.setHasPeer(hasPeer);
				sonSelectSpec = fatherOto.getSelectSpec();

				String refToSon = fatherOto.getRefToSon();
				if (StringUtils.isBlank(refToSon) == true) {
					String sonVoFullName = JavaFormatter.getVoFullName(workspaceSpec, sonSelectSpec.getTableName());
					refToSon = StringHelper.toLowerCase(JavaFormatter.getClassSimpleName(sonVoFullName), 0);
				}

				if (simpleClassNameChain == null) {
					sonAttrName = refToSon;
					fatherOtoIdx.setAttrNameChain(sonAttrName);
				} else {
					sonAttrName = simpleClassNameChain + JavaSrcElm.DOT + refToSon;
					fatherOtoIdx.setAttrNameChain(sonAttrName);
				}

				sonTableName = sonSelectSpec.getTableName();
				sonTableAlias = sonSelectSpec.getTableAlias();
				sonClassName = fullClassNameChain + JavaSrcElm.DOT + refToSon;
				fatherOtoIdx.setLeftTableName(fatherSelectSpec.getTableName());
				fatherOtoIdx.setLeftTableAlias(fatherSelectSpec.getTableAlias());
				fatherOtoIdx.setRightTableName(sonSelectSpec.getTableName());
				fatherOtoIdx.setRightTableAlias(sonSelectSpec.getTableAlias());

				// Log the registration of 'fatherOneToOne'.
				if (StringUtils.isNotBlank(sonTableAlias) == true) {
					logger.debug("REGISTER: Register an OTO relationship into otoIndexList. " + "className = \"" + sonClassName + "\", tableName = \"" + sonTableName + "("
							+ sonTableAlias + ")\".");
				} else {
					logger.debug("REGISTER: Register an OTO relationship into otoIndexList. " + "className = \"" + sonClassName + "\", tableName = \"" + sonTableName + "\".");
				}
				logger.debug("JUDGE PEER: Current OTO relationship has peer '" + hasPeer + "'");

				// 针对遇到的每个selectSpec，把在其下的caseFilterSpec中的所有resultFilterSpec统一记录到topCaseFilterSpec中
				List<CaseFilterSpec> caseFilterSpecList = sonSelectSpec.getCaseFilter();
				if (caseFilterSpecList != null && caseFilterSpecList.size() > 0) {
					for (CaseFilterSpec caseFilterSpec : caseFilterSpecList) {
						topCaseFilterSpec.getResultFilter().addAll(caseFilterSpec.getResultFilter());
					}
				}

				// Get son oneToOne relationship.
				sonOtoList = sonSelectSpec.getOneToOne();

				// If having son oneToOne relationship.
				if (sonOtoList != null && sonOtoList.size() != 0) {
					// Invoke recursively to find all descendant oneToOne relationships
					computeOtoIndex(workspaceSpec, otoIndexList, topCaseFilterSpec, sonSelectSpec, fatherOtoIdx, sonClassName, sonAttrName);
				}

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// <ancesSelectSpec>
	// ....<descOneToOne />
	// </ancesSelectSpec>
	//
	private void populateResultMap(ResultMap resultMap, ArtifactSpec artifactSpec, SelectSpec ancesSelectSpec, OneToOneIdx descOneToOneIdx, String attrNameChain)
			throws AppException {

		try {

			// If the given oneToOne is not null, then consider it as current selectSpec.
			// String ancesClassName = ancesSelectSpec.getClassName();
			RelationshipSpec descOneToOne = null;
			SelectSpec currSelectSpec = null;
			// String refToSon = null;

			if (descOneToOneIdx != null) {
				descOneToOne = descOneToOneIdx.getOneToOne();
				currSelectSpec = descOneToOne.getSelectSpec();
				// refToSon = descOneToOne.getRefToSon();
			} else {
				currSelectSpec = ancesSelectSpec;
			}

			String tableName = currSelectSpec.getTableName();
			String tableAlias = currSelectSpec.getTableAlias();

			// Find OrmTable according to the table name.
			TableSpec ormTable = CtxCacheFacade.lookupTableSpec(tableName);
			if (ormTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: \"" + tableName + "\"");
				return;
			}

			List<Result> resultList = resultMap.getResult();
			Result result = null;

			ColumnSpec ormColumn = null;
			String columnName = null;
			String attrName = null;
			// OrmAttr ormAttr = null;

			logger.debug("ATTR NAME CHAIN: The given attrNameChain is: " + attrNameChain);

			if (attrNameChain == null) {
				attrNameChain = "";
			} else {
				attrNameChain = attrNameChain + JavaSrcElm.DOT;
			}

			HashSet<String> includedAttrs = ProfileHelper.getIncludedAttrName(currSelectSpec);
			HashSet<String> excludedAttrs = ProfileHelper.getExcludedAttrName(currSelectSpec);

			// Populate 'result' elements according to the the ClassEntity definition.
			List<ColumnSpec> ormColumnList = ormTable.getColumnList();
			for (int i = 0; i < ormColumnList.size(); i++) {

				// Check inclusion and exclusion, inclusion take higher preference.
				ormColumn = ormColumnList.get(i);
				columnName = ormColumn.getName();

				// Translate column name to java attribute name.
				attrName = JavaFormatter.getJavaStyle(columnName, false, false);

				if (includedAttrs.size() != 0) {
					if (includedAttrs.contains(attrName) == false) {
						logger.debug("EXCLUDE ATTRIBUTE: Property '" + attrName + "' is NOT in the inclusion list, skipped.");
						continue;
					}
				} else if (excludedAttrs.size() != 0) {
					if (excludedAttrs.contains(JavaSrcElm.ASTERISK) == true) { // 排除所有的属性
						break;
					}
					if (excludedAttrs.contains(attrName) == true) {
						logger.debug("EXCLUDE ATTRIBUTE: Property '" + attrName + "' is in the exclusion list, skipped.");
						continue;
					}
				}

				result = mapperObjFactory.createResult();
				result.setProperty(attrNameChain + attrName);
				result.setColumn(MapperFormatter.getColumnAlias(artifactSpec, tableName, tableAlias, columnName));
				result.setJdbcType(ormColumn.getJdbcTypeName());
				resultList.add(result);

				logger.debug("FIND MAPPING: Find mapping between attribute '" + attrNameChain + attrName + "' and column '" + columnName + "'");

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Get attributes from 'fatherSelectSpec' or 'fatherOtoIdx'.
	//
	// From 'fatherSelectSpec':
	// <fatherSelectSpec>
	// ....<sonOneToMany />
	// </fatherSelectSpec>
	//
	// From 'fatherOtoIdx':
	// <fatherOneToOne>
	// ....<fatherSelectSpec />
	// ........<sonOneToMany />
	// </fatherOneToOne>
	//
	private void claimInternalSelect(ResultMap resultMap, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec fatherSelectSpec, OneToOneIdx fatherOtoIdx) throws AppException {

		WorkspaceSpec workspaceSpec = artifactSpec.getCommonAttrSpec().getWorkspaceSpec();

		try {

			String fatherTableName = null;
			String fatherTableAlias = null;
			List<RelationshipSpec> sonOneToManyList = null;

			// If the given 'fatherSelectSpec' is not null, then get attributes from it.
			if (fatherSelectSpec != null) {
				fatherTableName = fatherSelectSpec.getTableName();
				fatherTableAlias = fatherSelectSpec.getTableAlias();
				sonOneToManyList = fatherSelectSpec.getOneToMany();
			}
			// If the given 'fatherSelectSpec' is null, then get attributes from 'fatherOtoIdx'.
			else if (fatherOtoIdx != null) {
				RelationshipSpec fatherOneToOne = fatherOtoIdx.getOneToOne();
				fatherSelectSpec = fatherOneToOne.getSelectSpec();
				fatherTableName = fatherSelectSpec.getTableName();
				fatherTableAlias = fatherSelectSpec.getTableAlias();
				sonOneToManyList = fatherSelectSpec.getOneToMany();
			} else {
				throw new IllegalArgumentException("'ancesSelectSpec' and 'otoIndex' " + "could not be null simultaneously.");
			}

			// Find OrmTable according to the table name.
			TableSpec fatherTable = CtxCacheFacade.lookupTableSpec(fatherTableName);
			if (fatherTable == null) {
				logger.error("!!! NO TABLE !!!: No table found with table name: " + fatherTableName);
				return;
			}

			// Prepare to claim internal select for every oneToMany relationship.
			RelationshipSpec sonOneToMany = null;
			SelectSpec sonSelectSpec = null;
			String listOfSon = null;
			String sonTableName = null;
			String sonParamName = null;
			String fatherAttrName = null;
			String fatherColumnName = null;
			ColumnSpec fatherColumn = null;
			// String attrAlias = null;
			String columnAlias = null;

			List<Collection> collectionList = resultMap.getCollection();
			Collection collection = null;

			for (int i = 0; i < sonOneToManyList.size(); i++) {

				sonOneToMany = sonOneToManyList.get(i);
				sonSelectSpec = sonOneToMany.getSelectSpec();
				sonTableName = sonSelectSpec.getTableName();
				sonParamName = JavaFormatter.getDtoXFullName(workspaceSpec, sonTableName);
				listOfSon = sonOneToMany.getListOfSon(); // Done
				fatherAttrName = sonOneToMany.getFatherAttr();
				fatherColumnName = JavaFormatter.getDBStyle(fatherAttrName);
				fatherColumn = fatherTable.getColumnIdx().get(fatherColumnName);

				if (StringUtils.isBlank(listOfSon) == true) {
					String sonDtoXFullName = JavaFormatter.getDtoXFullName(workspaceSpec, sonSelectSpec.getTableName());
					listOfSon = StringHelper.toLowerCase(JavaFormatter.getClassSimpleName(sonDtoXFullName), 0) + JavaSrcElm.UTIL_LIST_SIMPLE;
				}

				if (fatherColumn != null) {

					collection = mapperObjFactory.createCollection();
					columnAlias = MapperFormatter.getColumnAlias(artifactSpec, fatherTableName, fatherTableAlias, fatherColumnName);
					collection.setProperty(listOfSon);
					collection.setOfType(sonParamName);
					collection.setColumn(columnAlias);
					collection.setSelect(MapperFormatter.getSelectIdOfInternalOtm(artifactSpec, daoSpec, sonSelectSpec, listOfSon, false));
					collectionList.add(collection);

					logger.debug("FIND MAPPING: Find mapping between attribute '" + listOfSon + "' and column '" + columnAlias + "'");

				}

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	//
	// Handle 'oneToMany' directly under 'fatherSelectSpec', and those under
	// 'sonOtoIndexList'.
	//
	// Inside 'fatherSelectSpec':
	// <fatherSelectSpec>
	// ....<sonOneToMany>
	// ........<sonSelectSpec>
	// ............<descOneToOne />
	// ............<descOneToMany>
	// ................<descSelectSpec />
	// ...........</descOneToMany>
	// ........</sonSelectSpec>
	// ....</sonOneToMany>
	// ....<sonOneToOne>
	// ........<sonSelectSpec>
	// ............<descOneToMany>
	// ................<descSelectSpec>
	// ....................<descOneToOne />
	// ................</descSelectSpec>
	// ...........</descOneToMany>
	// ........</sonSelectSpec>
	// ....</sonOneToOne>
	// ....<sonOneToOne />
	// </fatherSelectSpec>
	//
	private void buildInternalStmts(Mapper mapper, ArtifactSpec artifactSpec, DaoSpec daoSpec, SelectSpec fatherSelectSpec, List<OneToOneIdx> sonOtoIndexList) throws AppException {

		try {

			// 增加DTO及对应的根VO
			CtxCacheFacade.addDtoAttr(artifactSpec, fatherSelectSpec.getTableName(), null, null, MapperElm.SQL_SELECT, false);
			// Handle 'oneToMany' directly under 'fatherSelectSpec'.
			List<RelationshipSpec> sonOneToManyList = fatherSelectSpec.getOneToMany();
			RelationshipSpec sonOneToMany = null;
			String listOfSon = null;
			SelectSpec sonSelectSpec = null;
			List<RelationshipSpec> descOneToOneList = null;
			List<RelationshipSpec> descOneToManyList = null;

			String selectId = null;
			String resultMapId = null;

			if (sonOneToManyList != null) {

				for (int i = 0; i < sonOneToManyList.size(); i++) {

					// If there is oneToOne relationship under 'sonSelectSpec', then handle them.
					sonOneToMany = sonOneToManyList.get(i);
					sonSelectSpec = sonOneToMany.getSelectSpec();
					descOneToOneList = sonSelectSpec.getOneToOne();
					descOneToManyList = sonSelectSpec.getOneToMany();

					// 为DTO增加oneToMany的列表属性
					CtxCacheFacade.addDtoAttr(artifactSpec, fatherSelectSpec.getTableName(), sonSelectSpec.getTableName(), sonOneToMany.getListOfSon(), MapperElm.SQL_SELECT, true); // Done.

					if (descOneToOneList != null && descOneToOneList.size() != 0) {

						logger.debug("OTO in OTM: Found OTO in OTM, need build addtional " + "one to one statements.");

						// Use the mandatory 'id' for private 'select'.
						listOfSon = sonOneToMany.getListOfSon(); // Done.
						selectId = MapperFormatter.getSelectIdOfInternalOtm(artifactSpec, daoSpec, sonSelectSpec, listOfSon, false);

						// Use the mandatory 'id' for private 'resultMap'.
						resultMapId = MapperFormatter.getResultMapIdOfInternalOtm(artifactSpec, daoSpec, sonSelectSpec, listOfSon);
						buildSecondaryJoinStmt(mapper, selectId, resultMapId, artifactSpec, daoSpec, fatherSelectSpec, sonOneToMany);

					}
					// If there is oneToMany relationship under 'sonSelectSpec', then handle them.
					else if (descOneToManyList != null && descOneToManyList.size() != 0) {

						logger.debug("OTM in OTM: Found OTM in OTM, need build addtional " + "one to many statements.");

						buildInternalOtmResultMap(mapper, artifactSpec, daoSpec, sonOneToMany);
						selectBuilder.buildInternalSelectOneTable(mapper, artifactSpec, daoSpec, fatherSelectSpec, sonOneToMany);

						buildInternalStmts(mapper, artifactSpec, daoSpec, sonSelectSpec, new ArrayList<OneToOneIdx>());

					}
					// If there is neither oneToOne nor oneToMany relationship under 'sonSelectSpec'.
					else {

						buildInternalOtmResultMap(mapper, artifactSpec, daoSpec, sonOneToMany);
						selectBuilder.buildInternalSelectOneTable(mapper, artifactSpec, daoSpec, fatherSelectSpec, sonOneToMany);

					}

				}

			}

			// Handle all of 'oneToMany' inside 'sonOtoIndexList'.
			OneToOneIdx sonOtoIndex = null;
			RelationshipSpec sonOneToOne = null;
			// OrmClass sonOrmClass = null;
			RelationshipSpec descOneToMany = null;
			SelectSpec descSelectSpec = null;
			if (sonOtoIndexList != null) {

				for (int i = 0; i < sonOtoIndexList.size(); i++) {

					sonOtoIndex = sonOtoIndexList.get(i);
					sonOneToOne = sonOtoIndex.getOneToOne();
					sonSelectSpec = sonOneToOne.getSelectSpec();
					descOneToManyList = sonSelectSpec.getOneToMany();

					// 为DTO增加oneToOne的VO属性
					CtxCacheFacade.addDtoAttr(artifactSpec, fatherSelectSpec.getTableName(), sonSelectSpec.getTableName(), sonOneToOne.getRefToSon(), MapperElm.SQL_SELECT, false);

					if (descOneToManyList != null) {

						for (int j = 0; j < descOneToManyList.size(); j++) {

							descOneToMany = descOneToManyList.get(j);

							// If there is oneToOne relationship inside 'descOneToMany', then handle oneToOne firstly.
							descSelectSpec = descOneToMany.getSelectSpec();
							descOneToOneList = descSelectSpec.getOneToOne();

							// 为DTO增加oneToMany的列表属性
							CtxCacheFacade.addDtoAttr(artifactSpec, fatherSelectSpec.getTableName(), descSelectSpec.getTableName(), descOneToMany.getListOfSon(),
									MapperElm.SQL_SELECT, true); // Done.

							if (descOneToOneList != null && descOneToOneList.size() != 0) {

								logger.debug("OTO in OTM: Found OTO in OTM, need build addtional " + "one to one statements.");

								// Use the mandatory 'id' for private 'select'.
								listOfSon = descOneToMany.getListOfSon(); // Done.
								selectId = MapperFormatter.getSelectIdOfInternalOtm(artifactSpec, daoSpec, descSelectSpec, listOfSon, false);

								// Use the mandatory 'id' for oneToOneResultMap.
								resultMapId = MapperFormatter.getResultMapIdOfInternalOtm(artifactSpec, daoSpec, descSelectSpec, listOfSon);
								buildSecondaryJoinStmt(mapper, selectId, resultMapId, artifactSpec, daoSpec, sonSelectSpec, descOneToMany);

							} else {

								buildInternalOtmResultMap(mapper, artifactSpec, daoSpec, descOneToMany);
								selectBuilder.buildInternalSelectOneTable(mapper, artifactSpec, daoSpec, fatherSelectSpec, descOneToMany);

							}

						}

					}

				}

			}

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	// private void cacheClassConst(ArtifactSpec artifactSpec, String
	// ancesClassName, OneToOneIdx descOtoIdx, String tableName, String
	// tableAlias)
	// throws AppException {
	//
	// try {
	//
	// String dtoDomainName = getClassDomainName(ancesClassName);
	// String constantName = null;
	// if (descOtoIdx != null) {
	// String attrNameChain = descOtoIdx.getAttrNameChain();
	// attrNameChain = StringUtils.replace(attrNameChain, JavaSrcElm.DOT,
	// JavaSrcElm.UNDER_LINE);
	// constantName = dtoDomainName + JavaSrcElm.UNDER_LINE + attrNameChain;
	// } else {
	// constantName = dtoDomainName;
	// }
	//
	// String constantValue = null;
	// if (StringUtils.isNotBlank(tableAlias) == true) {
	// constantValue = tableAlias;
	// } else {
	// constantValue = MapperFormatter.getTableShortName(tableName);
	// }
	//
	// logger.debug("CACHE CONST: " + "Prepare to cache '" + constantName + " =
	// " + constantValue + "'");
	//
	// // Get settings for ClassConstant.
	// AllArtifactSpec allArtifactSpec = artifactSpec.getAllArtifactSpec();
	// ComnArtifact comnArtifact = allArtifactSpec.getComnArtifact();
	// // String constPkg = comnArtifact.getConstPkg();
	// String classFullName = comnArtifact.getClassConst();
	//
	// ClassAnalyzer classAnalyzer = new ClassAnalyzer();
	// classAnalyzer.cacheConst(classFullName, constantName, constantValue);
	//
	// } catch (Throwable t) {
	// throw new AppException(t);
	// }
	//
	// }

	// private void cacheAttrConst(ArtifactSpec artifactSpec, String propName,
	// String columnName) throws AppException {
	//
	// try {
	//
	// // logger.debug("CACHE CONST: Prepare to cache '" + propName + " = "
	// // + columnName + "'");
	//
	// // Get settings for ClassConstant.
	// AllArtifactSpec allArtifactSpec = artifactSpec.getAllArtifactSpec();
	// ComnArtifact comnArtifact = allArtifactSpec.getComnArtifact();
	// String classFullName = comnArtifact.getAttrConst();
	//
	// ClassAnalyzer classAnalyzer = new ClassAnalyzer();
	// classAnalyzer.cacheConst(classFullName, propName, columnName);
	//
	// } catch (Throwable t) {
	// throw new AppException(t);
	// }
	//
	// }

	// private String getClassDomainName(String classFullName) {
	//
	// int rightFirst = classFullName.lastIndexOf(JavaSrcElm.DOT);
	// String dtoPkg = classFullName.substring(0, rightFirst);
	// String classSimpleName = classFullName.substring(rightFirst + 1,
	// classFullName.length());
	//
	// // Skip 'dto' in package name.
	// int rightSecond = dtoPkg.lastIndexOf(JavaSrcElm.DOT);
	// String domainPkg = classFullName.substring(0, rightSecond);
	// int rightThird = domainPkg.lastIndexOf(JavaSrcElm.DOT);
	// String domainName = domainPkg.substring(rightThird + 1,
	// domainPkg.length());
	//
	// // Return domainName and shortName.
	// return domainName + JavaSrcElm.UNDER_LINE + classSimpleName;
	//
	// }

}