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
package de.marx_labs.ads.server.utils.listener.configuration;

import java.net.UnknownHostException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import de.marx_labs.ads.base.utils.BaseContext;
import de.marx_labs.ads.base.utils.exception.ServiceException;
import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.services.geo.IPLocationDB;
import de.marx_labs.ads.services.tracking.TrackingContextKeys;
import de.marx_labs.ads.services.tracking.TrackingService;
import de.marx_labs.ads.services.tracking.impl.local.h2.H2TrackingService;
import de.marx_labs.ads.services.tracking.impl.mongo.MongoTrackingService;

public class AdServerModule extends AbstractModule {

	private static final Logger logger = LoggerFactory
			.getLogger(AdServerModule.class);

	@Override
	protected void configure() {

		try {
			BaseContext baseContext = new BaseContext();
			baseContext.put(
					TrackingContextKeys.EMBEDDED_DB_DIR,
					RuntimeContext.getProperties().getProperty(
							AdServerConstants.CONFIG.PROPERTIES.TRACK_DIR));

			String classname = RuntimeContext
					.getProperties()
					.getProperty(
							AdServerConstants.CONFIG.PROPERTIES.IPLOCATIONSERVICE_CLASS,
							"");
			IPLocationDB iplocDB = (IPLocationDB) Class.forName(classname)
					.newInstance();

			bind(IPLocationDB.class).toInstance(iplocDB);

			initTracking(baseContext);

		} catch (ClassCastException cce) {
			logger.error("", cce);
		} catch (InstantiationException e) {
			logger.error("", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		}
	}

	private void initTracking(BaseContext context) {

		try {
			String classname = RuntimeContext.getProperties().getProperty(
					AdServerConstants.CONFIG.PROPERTIES.TRACKINGSERVICE_CLASS,
					"");
			TrackingService trackService = (TrackingService) Class.forName(
					classname).newInstance();
			
			if (trackService instanceof H2TrackingService) {
				Context ctx = new InitialContext();
				ctx = (Context) ctx.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup("jdbc/trackingds");

				context.put(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE, ds);
			} else if (trackService instanceof MongoTrackingService) {
				String host = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.TRACKING_MONGO_HOST, "");
				String db = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.TRACKING_MONGO_DB, "");
				String collectionName = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.TRACKING_MONGO_COLLECTION, "");
				String username = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.TRACKING_MONGO_USERNAME, "");
				String password = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.TRACKING_MONGO_PASSWORD, "");
				String authentication = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.TRACKING_MONGO_AUTHENTICATION, "false");
				
				MongoClient mongoClient = new MongoClient(host);
				DB mongoDB = mongoClient.getDB(db);
				mongoDB.isAuthenticated();
				boolean auth = true;
				if ("true".equalsIgnoreCase(authentication)) {
					auth = mongoDB.authenticate(username, password.toCharArray());
				}
				if (auth) {
					DBCollection collection = mongoDB.getCollection(collectionName);
					
					context.put(MongoTrackingService.MONGO_DB, mongoDB);
					context.put(MongoTrackingService.MONGO_DB_COLLECTION, collection);
				} else {
					throw new RuntimeException("MongoDB Exception: not authorized");
				}
			}
			
			trackService.open(context);

			bind(TrackingService.class).toInstance(trackService);

		} catch (NamingException se) {
			logger.error("", se);
		} catch (ClassCastException cce) {
			logger.error("", cce);
		} catch (ServiceException e) {
			logger.error("", e);
		} catch (InstantiationException e) {
			logger.error("", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		} catch (UnknownHostException e) {
			logger.error("", e);
		}
	}

}
