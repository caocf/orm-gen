package bh.toolkit.srcgen.builder;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;
import bh.toolkit.srcgen.lang.GlobalConst;
import bh.toolkit.srcgen.model.artifact.ArtifactSpec;
import bh.toolkit.srcgen.model.artifact.DaoSpec;
import bh.toolkit.srcgen.model.mybatis.Mapper;
import bh.toolkit.srcgen.util.ExceptionUtil;
import bh.toolkit.srcgen.util.JavaFormatter;

public class MapperBuilder {

	private static Logger logger = Logger.getLogger(MapperBuilder.class);

	private static MapperBuilder mapperBuilder;
	private Marshaller mapperMarshaller;
	private Unmarshaller mapperUnMarshaller;
	private bh.toolkit.srcgen.model.artifact.ObjectFactory artifactObjFactory;
	private bh.toolkit.srcgen.model.mybatis.ObjectFactory mapperObjFactory;

	public synchronized static MapperBuilder getInstance() throws AppException {

		try {
			if (mapperBuilder == null) {
				mapperBuilder = new MapperBuilder();
			}
		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mapperBuilder;

	}

	private MapperBuilder() throws AppException {

		try {

			JAXBContext mapperJC = JAXBContext.newInstance(bh.toolkit.srcgen.model.mybatis.ObjectFactory.class);
			mapperMarshaller = mapperJC.createMarshaller();
			mapperMarshaller.setProperty(Marshaller.JAXB_ENCODING, GlobalConst.VAL_DATA_ENCODING);
			mapperMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			mapperUnMarshaller = mapperJC.createUnmarshaller();
			artifactObjFactory = new bh.toolkit.srcgen.model.artifact.ObjectFactory();
			mapperObjFactory = new bh.toolkit.srcgen.model.mybatis.ObjectFactory();

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

	}

	public String marshalMapper(Mapper mapper) throws AppException {

		StringWriter writer = new StringWriter();
		try {
			JAXBElement<Mapper> xmlRootElement = mapperObjFactory.createMapper(mapper);
			mapperMarshaller.marshal(xmlRootElement, writer);
		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return writer.toString();

	}

	@SuppressWarnings("unchecked")
	public Mapper unMarshalMapper(InputStream is) throws AppException {

		Mapper mapper = null;
		try {
			JAXBElement<Mapper> xmlRootElement = (JAXBElement<Mapper>) mapperUnMarshaller.unmarshal(is);
			mapper = (Mapper) xmlRootElement.getValue();
		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mapper;

	}

	public Mapper buildMapper(ArtifactSpec artifactSpec, DaoSpec daoSpec) throws AppException {

		Mapper mapper = null;

		try {

			// Initiate a blank Mapper.
			mapper = mapperObjFactory.createMapper();

			// 根据MyBatis要求，设置Mapper的命名空间为DAO的全名
			String mapperNs = JavaFormatter.getDaoFullName(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), daoSpec.getShortName());
			mapper.setNamespace(mapperNs);

			// Create Mapper's all ResultMap.
			ResultMapBuilder resultMapBuilder = new ResultMapBuilder(artifactObjFactory, mapperObjFactory);
			resultMapBuilder.buildAllResultMap(mapper, artifactSpec, daoSpec);

			// Create Mapper's all Insert.
			InsertBuilder insertBuilder = new InsertBuilder(mapperObjFactory);
			insertBuilder.buildAllInsert(mapper, artifactSpec, daoSpec);

			// Create Mapper's all Update.
			UpdateBuilder updateBuilder = new UpdateBuilder(mapperObjFactory);
			updateBuilder.buildAllUpdate(mapper, artifactSpec, daoSpec);

			// Create Mapper's all Delete
			DeleteBuilder deleteBuilder = new DeleteBuilder(mapperObjFactory);
			deleteBuilder.buildAllDelete(mapper, artifactSpec, daoSpec);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mapper;
	}

	public Mapper buildMapperx(ArtifactSpec artifactSpec, DaoSpec daoSpec) throws AppException {

		Mapper mapperx = null;

		try {

			// Initiate a blank Mapper.
			mapperx = mapperObjFactory.createMapper();

			// 根据MyBatis要求，设置Mapper的命名空间为DAO的全名
			String mapperNs = JavaFormatter.getDaoXFullName(artifactSpec.getCommonAttrSpec().getWorkspaceSpec(), daoSpec.getShortName());
			mapperx.setNamespace(mapperNs);

		} catch (Throwable t) {
			ExceptionUtil.handleException(t, logger);
		}

		return mapperx;
	}

}
