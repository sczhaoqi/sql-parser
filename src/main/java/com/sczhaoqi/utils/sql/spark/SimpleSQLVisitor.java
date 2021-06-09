package com.sczhaoqi.utils.sql.spark;

import antlr4.spark.SqlBaseBaseVisitor;
import antlr4.spark.SqlBaseParser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaoqi
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Slf4j
public class SimpleSQLVisitor extends SqlBaseBaseVisitor<String> {

    @Override
    public String visitSelectClause(SqlBaseParser.SelectClauseContext ctx) {
        log.info("visit SELECTClause: {}", ctx.getText());
        return super.visitSelectClause(ctx);
    }

    @Override
    public String visitColumnReference(SqlBaseParser.ColumnReferenceContext ctx) {
        log.info("visit ColumnReference: {}", ctx.getText());
        return super.visitColumnReference(ctx);
    }

    @Override
    public String visitTableName(SqlBaseParser.TableNameContext ctx) {
        log.info("visit TableName: {}", ctx.getText());
        return super.visitTableName(ctx);
    }

    @Override
    public String visitWhereClause(SqlBaseParser.WhereClauseContext ctx) {
        log.info("visit WhereClause: {}", ctx.getText());
        return super.visitWhereClause(ctx);
    }
}