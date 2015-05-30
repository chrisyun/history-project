package org.thorn.dao.mybatis.annotation;

import java.util.HashMap;
import java.util.Map;

import org.thorn.core.util.LocalStringUtils;

public class MapperUtils {
	
	private static Map<String, Mapper> annotation = new HashMap<String, Mapper>();
	
	private static Map<MethodType, String> default_Method_Map = new HashMap<MethodType, String>();
	
	static {
		default_Method_Map.put(MethodType.INSERT, "save");
		default_Method_Map.put(MethodType.UPDATE, "modify");
		default_Method_Map.put(MethodType.DELETE, "delete");
		default_Method_Map.put(MethodType.DELETE_BATCH, "deleteForBatch");
		default_Method_Map.put(MethodType.QUERY, "query");
		default_Method_Map.put(MethodType.QUERY_LIST, "queryForList");
//		default_Method_Map.put(MethodType.QUERY_PAGE, "queryForPage");
		default_Method_Map.put(MethodType.COUNT, "countForPage");
	}
	
	
	public static String getMapperSource(Class<? extends Object> obj, MethodType type) {
		
		Mapper mapper = MapperUtils.getFormAnnotationMap(obj);
		
		String nameSpace = mapper.nameSpace();
		String nodeId = "";
		
		for(MapperNode node : mapper.node()) {
			
			if(node.type() == type) {
				nodeId = node.id();
				break;
			}
		}
		
		// get default value
		if(LocalStringUtils.isEmpty(nodeId)) {
			nodeId = default_Method_Map.get(type);
		}
		
		return nameSpace + "." + nodeId;
	}
	
	private static Mapper getFormAnnotationMap(Class<? extends Object> obj) {

		String class_name = obj.getName();

		Mapper mapper = annotation.get(class_name);

		if (mapper == null) {
			// first load
			addAnnotationMap(obj);

			mapper = annotation.get(class_name);
		}

		return mapper;
	}
	
	private static void addAnnotationMap(Class<? extends Object> obj) {

		if (!obj.isAnnotationPresent(Mapper.class)) {
			return;
		}

		Mapper mapper = (Mapper) obj.getAnnotation(Mapper.class);
		
		String class_name = obj.getName();

		synchronized (mapper) {
			annotation.put(class_name, mapper);
		}
	}
	
}

