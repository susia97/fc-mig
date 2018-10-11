package pe.mayciel.fos.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.Test;

import pe.mayciel.freechal.domain.FreechalArtcl;

/**
 * 입력받은 도메인에 해당하는 기본적인 select, insert, update 용 ibatis 맵을 생성해 주는 클래스.<br>
 * 생성을 원하지 않는 field 에는 {@link NotColumn} annotation 을 붙일 것.
 * 
 * @author Hwang Seong-wook
 * @since 2013. 01. 07.
 * @version 1.0.0.2 (2013. 01. 10.)
 */
public class GenerateSqlMap {
	@Test
	public void t() {
		System.out.println(generate(FreechalArtcl.class, "FreechalArtcl"));
	}

	/**
	 * domainClass, tableName 에 해당하는 select, insert, update 용 ibatis 맵을 생성하여
	 * 반환한다.<br>
	 * classAlias 는 자동으로 생성된다.
	 * 
	 * @param domainClass
	 * @param tableName
	 * @return
	 */
	public static String generate(Class<?> domainClass, String tableName) {
		return generate(domainClass, tableName,
				NodeNameConvertor.toCamel(domainClass.getSimpleName()));
	}

	/**
	 * domainClass, tableName 에 해당하는 select, insert, update 용 ibatis 맵을 생성하여
	 * 반환한다.
	 * 
	 * @param domainClass
	 * @param tableName
	 * @param classAlias
	 * @return
	 */
	public static String generate(Class<?> domainClass, String tableName,
			String classAlias) {
		return generate(domainClass, tableName, classAlias, 4, true);
	}

	/**
	 * domainClass, tableName 에 해당하는 select, insert, update 용 ibatis 맵을 생성하여
	 * 반환한다.
	 * 
	 * @param domainClass
	 * @param tableName
	 * @param classAlias
	 * @param tabSize
	 *            탭의 길이
	 * @param useMap
	 *            select 시에 resultMap 을 사용할지 여부
	 * @return
	 */
	public static String generate(Class<?> domainClass, String tableName,
			String classAlias, int tabSize, boolean useMap) {
		StringBuilder map = new StringBuilder();
		if (null != classAlias) {
			map.append("\t<typeAlias alias=\"").append(classAlias)
					.append("\" type=\"").append(domainClass.getName())
					.append("\"/>\n");
		} else {
			classAlias = domainClass.getName();
		}
		List<Field> fields = ClassUtil.getAllFields(domainClass);
		if (useMap) {
			addResultMap(map, classAlias, tabSize, fields);
			addSelectQuery(map, classAlias, tableName, fields);
		} else {
			addSelectQueryWithoutMap(map, classAlias, tableName, fields);
		}
		addInsertQuery(map, classAlias, tableName, fields);
		addUpdateQuery(map, classAlias, tableName, fields);
		return map.toString();
	}

