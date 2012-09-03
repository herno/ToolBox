/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.db.condition.impl;


import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.condition.Filter;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;


/**
 * Unterdrückung von Bannern auf bestimmten Seiten
 * 
 * @author tmarx
 *
 */
public class ExcludeSiteCondition implements Condition, Filter {

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
				if (Strings.isNullOrEmpty(request.getSite())) {
					return false;
				}
				
				if (sdef != null) {
					if (sdef.getSites().contains(request.getSite())) {
						return false;
					}
				}
				
				return true;
			}
		};
		return predicate;
	}

}
