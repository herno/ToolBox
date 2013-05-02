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
package de.marx_labs.ads.db.condition.impl.mongo;

import java.util.ArrayList;
import java.util.List;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;


public class SiteCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getSite() == null) {
			return;
		}
		
		List<String> sites = new ArrayList<String>();
		sites.add(request.getSite());
		sites.add(AdDBConstants.ADDB_AD_SITE_ALL);
		
		BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_SITE, new BasicDBObject("$in", sites));
		
		builder.and(dq);
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		SiteConditionDefinition sdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.SITE)) {
			sdef = (SiteConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.SITE);
		}
		
		if (sdef != null && !sdef.getSites().isEmpty()) {
			// Sites im Dokument speichern
			List<String> sites = sdef.getSites();
			bannerDoc.put(AdDBConstants.ADDB_AD_SITE, sites);
		} else {
			/*
			 * Banner, die keine Einschräkung auf eine spezielle Seite haben
			 */
			List<String> sites = new ArrayList<String>();
			sites.add(AdDBConstants.ADDB_AD_SITE_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_SITE, sites);
		}
	}

}