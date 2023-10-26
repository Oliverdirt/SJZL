package com.ctsi.ssdc.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;



import static com.ctsi.ssdc.consts.ComponentConstant.UTIL;

/**
 * 实现两个实体类(实体名称不相同，但是属性相同)属性之间的复制
 * @author fubo
 * @version 1.0.1
 * @Copyright 中国电信集团系统集成有限责任公司
 */
public class CopyUtil {

	/**
	 * 实现两个实体类(实体名称不相同，但是属性相同)之间的复制，浅拷贝
	 * @param source 被克隆对象
	 * @param dest  目标对象
	 * @throws Exception 复制属性值发生的异常
	 */
	

	public static void copy(Object source, Object dest) throws Exception {
		// 获取属性
		BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),
				Object.class);
		PropertyDescriptor[] sourceProperty = sourceBean
				.getPropertyDescriptors();

		BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),
				Object.class);
		PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

		try {
			for (int i = 0; i < sourceProperty.length; i++) {
				for (int j = 0; j < destProperty.length; j++) {
					if (sourceProperty[i].getName().equals(
							destProperty[j].getName())) {
						// 调用source的getter方法和dest的setter方法
						destProperty[j].getWriteMethod().invoke(dest,sourceProperty[i].getReadMethod().invoke(source));
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("属性复制失败:" + e.getMessage());
		}
	}
	
	/**
     * 通过序列化与反序列化，实现两个实体类（相同属性名与类型）之间的深拷贝
     * @param source 被克隆对象
     * @param target 目标对象
     */
	

	public static void deepCopy(Object source, Object target) {
		//BeanCopier相同属性之间的克隆
		BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), true);
		copier.copy(source, target, new Converter() {
			@Override
			public Object convert(Object obj, Class arg1, Object arg2) {
				if (obj == null) {
					return null;
				}else if(obj.getClass()!=arg1) {
					//类型不一致不拷贝
					return null;
				}else if (arg1 instanceof Serializable) {
					//利用序列化拷贝属性
					ObjectOutputStream obs = null;
					ObjectInputStream ois = null;
					try {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						obs = new ObjectOutputStream(out);
						obs.writeObject(obj);
						ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
						ois = new ObjectInputStream(ios);
						// 返回生成的新对象
						Object cloneObj = ois.readObject();
						return cloneObj;
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					} finally {
						//关闭
						if (obs != null) {
							try {
								obs.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (ois != null) {
							try {
								ois.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return obj;
			}
		});
	}
	
}
