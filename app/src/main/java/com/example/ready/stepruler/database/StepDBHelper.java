package com.example.ready.stepruler.database;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.List;

/**
 * Created by ready on 2018/1/5.
 */
public class StepDBHelper {

    public static String DB_NAME;
    public static LiteOrm liteOrm;

    public static void createDb(Context _activity, String DB_NAME) {
        DB_NAME = DB_NAME + ".db";
        if (liteOrm == null) {
            liteOrm = LiteOrm.newCascadeInstance(_activity, DB_NAME);
            liteOrm.setDebugged(true);
        }
    }


    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }

    /**
     * 插入一条记录
     */
    public static <T> void insert(T t) {
        liteOrm.save(t);
    }

    /**
     * 插入所有记录
     */
    public static <T> void insertAll(List<T> list) {
        liteOrm.save(list);
    }

    /**
     * 查询所有
     */
    public static <T> List<T> getQueryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }

    /**
     * 查询  某字段 等于 Value的值
     */
    public static <T> List<T> getQueryByWhere(Class<T> cla, String field, String value) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     */
    public static <T> List<T> getQueryByWhereLength(Class<T> cla, String field, String value, int start, int length) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }

    /**
     * 删除所有
     */
    public static <T> void deleteAll(Class<T> cla) {
        liteOrm.deleteAll(cla);
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     */
    public static <T> int deleteWhere(Class<T> cla, String field, String value) {
        return liteOrm.delete(cla, new WhereBuilder(cla).where(field + "!=?", value));
    }

    /**
     * 仅在以存在时更新
     */
    public static <T> void update(T t) {
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }


    public static <T> void updateALL(List<T> list) {
        liteOrm.update(list);
    }

    public static void closeDb() {
        liteOrm.close();
    }

}
