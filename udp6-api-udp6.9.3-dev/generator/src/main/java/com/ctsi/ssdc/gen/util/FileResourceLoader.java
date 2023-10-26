package com.ctsi.ssdc.gen.util;

import com.ctsi.ssdc.gen.constant.GenConstants;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ClassUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * velocity类加载器
 * <p>
 * The code change the world !!!
 *
 * @create 2022-04-19 15:26
 **/
public class FileResourceLoader extends ResourceLoader {

    @Override
    public void init(ExtendedProperties extendedProperties) {

    }

    @Override
    public InputStream getResourceStream(String source){
            File file = null;
            //自定义路径加载
            try {
                if(source.startsWith(GenConstants.VM)){
                    return ClassUtils.getResourceAsStream(this.getClass(), source);
                } else {
                    file = getResourceFile(source);
                    return new FileInputStream(file);
                }
            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException:" + source);
                return this.getClass().getResourceAsStream(source);
            }
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0L;
    }

    private File getResourceFile(String name) {
        return new File(name);
    }

}
