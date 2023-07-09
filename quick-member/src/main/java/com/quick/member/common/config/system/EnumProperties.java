package com.quick.member.common.config.system;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @ProjectName: agent-server
 * @Package: com.star.boss.agent.domain.utils
 * @ClassName: EnumProperties
 * @Author: Longcm
 * @Description: 获取枚举配置类文件的key value数据工具类
 * @Version: 1.0
 */
@PropertySource("classpath:config/enums.properties")
@Configuration
public class EnumProperties {
    public static final String BASE_PACKAGE = "basePackage";
    /**
     * 请求加载一次即可(存储枚举类路径即可，无需存储整个枚举类对象，减少非必要的内存开销)
     * key为枚举名称，value为枚举类全路径
     */
    public static volatile Map<String,String> enums;

    @Autowired
    private Environment environment;

    public String getValue(String key){
        return environment.getProperty(key);
    }

    public Map<String,String> scan() {
        String packages = getValue(BASE_PACKAGE);
        Assert.hasText(packages,"packages is empty");
        String[] ps = packages.split(";");
        Set<Class<? extends Enum>> enums = new HashSet<>();
        for (String pa : ps) {
            Set<Class<? extends Enum>> subTypesOf = new Reflections(pa).getSubTypesOf(Enum.class);
            enums.addAll(subTypesOf);
        }
        Map<String,String> map = new HashMap<>(enums.size());
        for (Class<? extends Enum> anEnum : enums) {
            map.put(anEnum.getSimpleName().toLowerCase(Locale.ROOT),anEnum.getName());
        }
        return map;
    }

    public Class getEnum(String className) throws ClassNotFoundException {
        if(enums==null){
            synchronized (EnumProperties.class){
                if(enums==null){
                    enums = scan();
                }
            }
        }
        String enumClass = enums.get(className);
        Assert.hasText(enumClass,"className "+className+"not found");
        return Class.forName(enumClass);
    }
}
