package com.sczhaoqi.utils.sql.spark;

import antlr4.spark.SqlBaseBaseListener;
import antlr4.spark.SqlBaseParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zhaoqi
 * @date 2021/6/9 1:49 下午
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Slf4j
public class SimpleSQLListener extends SqlBaseBaseListener {
    @Override
    public void enterSelectClause(SqlBaseParser.SelectClauseContext ctx) {
        super.enterSelectClause(ctx);
    }

    @Override
    public void enterColumnReference(SqlBaseParser.ColumnReferenceContext ctx) {
        super.enterColumnReference(ctx);

    }

    @Override
    public void enterTableName(SqlBaseParser.TableNameContext ctx) {
        super.enterTableName(ctx);
        if (Strings.isNotEmpty(ctx.getText())) {
            String table = ctx.getText().toLowerCase();
            Set<String> oper;
            if (dbTableOperator.containsKey(table)) {
                oper = dbTableOperator.get(table);
            } else {
                oper = new HashSet<String>();
            }
            dbTableOperator.put(table, oper);
        }
    }

    // 用来保存表与操作的对应关系
    private Map<String, Set<String>> dbTableOperator = new HashMap<String, Set<String>>();

    public Map<String, Set<String>> getDbTableOperator() {
        return dbTableOperator;
    }
}
