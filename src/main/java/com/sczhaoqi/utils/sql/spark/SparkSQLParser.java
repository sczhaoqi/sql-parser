package com.sczhaoqi.utils.sql.spark;

import antlr4.spark.SqlBaseLexer;
import antlr4.spark.SqlBaseParser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author zhaoqi
 * @date 2021/6/8 4:54 下午
 */
@Slf4j
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class SparkSQLParser {

    public static void main(String[] args) throws Exception {
        String sqlText = "select name from student where dt=1";

        SqlBaseLexer lexer = new SqlBaseLexer(CharStreams.fromString(sqlText.toUpperCase()));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SqlBaseParser parser = new SqlBaseParser(tokenStream);

        SimpleSQLListener sqlListener = new SimpleSQLListener();
        SimpleErrorListener errorListener = new SimpleErrorListener();
        parser.addErrorListener(errorListener);
        parser.addParseListener(sqlListener);

        SqlBaseParser.SingleStatementContext statement = parser.singleStatement();
        SimpleSQLVisitor sqlVisitor = new SimpleSQLVisitor();
        String res = sqlVisitor.visit(statement);
        log.info("visit result: {}", res);

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(sqlListener, statement);
        log.info(JSON.toJSONString(sqlListener.getDbTableOperator()));
    }
}
