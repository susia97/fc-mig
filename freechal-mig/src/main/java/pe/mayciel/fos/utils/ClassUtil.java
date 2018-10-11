package pe.mayciel.fos.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class 의 method, field 등의 조회 및 실행, 설정을 위한 utility class.
 * @author Hwang Seong-wook
 * @since 2013. 01. 04.
 * @version 1.0.0.1 (2013. 01. 04.)
 */
public class ClassUtil {
	private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * paramName 의 이름을 가진 파라메터의 getter/setter method 이름을 반환한다.
	 * 
	 * @param prefix
	 *            get/set
	 * @param paramName
	 * @return getter/setter method name.
	 */
	public static String getMethodName(String prefix, String paramName) {
		if (StringUtils.isBlank(prefix) || StringUtils.isBlank(paramName)) {
			return "";
		}
		String headChar = paramName.substring(0, 1).toUpperCase();
		return prefix + headChar + paramName.substring(1);
	}

	/**
	 * targetClass 클래스 안에 있는 methodName 인 이름의 method 를 반환한다.<br>
	 * 찾으려는 method 가 parameter 를 받는 경우, parameterTypes 를 꼭 입력하여야 한다.<br>
	 * 해당 클래스에서 선언한 method 와 상속받은 모든 상위 클래스의 method 를 찾을 수 있다.<br>
	 * 문제 발생 시 NoSuchMethodException 을 던진다
	 * 
	 * @param targetClass
	 *            메소드를 뽑아 낼 대상 클래스
	 * @param methodName
	 *            메소드의 이름
	 * @param parameterTypes
	 *            찾는 메소드의 파라메터 타입 클래스 정보들
	 * @return
	 * @throws NoSuchMethodException
	 *             메소드를 찾지 못하면 발생
	 */
	public static Method getMethod(Class<?> targetClass, String methodName,
		Class<?>... parameterTypes) throws NoSuchMethodException {
		if (null == targetClass) {
			throw new NoSuchMethodException("TargetClass is null!");
		}
		if (StringUtils.isBlank(methodName)) {
			throw new NoSuchMethodException("MethodName is blank!");
		}
		Class<?> clazz = targetClass;
		Method result = null;
		while (null != clazz) {
			try {
				result = clazz.getDeclaredMethod(methodName, parameterTypes);
				clazz = null;
			} catch (NoSuchMethodException e) {
				clazz = clazz.getSuperclass();
			}
		}
		if (null == result) {
			throw new NoSuchMethodException("Can't find such method! Class : "
				+ targetClass + ", methodName : " + methodName);
		}
		return result;
	}

