/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.mad.ads.db.condition.impl.mongo;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.TimeConditionDefinition;
import net.mad.ads.db.definition.condition.TimeConditionDefinition.Period;
import net.mad.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

/**
 * Bedingung bzgl. des Zeitraums in dem das Banner angezeigt werden soll
 * z.B. zwischen 23 und 4 Uhr 
 * 
 * 04.07.2011:
 * Nach der Anpassung können im Banner nun Perioden angegeben werden, wann ein Banner
 * angezeigt werden soll 
 * zwischen 8 und 10 Uhr und zwischen 12 und 14 Uhr 
 * 
 * @author tmarx
 *
 */
public class TimeCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		BooleanQuery query = null;
		
		if (request.getTime() != null) { 
			
			List<DBObject> condition_queries = new ArrayList<DBObject>();
			
			/*
			 * Prefix der Indizieren beachten.
			 */
			for (int i = 0; i < TimeConditionDefinition.MAX_PERIOD_COUNT; i++) {
				
				QueryBuilder condition = QueryBuilder.start();
				
				// from part of the query
				BasicDBObject query_from_1 = new BasicDBObject(AdDBConstants.ADDB_AD_TIME_FROM + i, new BasicDBObject("$lte", request.getTime()));
				BasicDBObject query_from_2 = new BasicDBObject(AdDBConstants.ADDB_AD_TIME_FROM + i, AdDBConstants.ADDB_AD_TIME_ALL);
				
				List<BasicDBObject> queries = new ArrayList<BasicDBObject>();
				queries.add(query_from_1);
				queries.add(query_from_2);
				BasicDBObject query_from = new BasicDBObject("$or", queries);
				
				condition.and(query_from);
				
				// to part of the query
				BasicDBObject query_to_1 = new BasicDBObject(AdDBConstants.ADDB_AD_TIME_TO + i, new BasicDBObject("$gte", request.getTime()));
				BasicDBObject query_to_2 = new BasicDBObject(AdDBConstants.ADDB_AD_TIME_TO + i, AdDBConstants.ADDB_AD_TIME_ALL);
				
				queries = new ArrayList<BasicDBObject>();
				queries.add(query_to_1);
				queries.add(query_to_2);
				BasicDBObject query_to = new BasicDBObject("$or", queries);
				
				condition.and(query_to);

				condition_queries.add(condition.get());
				
				
				
//				query = new BooleanQuery();
//				
//				BooleanQuery temp = new BooleanQuery();
//				TermRangeQuery tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_TIME_FROM + i, new BytesRef("0000"), new BytesRef(request.getTime()), true, true);
//				temp.add(tQuery, Occur.SHOULD);
//				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_TIME_FROM + i, AdDBConstants.ADDB_AD_TIME_ALL)), Occur.SHOULD);
//				
//				query.add(temp, Occur.MUST);
//				
//				temp = new BooleanQuery();
//				tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_TIME_TO + i, new BytesRef(request.getTime()), new BytesRef("2500"), true, true);
//				temp.add(tQuery, Occur.SHOULD);
//				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_TIME_TO + i, AdDBConstants.ADDB_AD_TIME_ALL)), Occur.SHOULD);
//				
//				query.add(temp, Occur.MUST);
//				
//				tempquery.add(query, Occur.SHOULD);
			}	
			builder.and(new BasicDBObject("$or", condition_queries));
		}
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		TimeConditionDefinition tdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.TIME)) {
			tdef = (TimeConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.TIME);
		}
		
		if (tdef != null && !tdef.getPeriods().isEmpty()) {
			/*
			 * 	Um die Paare von/zu der Perioden zu erhalten, werden die jeweilige Felder geprefixt.
			 *  Dadurch können bei der Suche die einzelnen Perioden unterschieden werden
			 */
			int count = 0;
			for (Period p : tdef.getPeriods()) {
				if (p.getFrom() != null) {
					bannerDoc.put(AdDBConstants.ADDB_AD_TIME_FROM + count, p.getFrom());
				} else {
					bannerDoc.put(AdDBConstants.ADDB_AD_TIME_FROM + count, AdDBConstants.ADDB_AD_TIME_ALL);
				}
				
				if (p.getFrom() != null) {
					bannerDoc.put(AdDBConstants.ADDB_AD_TIME_TO + count, p.getTo());
				} else {
					bannerDoc.put(AdDBConstants.ADDB_AD_TIME_TO + count, AdDBConstants.ADDB_AD_TIME_ALL);
				}
				count++;
			}
		} else {
			bannerDoc.put(AdDBConstants.ADDB_AD_TIME_FROM + 0, AdDBConstants.ADDB_AD_TIME_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_TIME_TO + 0, AdDBConstants.ADDB_AD_TIME_ALL);
		}
	}

}