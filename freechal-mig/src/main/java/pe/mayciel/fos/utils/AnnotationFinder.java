package pe.mayciel.fos.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 클래스, 메소드에 선언되어 있는 annotation 을 찾아주는 클래스.<br>
 * 한번 찾은 annotation 정보는 메모리 캐싱된다.
 * @author Hwang Seong-wook
 * @since 2013. 01. 04.
 * @version 1.0.0.1 (2013. 01. 04.)
 */
public class AnnotationFinder {
	private static Logger logger = LoggerFactory.getLogger(AnnotationFinder.class);
	private static Map<String, Annotation> annotations = Collections.synchronizedMap(new HashMap<String, Annotation>());

	/**
	 * class 에 선언된 annotation 을 반환한다.<br>
	 * 현재 클래스에 annotation 이 없다면 상위 클래스로 올라가서 찾는다.
	 * 
	 * @param <T>
	 * @param annotationClass
	 *            찾으려는 annotation class
	 * @param targetClass
	 *            annotation 을 찾는 대상 class
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(
		Class<T> annotationClass, Class<?> targetClass) {
		if (null == annotationClass || null == targetClass) {
			return null;
		}
		String key = getKey(annotationClass, targetClass, null);
		T annotation;
		if (annotations.containsKey(key)) {
			annotation = annotationClass.cast(annotations.get(key));
		} else {
			annotation = targetClass.getAnnotation(annotationClass);
			if (null == annotation && null != targetClass.getSuperclass()) {
				annotation = getAnnotation(annotationClass,
					targetClass.getSuperclass());
			}
			annotations.put(key, annotation);
		}
		return annotation;
	}

	/**
	 * class 의 method 에 선언된 annotation 을 반환한다.
	 * 
	 * @param <T>
	 * @param annotationClass
	 *            찾으려는 annotation class
	 * @param targetClass
	 *            annotation 을 찾는 대상 class
	 * @param methodNm
	 *            annotation 을 찾는 대상 method 이름
	 * @param parameterTypes
	 *            찾는 메소드의 파라메터 타입 클래스 정보들
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(
		Class<T> annotationClass, Class<?> targetClass, String methodNm,
		Class<?>... parameterTypes) {
		if (null == annotationClass || null == targetClass) {
			return null;
		}
		String key = getKey(annotationClass, targetClass, methodNm);
		T annotation = null;
		if (annotations.containsKey(key)) {
			annotation = annotationClass.cast(annotations.get(key));
		} else {
			try {
				annotation = ClassUtil.getMethod(targetClass, methodNm,
					parameterTypes).getAnnotation(annotationClass);
				annotations.put(key, annotation);
			} catch (NoSuchMethodException e) {
				logger.error("No method!", e);
				annotations.put(key, null);
			}
		}
		return annotation;
	}

	/**
	 * targetClass 에 선언된 메소드 중, annotationClass 가 부여된 메소드 목록을 반환한다.<br>
	 * 단, 상위 클래스의 메소드는 검색하지 않는다.
	 * 
	 * @param annotationClass
	 *            검색할 대상 어노테이션 클래스
	 * @param targetClass
	 *            검색할 대상 클래스
	 * @return
	 */
	public static List<Method> getAnnotationedMethods(
		Class<? extends Annotation> annotationClass, Class<?> targetClass) {
		List<Method> result = new ArrayList<Method>();
		for (Method method : targetClass.getDeclaredMethods()) {
			if (null != method.getAnnotation(annotationClass)) {
				result.add(method);
			}
		}
		return result;
	}

	/**
	 * targetClass 에 선언된 필드 중, annotationClass 가 부여된 필드 목록을 반환한다.<br>
	 * 단, 상위 클래스의 필드는 검색하지 않는다.
	 * 
	 * @param annotationClass
	 *            검색할 대상 어노테이션 클래스
	 * @param targetClass
	 *            검색할 대상 클래스
	 * @return
	 */
	public static List<Field> getAnnotationedFields(
		Class<? extends Annotation> annotationClass, Class<?> targetClass) {
		List<Field> result = new ArrayList<Field>();
		for (Field field : targetClass.getDeclaredFields()) {
			if (null != field.getAnnotation(annotationClass)) {
				result.add(field);
			}
		}
		return result;
	}

	/**
	 * annotation, class, methodNm 으로 키를 생성한다.
	 * 
	 * @param <T>
	 * @param annotationClass
	 * @param clazz
	 * @param methodNm
	 * @return
	 */
	private static String getKey(Class<? extends Annotation> annotationClass,
		Class<?> clazz, String methodNm) {
		StringBuilder sb = new StringBuilder();
		sb.append(annotationClass.getName()).append(",").append(clazz.getName());
		if (StringUtils.isNotBlank(methodNm)) {
			sb.append(".").append(methodNm);
		}
		return sb.toString();
	}
}