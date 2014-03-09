/**
 * Copyright By Nanjing Fujitsu Nanda Software Technology Co., Ltd
 * 下午3:17:00
 * BaseDao.java
 *
 */
package org.liyou.qixiaobo.daos;

import org.hibernate.*;
import org.liyou.qixiaobo.execptions.NotOneException;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author qixb.fnst
 */
public class BaseDao<T> {
    @Resource
    private SessionFactory sessionFacotry;
    private final static DateFormat dateFormat = new SimpleDateFormat ("HH:mm");
    private final static DateFormat dateFormatWithDay = new SimpleDateFormat ("yyyy-MM-dd");
    private final static DateFormat dateFormatWithDayAndTime = new SimpleDateFormat ("MM-dd HH:mm");

    @Transactional
    public T insert (T t) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction();
        session.save (t);
        session.flush ();
        tx.commit();
        return t;
    }

    @Transactional
    public T update (T t) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction();
        session.update (t);
        session.flush ();
        tx.commit ();
        return t;
    }

    public static DateFormat getDateformat () {
        return dateFormat;
    }

    public static String getDateformatString (Date date) {
        if (date == null) {
            return null;
        }
        return dateFormat.format (date);
    }

    public static String getDateformatWithDayAndTimeString (Date date) {
        if (date == null) {
            return null;
        }
        return dateFormatWithDayAndTime.format (date);
    }

    public static String getDateformatWithDayString (Date date) {
        if (date == null) {
            return null;
        }
        return dateFormatWithDay.format (date);
    }

    public static DateFormat getDateformatwithday () {
        return dateFormatWithDay;
    }

    @Transactional
    public void delete (T t) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction();
        session.delete (t);
        session.flush ();
        tx.commit ();
    }

    @Transactional
    public T query (Class<T> clazz, int id) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction();
        Object t = session.get (clazz, id);
        tx.commit ();
        if (t == null)
            return null;
        return (T) t;

    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<T> query (String queryString) {
        Session session = sessionFacotry.getCurrentSession ();
        Query query = session.createQuery (queryString);
        return query.list ();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    protected List<T> query (Criteria criteria) {
        return criteria.list ();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    protected T query4One (Criteria criteria) throws NotOneException {
        List<T> list = criteria.list ();
        if (list == null || list.size () == 0) {
            throw new NotOneException ("没有查询到结果集！");
        }
        if (list.size () > 1) {
            throw new NotOneException ("数据库数据出错！");
        }
        return list.get (0);
    }

    @Transactional
    protected int queryNums (Class<T> t, String whereClause) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("select count(*) from ").append (t.getSimpleName ());
        if (whereClause != null) {
            sb.append (" where ").append (whereClause);
        }
        String hql = sb.toString ();
        Session session = sessionFacotry.getCurrentSession ();
        Number count = (Number) session.createQuery (hql).uniqueResult ();
        return count.intValue ();
    }

    /**
     * <font color='red'>该方法只支持mysql</font>
     * **
     */
    @Transactional
    protected List<T> query4Page (Class<T> t, String orderClause,
                                  String whereClause, boolean userLimit, int firstRecord, int limit) {
        StringBuilder sb = new StringBuilder ();
        sb.append (" from ").append (t.getSimpleName ());
        if (whereClause != null) {
            sb.append (" where ").append (whereClause);
        }
        if (orderClause != null) {
            sb.append (" order by").append (orderClause);
        }
        Session session = sessionFacotry.getCurrentSession ();
        Query query = session.createQuery (sb.toString ());
        if (userLimit) {
            query.setFirstResult (firstRecord);
            query.setMaxResults (limit);
        }
        @SuppressWarnings("unchecked")
        List<T> ts = query.list ();
        return ts;
    }
}
