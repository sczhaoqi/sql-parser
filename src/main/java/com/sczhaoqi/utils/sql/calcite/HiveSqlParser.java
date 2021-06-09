package com.sczhaoqi.utils.sql.calcite;

import com.google.common.base.Joiner;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.calcite.sql.SqlKind.*;

/**
 * @author zhaoqi
 * @date 2021/6/9 4:07 下午
 */
public class HiveSqlParser {
    static Pattern pattern = Pattern.compile("(create table ([\\w\\.]+) as )([\\s\\S]+)");

    public static void main(String[] args) throws RelConversionException, SqlParseException, ValidationException {


        String test = "create table asf.afe as SELECT column_name ,adf FROM table1\n" +
                "UNION\n" +
                "SELECT column_name FROM table2 " + "UNION\n" +
                "SELECT column_name FROM table2";

        test = " select ids, name from a, c as sdf where a.id = b.id and a.name = 'zhang' ";

        List<String> dependencies = new LinkedList<String>();
        getDependencies(getSqlNode(test), dependencies);

        System.out.println("dependencies: " + Joiner.on(",").join(dependencies));


    }

    public static SqlNode getSqlNode(String sql) throws SqlParseException {
        String formattedSql = sql.replaceAll(",", " , ").replaceAll("\\s+", " ").trim();

        if (formattedSql.toLowerCase().startsWith("create table ")) {
            System.out.println("sql creates table");
            Matcher m = pattern.matcher(formattedSql.toLowerCase());
            if (m.matches()) {
                System.out.println("matched: " + m.group(1).length());
                formattedSql = formattedSql.substring(m.group(1).length());
            } else {
                System.out.println("no matched");
            }
        }
        System.out.println(formattedSql);

        SqlParser.ConfigBuilder configBuilder = SqlParser.configBuilder().setLex(Lex.JAVA);
        SqlNode sqlNode = SqlParser.create(formattedSql, configBuilder.build()).parseQuery();
        System.out.println(sqlNode.getKind());

        return sqlNode;

    }


    public static List<String> getDependencies(SqlNode sqlNode, List<String> result) throws SqlParseException {
        if (sqlNode.getKind() == JOIN) {
            SqlJoin sqlKind = (SqlJoin) sqlNode;
            System.out.println("-----join");

            //System.out.println("-----join left");
            getDependencies(sqlKind.getLeft(), result);
            //System.out.println("-----join right");
            getDependencies(sqlKind.getRight(), result);
        }


        if (sqlNode.getKind() == IDENTIFIER) {
            System.out.println("-----identifier");
            result.add(sqlNode.toString());
        }


        if (sqlNode.getKind() == INSERT) {
            SqlInsert sqlKind = (SqlInsert) sqlNode;
            System.out.println("-----insert");

            getDependencies(sqlKind.getSource(), result);
        }


        if (sqlNode.getKind() == SELECT) {
            SqlSelect sqlKind = (SqlSelect) sqlNode;
            System.out.println("-----select");
            getDependencies(sqlKind.getFrom(), result);
        }

        if (sqlNode.getKind() == AS) {
            SqlBasicCall sqlKind = (SqlBasicCall) sqlNode;
            System.out.println("----as");
            getDependencies(sqlKind.getOperandList().get(0), result);
        }

        if (sqlNode.getKind() == UNION) {
            SqlBasicCall sqlKind = (SqlBasicCall) sqlNode;
            System.out.println("----union");

            getDependencies(sqlKind.getOperandList().get(0), result);
            getDependencies(sqlKind.getOperandList().get(1), result);

        }

        if (sqlNode.getKind() == ORDER_BY) {
            SqlOrderBy sqlKind = (SqlOrderBy) sqlNode;
            System.out.println("----order_by");
            getDependencies(sqlKind.getOperandList().get(0), result);
        }

        return result;
    }
}