	/**
	 * targetClass 클래스 및 상속받은 모든 상위 클래스의 method list 를 반환한다.<br>
	 * 대상 클래스가 null 이면 빈 목록을 반환한다.
	 * 
	 * @param targetClass
	 *            메소드 목록을 얻을 대상 클래스
	 * @return
	 */
	public static List<Method> getAllMethods(Class<?> targetClass) {
		List<Method> result = new ArrayList<Method>();
		while (null != targetClass) {
			Method[] arr = targetClass.getDeclaredMethods();
			if (null != arr) {
				for (Method method : arr) {
					result.add(method);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		return result;
	}

	/**
	 * targetObj 객체의 methodName 인 메소드를 실행 후 결과를 반환한다.<br>
	 * 실행하려는 메소드가 파라메터를 받는 경우, parameter 값을 꼭 입력하여야 한다.<br>
	 * parameter 값이 null 인 경우는 실행이 되지 않으므로(메소드를 찾을 수 없다),<br>
	 * getMethod 로 메소드를 찾은 후 직접 invoke 하도록 한다.
	 * 
	 * @param targetObj
	 *            실행할 메소드가 있는 대상 객체
	 * @param methodName
	 *            실행할 메소드 이름
	 * @param parameters
	 *            메소드 실행 시 전달할 파라메터(없으면 입력 안해도 됨, null 입력불가)
	 * @return
	 * @throws NoSuchMethodException
	 *             메소드를 찾지 못했을 경우 발생시킴
	 * @throws InvocationTargetException
	 *             invoke 실행 중 발생
	 * @throws IllegalAccessException
	 *             invoke 실행 중 발생
	 * @throws IllegalArgumentException
	 *             invoke 실행 중 발생
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object targetObj, String methodName,
		Object... parameters) throws NoSuchMethodException,
		IllegalArgumentException,
		IllegalAccessException,
		InvocationTargetException {
		if (null == targetObj) {
			throw new NoSuchMethodException("TargetObj is null!");
		}
		return (T)invoke(targetObj.getClass(), targetObj, methodName,
			parameters);
	}

	/**
	 * targetClass 의 methodName 인 메소드를 실행 후 결과를 반환한다.<br>
	 * static method 만 이 방식으로 실행하여아 하고,<br>
	 * static 이 아니면 invoke 메소드를 이용하도록 한다.<br>
	 * <br>
	 * 실행하려는 메소드가 파라메터를 받는 경우, parameter 값을 꼭 입력하여야 한다.<br>
	 * parameter 값이 null 인 경우는 실행이 되지 않으므로(메소드를 찾을 수 없다),<br>
	 * getMethod 로 메소드를 찾은 후 직접 invoke 하도록 한다.
	 * 
	 * @param targetClass
	 *            실행할 메소드가 있는 대상 클래스
	 * @param methodName
	 *            실행할 메소드 이름
	 * @param parameters
	 *            메소드 실행 시 전달할 파라메터(없으면 입력 안해도 됨, null 입력불가)
	 * @return
	 * @throws NoSuchMethodException
	 *             메소드를 찾지 못했을 경우 발생시킴
	 * @throws InvocationTargetException
	 *             invoke 실행 중 발생
	 * @throws IllegalAccessException
	 *             invoke 실행 중 발생
	 * @throws IllegalArgumentException
	 *             invoke 실행 중 발생
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeStatic(Class<?> targetClass, String methodName,
		Object... parameters) throws IllegalArgumentException,
		NoSuchMethodException,
		IllegalAccessException,
		InvocationTargetException {
		return (T)invoke(targetClass, targetClass, methodName, parameters);
	}

	/**
	 * targetClass 클래스 안에 있는 fieldName 인 field 를 반환한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 를 찾을 수 있다.<br>
	 * 문제 발생 시 NoSuchFieldException 을 던진다
	 * 
	 * @param targetClass
	 *            메소드를 뽑아 낼 대상 클래스
	 * @param fieldName
	 *            필드의 이름
	 * @return
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 */
	public static Field getField(Class<?> targetClass, String fieldName) throws NoSuchFieldException {
		if (null == targetClass) {
			throw new NoSuchFieldException("TargetClass is null!");
		}
		if (StringUtils.isBlank(fieldName)) {
			throw new NoSuchFieldException("FieldName is blank!");
		}
		Class<?> clazz = targetClass;
		Field result = null;
		while (null != clazz) {
			try {
				result = clazz.getDeclaredField(fieldName);
				clazz = null;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		if (null == result) {
			throw new NoSuchFieldException("Can't find such field! Class : "
				+ targetClass + ", Field name : " + fieldName);
		}
		return result;
	}

	/**
	 * targetClass 클래스 및 상속받은 모든 상위 클래스의 field list 를 반환한다.<br>
	 * 대상 클래스가 null 이면 빈 목록을 반환한다.
	 * 
	 * @param targetClass
	 *            필드 목록을 얻을 대상 클래스
	 * @return
	 */
	public static List<Field> getAllFields(Class<?> targetClass) {
		List<Field> result = new ArrayList<Field>();
		while (null != targetClass) {
			Field[] arr = targetClass.getDeclaredFields();
			if (null != arr) {
				for (Field field : arr) {
					result.add(field);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		return result;
	}

	/**
	 * targetObj 객체의 fieldName 인 field 의 값을 반환한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 의 값을 찾을 수 있다.
	 * 
	 * @param targetObj
	 *            값을 뽑아 낼 대상 객체
	 * @param fieldName
	 *            field 의 이름
	 * @return
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 * @throws IllegalArgumentException
	 *             필드 값을 얻는 중 발생
	 * @throws IllegalAccessException
	 *             필드 값을 얻는 중 발생
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object targetObj, String fieldName) throws NoSuchFieldException,
		IllegalArgumentException,
		IllegalAccessException {
		if (null == targetObj) {
			throw new NoSuchFieldException("TargetObj is null!");
		}
		return (T)getFieldValue(targetObj.getClass(), targetObj, fieldName);
	}

	/**
	 * targetClass 의 fieldName 인 field 의 값을 반환한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 의 값을 찾을 수 있다.<br>
	 * static 인 field 에 대해서만 사용하도록 한다.
	 * 
	 * @param targetClass
	 *            값을 뽑아 낼 대상 클래스
	 * @param fieldName
	 *            field 의 이름
	 * @return
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 * @throws IllegalArgumentException
	 *             필드 값을 얻는 중 발생
	 * @throws IllegalAccessException
	 *             필드 값을 얻는 중 발생
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getStaticFieldValue(Class<?> targetClass,
		String fieldName) throws NoSuchFieldException,
		IllegalArgumentException,
		IllegalAccessException {
		return (T)getFieldValue(targetClass, targetClass, fieldName);
	}

	/**
	 * targetObj 객체의 fieldName 인 field 의 값을 세팅한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 의 값을 세팅할 수 있다.
	 * 
	 * @param targetObj
	 *            field 값을 세팅 할 대상 객체
	 * @param fieldName
	 *            field 의 이름
	 * @param value
	 *            field 에 넣을 값
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 * @throws IllegalArgumentException
	 *             필드 값을 얻는 중 발생
	 * @throws IllegalAccessException
	 *             필드 값을 얻는 중 발생
	 */
	public static void setFieldValue(Object targetObj, String fieldName,
		Object value) throws IllegalArgumentException,
		NoSuchFieldException,
		IllegalAccessException {
		if (null == targetObj) {
			throw new NoSuchFieldException("TargetObj is null!");
		}
		setFieldValue(targetObj.getClass(), targetObj, fieldName, value);
	}

	/**
	 * targetClass 의 fieldName 인 field 의 값을 세팅한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 의 값을 세팅할 수 있다.<br>
	 * static 인 field 에 대해서만 사용하도록 한다.
	 * 
	 * @param targetObj
	 *            field 값을 세팅 할 대상 객체
	 * @param fieldName
	 *            field 의 이름
	 * @param value
	 *            field 에 넣을 값
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 * @throws IllegalArgumentException
	 *             필드 값을 얻는 중 발생
	 * @throws IllegalAccessException
	 *             필드 값을 얻는 중 발생
	 */
	public static void setStaticFieldValue(Class<?> targetClass,
		String fieldName, Object value) throws IllegalArgumentException,
		NoSuchFieldException,
		IllegalAccessException {
		setFieldValue(targetClass, targetClass, fieldName, value);
	}

	/**
	 * targetClass 의 static 한 Collection, Map 의 size 를 반환한다.<br>
	 * Collection, Map 이 아니면 -1 을 반환.
	 * 
	 * @param targetClass
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static int getStaticItemSize(Class<?> targetClass, String fieldName) throws IllegalArgumentException,
		NoSuchFieldException,
		IllegalAccessException {
		Object value = getStaticFieldValue(targetClass, fieldName);
		int result;
		if (value instanceof Collection<?>) {
			result = ((Collection<?>)value).size();
		} else if (value instanceof Map<?, ?>) {
			result = ((Map<?, ?>)value).size();
		} else {
			logger.error("Unknown class type... return -1");
			result = -1;
		}
		return result;
	}

	/**
	 * targetClass 의 static 한 Collection, Map 을 clear 하고,<br>
	 * clear 하기 전의 갯수를 반환한다.<br>
	 * Collection, Map 이 아니면 -1 를 반환.
	 * 
	 * @param targetClass
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static int clearStaticItem(Class<?> targetClass, String fieldName) throws IllegalArgumentException,
		NoSuchFieldException,
		IllegalAccessException {
		Object value = getStaticFieldValue(targetClass, fieldName);
		int result;
		if (value instanceof Collection<?>) {
			result = ((Collection<?>)value).size();
			((Collection<?>)value).clear();
		} else if (value instanceof Map<?, ?>) {
			result = ((Map<?, ?>)value).size();
			((Map<?, ?>)value).clear();
		} else {
			logger.error("Unknown class type... return -1");
			return -1;
		}
		return result;
	}

	/**
	 * className 에 해당하는 클래스를 T 로 캐스팅해서 반환한다.
	 * 
	 * @param <T>
	 *            캐스팅하려는 클래스
	 * @param className
	 *            클래스 이름
	 * @param clazz
	 *            캐스팅하려는 클래스 객체
	 * @return
	 * @throws ClassNotFoundException
	 *             className 에 해당하는 클래스가 없을 경우 발생
	 * @throws ClassCastException
	 *             T 로 캐스팅할 수 없을 때 발생
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(String className, Class<T> clazz) throws ClassNotFoundException,
		ClassCastException {
		try {
			return (Class<T>)Class.forName(className).asSubclass(clazz);
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (ClassCastException e) {
			throw e;
		}
	}

	/**************************************************
	 * 
	 * Private Methods
	 * 
	 **************************************************/

	/**
	 * targetClass 에 정의 된 targetObj 객체의 methodName 인 메소드를 실행 후 결과를 반환한다.<br>
	 * 실행하려는 메소드가 파라메터를 받는 경우, parameter 값을 꼭 입력하여야 한다.<br>
	 * parameter 값이 null 인 경우는 실행이 되지 않으므로(메소드를 찾을 수 없다),<br>
	 * getMethod 로 메소드를 찾은 후 직접 invoke 하도록 한다.
	 * 
	 * @param targetClass
	 *            실행할 메소드가 있는 대상 클래스
	 * @param targetObj
	 *            실행할 메소드가 있는 대상 객체
	 * @param methodName
	 *            실행할 메소드 이름
	 * @param parameters
	 *            메소드 실행 시 전달할 파라메터(없으면 입력 안해도 됨, null 입력불가)
	 * @return
	 * @throws NoSuchMethodException
	 *             메소드를 찾지 못했을 경우 발생시킴
	 * @throws InvocationTargetException
	 *             invoke 실행 중 발생
	 * @throws IllegalAccessException
	 *             invoke 실행 중 발생
	 * @throws IllegalArgumentException
	 *             invoke 실행 중 발생
	 */
	@SuppressWarnings("unchecked")
	private static <T> T invoke(Class<?> targetClass, Object targetObj,
		String methodName, Object... parameters) throws NoSuchMethodException,
		IllegalArgumentException,
		IllegalAccessException,
		InvocationTargetException {
		if (null == targetClass || null == targetObj) {
			logger.warn("TargetClass or TargetObj is null! return null.");
			return null;
		}
		Method method = null;
		Object result = null;
		if (null == parameters) {
			method = getMethod(targetClass, methodName);
		} else {
			int len = parameters.length;
			Class<?>[] parameterTypes = new Class[len];
			for (int i = 0; i < len; i++) {
				if (null == parameters[i]) {
					logger.warn("Parameter is null! Can't find method!");
					throw new NoSuchMethodException();
				}
				parameterTypes[i] = parameters[i].getClass();
			}
			method = getMethod(targetClass, methodName, parameterTypes);
		}
		if (null == method) {
			throw new NoSuchMethodException();
		}
		method.setAccessible(true);
		result = method.invoke(targetObj, parameters);
		return (T)result;
	}

	/**
	 * targetClass에 선언되어 있는 targetObj 객체 안에 있는 fieldName 인 field 의 값을 반환한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 의 값을 찾을 수 있다.
	 * 
	 * @param targetClass
	 *            field 를 찾을 대상 클래스
	 * @param targetObj
	 *            field 값을 뽑아 낼 대상 객체
	 * @param fieldName
	 *            field 의 이름
	 * @return
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 * @throws IllegalArgumentException
	 *             필드 값을 얻는 중 발생
	 * @throws IllegalAccessException
	 *             필드 값을 얻는 중 발생
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getFieldValue(Class<?> targetClass, Object targetObj,
		String fieldName) throws NoSuchFieldException,
		IllegalArgumentException,
		IllegalAccessException {
		if (null == targetClass) {
			throw new NoSuchFieldException("TargetClass is null!");
		}
		if (null == targetObj) {
			throw new NoSuchFieldException("TargetObj is null!");
		}
		Field field = getField(targetClass, fieldName);
		field.setAccessible(true);
		return (T)field.get(targetObj);
	}

	/**
	 * targetClass에 선언되어 있는 targetObj 객체 안에 있는 fieldName 인 field 의 값을 세팅한다.<br>
	 * 해당 클래스에서 선언한 field 와 상속받은 모든 상위 클래스의 field 의 값을 세팅할 수 있다.
	 * 
	 * @param targetClass
	 *            field 를 찾을 대상 클래스
	 * @param targetObj
	 *            field 값을 세팅 할 대상 객체
	 * @param fieldName
	 *            field 의 이름
	 * @param value
	 *            field 에 넣을 값
	 * @throws NoSuchFieldException
	 *             필드를 찾지 못했을 경우 발생시킴
	 * @throws IllegalArgumentException
	 *             필드 값을 얻는 중 발생
	 * @throws IllegalAccessException
	 *             필드 값을 얻는 중 발생
	 */
	private static void setFieldValue(Class<?> targetClass, Object targetObj,
		String fieldName, Object value) throws NoSuchFieldException,
		IllegalArgumentException,
		IllegalAccessException {
		if (null == targetClass) {
			throw new NoSuchFieldException("TargetClass is null!");
		}
		if (null == targetObj) {
			throw new NoSuchFieldException("TargetObj is null!");
		}
		Field field = getField(targetClass, fieldName);
		field.setAccessible(true);
		field.set(targetObj, value);
	}
}