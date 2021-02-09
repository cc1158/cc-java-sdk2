package com.cc.sdk2.jsdk.commons.jdbc;

import com.cc.jsdk.utility.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/30 14:58
 **/
public class OrderByHelper {

    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("([-+])(\\S+)");

    public static String getSQLOrderBy(List<String> orderByList, Class pojoClazz) {
        if (orderByList == null || orderByList.size() == 0) {
            return "";
        }
        Field[] fields = pojoClazz == null ? new Field[0] : pojoClazz.getDeclaredFields();
        StringBuilder ret = new StringBuilder();
        ret.append(" ORDER BY ");

        int i = 0;
        for (String orderBy : orderByList) {
            Matcher matcher = ORDER_BY_PATTERN.matcher(orderBy);
            if (matcher.matches()) {
                String sign = matcher.group(1) == null ? "+" : matcher.group(1);
                String field = matcher.group(2);
                String columnName = null;
                if (!StringUtils.isNullOrEmpty(field)) {
                    for (Field f : fields) {
                        if (f.getName().equals(field)) {
                            TColumn tColumn = f.getDeclaredAnnotation(TColumn.class);
                            if (tColumn != null) {
                                columnName = tColumn.value();
                            }
                            break;
                        }
                    }
                    if (columnName != null) {
                        ret.append(columnName);
                    } else {
                        ret.append(field);
                    }
                    if (sign.equals("-")) {
                        ret.append(" DESC ");
                    } else {
                        ret.append(" ASC ");
                    }

                }
                i++;
            }
        }
        if (i > 0) {
            return ret.toString();
        }
        return "";

    }

}
