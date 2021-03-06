/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
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
package de.marx_labs.ads.db.condition.impl.lucene;


import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;

import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.condition.Filter;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;


/**
 * Unterdrückung von Bannern auf bestimmten Seiten
 * 
 * @author tmarx
 *
 */
public class ExcludeSiteCondition implements Condition<Document, BooleanQuery>, Filter {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
//		if (request.getSite() == null) {
//			return;
//		}
//		
//		
//		BooleanQuery query = new BooleanQuery();
//		
//		BooleanQuery temp = new BooleanQuery();
//		
//		// Seite einfügen
//		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_SITE_EXCLUDE, request.getSite())), Occur.SHOULD);
//		
//		query.add(temp, Occur.MUST);
//		mainQuery.add(query, Occur.MUST_NOT);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
//		ExcludeSiteConditionDefinition sdef = null;
//		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.EXCLUDE_SITE)) {
//			sdef = (ExcludeSiteConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.EXCLUDE_SITE);
//		}
//		
//		if (sdef != null && !sdef.getSites().isEmpty()) {
//			// Sites im Dokument speichern
//			List<String> sites = sdef.getSites();
//			for (String site : sites) {
//				bannerDoc.add(new Field(AdDBConstants.ADDB_AD_SITE_EXCLUDE, site, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
//			}
//		}
	}

	@Override
	public Predicate<AdDefinition> getFilterPredicate(final AdRequest request) {
		Predicate<AdDefinition> predicate = new Predicate<AdDefinition>() {
			@Override
			public boolean apply(AdDefinition type) {
				
				/*
				if (true) {
					return true;
				}
				*/
				
				ExcludeSiteConditionDefinition sdef = null;
				if (type.hasConditionDefinition(ConditionDefinitions.EXCLUDE_SITE)) {
					sdef = (ExcludeSiteConditionDefinition) type.getConditionDefinition(ConditionDefinitions.EXCLUDE_SITE);
				} else {
					// keine Seitenbeschränkung
					return true;
				}
				// keine Seite im Request, aber das Banner hat eine Seitenbeschränkung
				if (Strings.isNullOrEmpty(request.site())) {
					return false;
				}
				
				if (sdef != null) {
					if (sdef.getSites().contains(request.site())) {
						return false;
					}
				}
				
				return true;
			}
		};
		return predicate;
	}

}
