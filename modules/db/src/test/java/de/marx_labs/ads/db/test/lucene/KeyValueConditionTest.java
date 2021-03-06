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
package de.marx_labs.ads.db.test.lucene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.KeyValue;
import de.marx_labs.ads.db.definition.condition.KeyValueConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;


public class KeyValueConditionTest extends AdDBTestCase {
	
	@Test
	public void test1_KeyValueCondition () throws Exception {
		System.out.println("test1_KeyValueCondition");
		
		
		manager.getContext().validKeys.clear();
		manager.getContext().validKeys.add("browser");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		KeyValueConditionDefinition sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "chrome"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("2");
		sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "ie"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.formats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.types(types);
		request.keyValues().put("browser", "opera");
		
		List<AdDefinition> result = db.search(request);
		assertTrue(result.isEmpty());
		
		request.keyValues().clear();
		request.keyValues().put("browser", "firefox");
		result = db.search(request);
		assertEquals(2, result.size());
		
		request.keyValues().clear();
		request.keyValues().put("browser", "chrome");
		
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("1"));
		
		request.keyValues().clear();
		request.keyValues().put("browser", "ie");
		
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("2"));
		
	}
	

	@Test
	public void test2_KeyValueCondition () throws Exception {
		System.out.println("test2_KeyValueCondition");
		
		manager.getContext().validKeys.clear();
		manager.getContext().validKeys.add("browser");
		manager.getContext().validKeys.add("os");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		KeyValueConditionDefinition sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "chrome"));
		sdef.getKeyValues().add(new KeyValue("os", "osx"));
		sdef.getKeyValues().add(new KeyValue("os", "linux"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("2");
		sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "ie"));
		sdef.getKeyValues().add(new KeyValue("os", "windows"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("3");
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.formats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.types(types);
		request.keyValues().put("browser", "opera");
		request.keyValues().put("os", "linux");
		
		List<AdDefinition> result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("3"));
		
		request.keyValues().clear();
		request.keyValues().put("browser", "firefox");
		request.keyValues().put("os", "osx");
		result = db.search(request);
		assertEquals(2, result.size());
		
		
		request.keyValues().clear();
		request.keyValues().put("browser", "chrome");
		request.keyValues().put("os", "osx");
		
		result = db.search(request);
		assertEquals(2, result.size());
		
		request.keyValues().clear();
		request.keyValues().put("browser", "ie");
		request.keyValues().put("os", "windows");
		
		result = db.search(request);
		assertEquals(2, result.size());
		
	}
}
