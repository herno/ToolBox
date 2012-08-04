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
package net.mad.ads.base.api.service.orientdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.ads.condition.DateCondition;
import net.mad.ads.base.api.model.site.Site;
import net.mad.ads.base.api.service.site.SiteService;

public class OrientSiteService extends AbstractOrientDBService<Site> implements SiteService {

	private static class Fields {
		public static final String ID 					= "id";
		public static final String NAME 				= "name";
		public static final String DESCRIPTION 			= "description";
		public static final String CREATED 				= "created";
		public static final String URL 					= "url";
	}

	public static final String CLASS_NAME = "Site";
	
	public OrientSiteService() {
		super();
	}
	
	public String getClassName () {
		return this.CLASS_NAME;
	}


	public Site toObject(ODocument doc) {
		Site site = new Site();

		site.setId((String) doc.field(Fields.ID));
		site.setName((String) doc.field(Fields.NAME));
		site.setDescription((String) doc.field(Fields.DESCRIPTION));
		site.setCreated((Date) doc.field(Fields.CREATED));
		site.setUrl((String)doc.field(Fields.URL));


		return site;
	}

	public ODocument toDocument(Site site) {
		ODocument doc = new ODocument(CLASS_NAME);

		doc.field(Fields.ID, site.getId());
		doc.field(Fields.NAME, site.getName());
		doc.field(Fields.DESCRIPTION, site.getDescription());
		doc.field(Fields.CREATED, site.getCreated());
		doc.field(Fields.URL, site.getUrl());


		return doc;
	}

	public ODocument updateDocument(ODocument doc, Site site) {
//		doc.field(Fields.ID, site.getId());
		doc.field(Fields.NAME, site.getName());
		doc.field(Fields.DESCRIPTION, site.getDescription());
//		doc.field(Fields.CREATED, site.getCreated());
		doc.field(Fields.URL, site.getUrl());

		return doc;
	}

	public ODocument getDocumentByID(String id) {
		ODatabaseDocumentTx db = acquire();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ").append(Fields.ID).append(" = '").append(id).append("'");
			List<ODocument> result = db.query(
					  new OSQLSynchQuery<ODocument>(query.toString()));
			
			if (result.size() == 1) {
				return result.get(0);
			}
		} finally {
			release(db);
		}
		return null;
	}

}