	/**
	 * resultMap 을 추가한다.
	 * 
	 * @param map
	 * @param classAlias
	 * @param tabSize
	 * @param fields
	 */
	private static void addResultMap(StringBuilder map, String classAlias,
			int tabSize, List<Field> fields) {
		map.append("\t<resultMap class=\"").append(classAlias)
				.append("\" id=\"").append(classAlias).append("Map\">\n");
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			map.append("\t\t<result property=\"").append(field.getName())
					.append("\"");
			addTabByFieldLength(map, tabSize, field.getName().length());
			map.append("column=\"")
					.append(NodeNameConvertor.toUnderscore(field.getName()))
					.append("\"/>\n");
		}
		map.append("\t</resultMap>\n");
	}

	/**
	 * select 구문을 추가한다.
	 * 
	 * @param map
	 * @param classAlias
	 * @param tableName
	 * @param fields
	 */
	private static void addSelectQuery(StringBuilder map, String classAlias,
			String tableName, List<Field> fields) {
		map.append("\t<select id=\"get")
				.append(NodeNameConvertor.toPascal(classAlias))
				.append("\" parameterClass=\"").append(classAlias)
				.append("\" resultMap=\"").append(classAlias)
				.append("Map\">\n");
		map.append("\t\tSELECT ");
		int i = 0;
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			map.append(NodeNameConvertor.toUnderscore(field.getName()));
			i++;
		}
		map.append("\n\t\tFROM ").append(tableName).append("\n");
		map.append("\t</select>\n");
	}

	/**
	 * map 이 필요없는 select 구문을 추가한다.
	 * 
	 * @param map
	 * @param classAlias
	 * @param tableName
	 * @param fields
	 */
	private static void addSelectQueryWithoutMap(StringBuilder map,
			String classAlias, String tableName, List<Field> fields) {
		map.append("\t<select id=\"get")
				.append(NodeNameConvertor.toPascal(classAlias))
				.append("\" parameterClass=\"").append(classAlias)
				.append("\" resultClass=\"").append(classAlias).append("\">\n");
		map.append("\t\tSELECT ");
		int i = 0;
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			map.append(NodeNameConvertor.toUnderscore(field.getName()))
					.append(" AS ").append(field.getName());
			i++;
		}
		map.append("\n\t\tFROM ").append(tableName).append("\n");
		map.append("\t</select>\n");
	}

	/**
	 * insert 구문을 추가한다.
	 * 
	 * @param map
	 * @param classAlias
	 * @param tableName
	 * @param fields
	 */
	private static void addInsertQuery(StringBuilder map, String classAlias,
			String tableName, List<Field> fields) {
		map.append("\t<insert id=\"insert")
				.append(NodeNameConvertor.toPascal(classAlias))
				.append("\" parameterClass=\"").append(classAlias)
				.append("\">\n");
		map.append("\t\tINSERT INTO ").append(tableName).append(" (\n\t\t\t");
		int i = 0;
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			map.append(NodeNameConvertor.toUnderscore(field.getName()));
			i++;
		}
		map.append("\n\t\t) VALUES (\n\t\t\t");

		i = 0;
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			map.append("#").append(field.getName()).append("#");
			i++;
		}
		map.append("\n\t\t)\n");
		map.append("\t</insert>\n");
	}

	/**
	 * update 구문을 추가한다.
	 * 
	 * @param map
	 * @param classAlias
	 * @param tableName
	 * @param fields
	 */
	private static void addUpdateQuery(StringBuilder map, String classAlias,
			String tableName, List<Field> fields) {
		map.append("\t<update id=\"update")
				.append(NodeNameConvertor.toPascal(classAlias))
				.append("\" parameterClass=\"").append(classAlias)
				.append("\">\n");
		map.append("\t\tUPDATE ").append(tableName).append(" SET\n\t\t\t");
		int i = 0;
		for (Field field : fields) {
			if (isSkipField(field)) {
				continue;
			}
			addSeparator(map, i);
			map.append(NodeNameConvertor.toUnderscore(field.getName()))
					.append(" = #").append(field.getName()).append("#");
			i++;
		}
		map.append("\n\t\tWHERE \n");
		map.append("\t</update>\n");
	}

	/**
	 * field 가 query 생성에서 제외될 field 인지 여부를 반환한다.<br>
	 * {@link NotColumn} 이 선언되어 있거나, final 필드이면 제외한다.
	 * 
	 * @param field
	 * @return
	 */
	private static boolean isSkipField(Field field) {
		return null != field.getAnnotation(NotColumn.class)
				|| Modifier.isFinal(field.getModifiers());
	}

	/**
	 * 필드의 이름 길이에 따라 적절한 갯수의 탭을 추가한다.
	 * 
	 * @param map
	 * @param tabSize
	 * @param length
	 */
	private static void addTabByFieldLength(StringBuilder map, int tabSize,
			int length) {
		if (length > tabSize * 3) {
			map.append("\t");
			return;
		}
		if (length > tabSize * 2) {
			map.append("\t\t");
			return;
		}
		if (length > tabSize) {
			map.append("\t\t\t");
			return;
		}
		map.append("\t\t\t\t");
	}

	/**
	 * idx 에 따라 쉼표와 띄어쓰기, 줄바꿈 처리를 한다.
	 * 
	 * @param map
	 * @param idx
	 */
	private static void addSeparator(StringBuilder map, int idx) {
		if (idx == 0) {
			return;
		}
		map.append(",");
		if (idx % 5 == 0) {
			map.append("\n\t\t\t");
		} else {
			map.append(" ");
		}
	}
}