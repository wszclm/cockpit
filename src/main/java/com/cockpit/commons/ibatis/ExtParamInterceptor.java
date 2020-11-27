package com.cockpit.commons.ibatis;


import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

@Intercepts({@org.apache.ibatis.plugin.Signature(type=StatementHandler.class, method="prepare", args={java.sql.Connection.class,Integer.class})})
public class ExtParamInterceptor implements Interceptor {

    private static Logger log = LoggerFactory.getLogger(ExtParamInterceptor.class);
    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {

        StatementHandler statementHandler;
        MetaObject metaStatementHandler;
        StringBuffer sb = new StringBuffer();
        String sql;
        Object constans;
        String BOUNDSQL_SQL = "delegate.boundSql.sql";
        boolean result;
        try {
            statementHandler = (StatementHandler) getTarget(invocation);
            metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            sql = (String) metaStatementHandler.getValue(BOUNDSQL_SQL) + " ";
            sql = sql.replaceAll("\n", "");		//消除换行
            if(!sql.contains("constant:") ) {
                return invocation.proceed();
            }
            Pattern pattern = Pattern.compile(".+?constant:\\[(.+?)\\].+?", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sql);
            result = matcher.find();
            if (!result) {
                return invocation.proceed();
            }
            while (result) {
                constans = this.getConstantValue(matcher.group(1));
                if (constans == null) {
                    sb.append(matcher.group(0).replace(matcher.group(1), null));
                    continue;
                } else if (constans instanceof String) {
                    constans = "'" + constans + "'";
                } else if (constans instanceof List) {
                    constans = this.listToString((List<?>) constans);
                } else if (constans.getClass().isArray()) {
                    constans = this.arrayToString((Object[]) constans);
                }

                matcher.appendReplacement(sb, matcher.group(0).replaceFirst("(?i)constant:\\[" +matcher.group(1) + "\\]", constans.toString()));
                result = matcher.find();
            }
            matcher.appendTail(sb);
            metaStatementHandler.setValue(BOUNDSQL_SQL, sb.toString());		//修改sql
            return invocation.proceed();
        } finally {
            sql = null;
            statementHandler = null;
            metaStatementHandler = null;
        }
    }


    private Object getObject(Object target)
    {
        if (Proxy.isProxyClass(target.getClass()))
        {
            try {
                target = FieldUtils.readDeclaredField(Proxy.getInvocationHandler(target), "target", true);
            } catch (IllegalAccessException e) {
                return null;
            }
            if (Proxy.isProxyClass(target.getClass())) target = getObject(target);
        }
        return target;
    }


    private String arrayToString(Object[] obj) {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                sb.append("'" + obj[i] + "'");

                if (obj.length -1 != i && obj[i + 1] != null) {
                    sb.append(",");
                }
            }
        }

        sb.append(")");
        return sb.toString();
    }

    private String listToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (int i = 0; i < list.size(); i++) {
            sb.append("'" + list.get(i) + "'");
            if (list.size() -1 != i) {
                sb.append(",");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * 获取静态变量值
     * @param path
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws Exception
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws Throwable
     */
    private Object getConstantValue(String path) throws ClassNotFoundException, NoSuchFieldException, Exception
    {
        if (path == null || path.length() < 1 || (path.indexOf(".") == -1 && path.indexOf("cmdb:") == -1))
        {
            return null;
        }
        {
            ClassLoader c = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = c.loadClass(this.getClassPath(path));
            Field field = clazz.getField(this.getFieldName(path));
            return field.get(null);
        }

    }

    /**
     * 获取类路径
     * @param path
     * @return
     */
    private String getClassPath(String path) {
        return path.substring(0, path.lastIndexOf("."));
    }

    /**
     * 获取方法名
     * @param path
     * @return
     */
    private String getFieldName(String path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length());
    }



    public Object getTarget(Invocation invocation)
    {
        return getObject(invocation.getTarget());
    }

    @Override
    public Object plugin(Object o)
    {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties)
    {

    }

}
