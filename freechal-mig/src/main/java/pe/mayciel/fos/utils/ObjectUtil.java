package pe.mayciel.fos.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 객체 복사 등의 작업을 위한 클래스.
 * @author Hwang Seong-wook
 * @since 2013. 01. 21.
 */
public class ObjectUtil {
	/**
	 * src 에 있는 필드 값을 같은 이름인 dest 필드에 넣는다. 
	 * @param src
	 * @param dest
	 */
	public static void copyObject(Object src, Object dest) {
		if (null == src || null == dest) {
			return;
		}
		for (Field destField : ClassUtil.getAllFields(dest.getClass())) {
			try {
				Field srcField = ClassUtil.getField(src.getClass(),
					destField.getName());
				if (srcField.getType().equals(destField.getType())) {
					srcField.setAccessible(true);
					destField.setAccessible(true);
					try {
						destField.set(dest, srcField.get(src));
					} catch (Exception e) {
						continue;
					}
				}
			} catch (NoSuchFieldException e) {
				continue;
			}
		}
	}

	/**
	 * src 에 있는 객체들의 필드 값을 destClass 에 해당하는 객체를 생성하여 담은 후,<br>
	 * list 에 담아서 반환한다.
	 * @param srcList
	 * @param destClass
	 * @return
	 */
	public static <T> List<T> copyList(List<?> srcList, Class<T> destClass) {
		List<T> result = new ArrayList<T>();
		for (Object src : srcList) {
			try {
				T dest = destClass.newInstance();
				copyObject(src, dest);
				result.add(dest);
			} catch (InstantiationException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		return result;
	}
}