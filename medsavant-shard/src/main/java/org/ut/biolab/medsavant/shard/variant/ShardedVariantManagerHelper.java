/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.ut.biolab.medsavant.shard.variant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.shards.criteria.ShardedCriteriaImpl;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.ut.biolab.medsavant.shard.common.EntityStyle;
import org.ut.biolab.medsavant.shared.db.TableSchema;
import org.ut.biolab.medsavant.shared.format.CustomField;
import org.ut.biolab.medsavant.shared.model.Range;
import org.ut.biolab.medsavant.shared.model.SessionExpiredException;
import org.ut.biolab.medsavant.shared.util.MiscUtils;

import com.healthmarketscience.sqlbuilder.SelectQuery;

/**
 * Sharded version of the helper class for VariantManager.
 * 
 * @author <a href="mailto:mirocupak@gmail.com">Miroslav Cupak</a>
 * 
 */
public class ShardedVariantManagerHelper implements Serializable {

    private static final long serialVersionUID = 6242924081283721254L;
    private static final String[] INT_FIELDS = new String[] { "upload_id", "file_id", "variant_id", "position", "ac", "an", "dp", "end", "mq0", "ns" };
    private static final String[] FLOAT_FIELDS = new String[] { "qual", "af", "bq", "mq", "sb" };
    private static final String[] BOOLEAN_FIELDS = new String[] { "db", "h2", "somatic", "validated" };
    private static final String[] STRING_FIELDS = new String[] { "dna_id", "chrom", "dbsnp_id", "ref", "alt", "filter", "variant_type", "zygosity", "gt", "custom_info", "aa",
            "cigar" };

    private boolean equalsAny(String pattern, String[] match) {
        for (String s : match) {
            if (pattern.equals(s)) {
                return true;
            }
        }

        return false;
    }

    private Object castToItsType(String att, String value) {
        Object res = null;
        if (equalsAny(att, INT_FIELDS)) {
            res = Integer.valueOf(value);
        } else if (equalsAny(att, BOOLEAN_FIELDS)) {
            res = Boolean.valueOf(value);
        } else if (equalsAny(att, FLOAT_FIELDS)) {
            res = Float.valueOf(value);
        } else {
            res = value;
        }

        return res;
    }

    private Object[] getAttributeValues(Variant v) {
        // use reflection to get the list
        String atts = ReflectionToStringBuilder.toString(v, EntityStyle.getInstance());

        // parse
        atts = atts.substring(1, atts.length() - 1);
        String[] pairs = atts.split(",");
        Object[] os = new Object[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            String[] pieces = pairs[i].split("=", 2);
            os[i] = castToItsType(pieces[0], pieces[1]);
        }

        return os;
    }

    private String getWhereClause(SelectQuery q) {
        // replace original table identifiers with Hibernate references
        return q.getWhereClause().toString().replaceAll("t[0-9]+.", "this_.");
    }

    private Range getExtremeValuesForColumn(Session s, String colName) {
        Object oMin = s.createCriteria(Variant.class).setProjection(Projections.min(colName)).list().get(0);
        Object oMax = s.createCriteria(Variant.class).setProjection(Projections.max(colName)).list().get(0);

        double min;
        if (oMin instanceof Integer) {
            min = (double) (Integer) oMin;
        } else if (oMin instanceof Double) {
            min = (Double) oMin;
        } else {
            throw new ClassCastException("Min is not double");
        }
        double max;
        if (oMax instanceof Integer) {
            max = (double) (Integer) oMax;
        } else if (oMax instanceof Double) {
            max = (Double) oMax;
        } else {
            throw new ClassCastException("Max is not double");
        }

        return new Range(min, max);
    }

    public Integer getNumFilteredVariants(String sessID, SelectQuery q) {
        // TODO: deal with table-project mapping, hibernate configuration has to
        // be provided dynamically
        Session s = ShardedConnectionController.openSession();

        Criteria c = s.createCriteria(Variant.class).setProjection(Projections.count(VariantMapping.getIdColumn()));
        c.add(Restrictions.sqlRestriction(getWhereClause(q)));
        Integer res = ((BigDecimal) c.list().get(0)).intValue();

        ShardedConnectionController.closeSession(s);
        return (res == null ? 0 : res);
    }

    public List<Object[]> getVariants(String sessID, SelectQuery q, int start, int limit, String[] orderByCols) {
        Session session = ShardedConnectionController.openSession();
        Criteria c = session.createCriteria(Variant.class).setFetchSize(limit).setMaxResults(limit).setFirstResult(start);
        c.add(Restrictions.sqlRestriction(getWhereClause(q)));
        List<Variant> variantList = c.list();

        // convert to rows the editor expects
        int numberColumns = VariantMapping.getColumnCount();
        List<Object[]> res = new ArrayList<Object[]>();
        Iterator<Variant> iterator = variantList.iterator();
        while (iterator.hasNext()) {
            res.add(getAttributeValues((Variant) iterator.next()));
        }

        ShardedConnectionController.closeSession(session);

        return res;
    }

    public Map<Range, Long> getFilteredFrequencyValuesForNumericColumn(String sid, SelectQuery q, TableSchema table, CustomField column, float multiplier, boolean logBins)
            throws SQLException, SessionExpiredException, RemoteException, InterruptedException {
        Session s = ShardedConnectionController.openSession();

        Range range = getExtremeValuesForColumn(s, column.getColumnName());
        double binSize = MiscUtils.generateBins(column, range, logBins);

        String round;
        if (logBins) {
            round = "floor(log10(" + column.getColumnName() + ")) ";
        } else {
            round = "floor(" + column.getColumnName() + " / " + binSize + ") ";
        }

        // execute query
        // add order by value ASC if needed
        Criteria c = ((ShardedCriteriaImpl) s.createCriteria(Variant.class)).setProjection(Projections.sqlGroupProjection("count('variant_id') as pos, " + round + "as value",
                "value", new String[] { "value", "pos" }, new Type[] { new IntegerType(), new IntegerType() }));

        Map<Range, Long> results = new TreeMap<Range, Long>();
        List<Object[]> os = c.list();
        for (Object[] o : os) {
            Integer binNo = (int) (((Integer) o[0]) * multiplier);
            Long count = ((BigDecimal) o[1]).longValue();
            Range r;
            if (logBins) {
                r = new Range(Math.pow(10, binNo), Math.pow(10, binNo + 1));
            } else {
                r = new Range(binNo * binSize, (binNo + 1) * binSize);
            }
            results.put(r, count);
        }

        ShardedConnectionController.closeSession(s);

        return results;
    }
}
