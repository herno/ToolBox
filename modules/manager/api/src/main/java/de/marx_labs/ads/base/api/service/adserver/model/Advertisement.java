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
package de.marx_labs.ads.base.api.service.adserver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlPath;

public abstract class Advertisement {
	/*
	 * Advertisment id
	 */
	private String id;
	/*
	 * Campaign 
	 */
	private String campaign;
	/*
	 * The sites this ad should be displayed on
	 */
	private List<String> sites = new ArrayList<String>();
	/*
	 * The countries this add should be displayed on
	 */
	private List<String> countries = new ArrayList<String>();
	/*
	 * time of the day
	 */
	private List<Period> timePeriods = new ArrayList<Period>();
	/*
	 * date periods
	 */
	private List<Period> datePeriods = new ArrayList<Period>();
	/*
	 * weekends for displaying the ad
	 * 
	 * 1 = Monday
	 * 2 = Tuesday
	 * 3 = Wednesdays
	 * 4 = Thursday
	 * 5 = Friday
	 * 6 = Saturday
	 * 7 = Sunday
	 */
	private List<Integer> days = new ArrayList<Integer>();
	/*
	 * max click count per resolution
	 */
	private HashMap<String, Integer> clickExpiration = new HashMap<String, Integer>();
	/*
	 * max view count per resolution
	 */
	private HashMap<String, Integer> viewExpiration = new HashMap<String, Integer>();
	
	private String adFormat;
	private String adType;
	
	private Period valid = null;
	
	
	private boolean defaultAd = false;
	
	
	
	@XmlPath("valid")
	public Period getValid() {
		return valid;
	}

	public void setValid(Period valid) {
		this.valid = valid;
	}

	
	public boolean isDefaultAd() {
		return defaultAd;
	}

	public void setDefaultAd(boolean defaultAd) {
		this.defaultAd = defaultAd;
	}
	
	
	public String getAdFormat() {
		return adFormat;
	}

	public void setAdFormat(String adFormat) {
		this.adFormat = adFormat;
	}
	
	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	@XmlPath("conditions/timePeriods/time")
	public List<Period> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(List<Period> timePeriods) {
		this.timePeriods = timePeriods;
	}

	@XmlPath("conditions/datePeriods/date")
	public List<Period> getDatePeriods() {
		return datePeriods;
	}

	public void setDatePeriods(List<Period> datePeriods) {
		this.datePeriods = datePeriods;
	}

	@XmlPath("conditions/days/day/text()")
	public List<Integer> getDays() {
		return days;
	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}

	@XmlPath("conditions/expiration/click")
	public HashMap<String, Integer> getClickExpiration() {
		return clickExpiration;
	}

	public void setClickExpiration(
			HashMap<String, Integer> clickExpiration) {
		this.clickExpiration = clickExpiration;
	}

	@XmlPath("conditions/expiration/view")
	public HashMap<String, Integer> getViewExpiration() {
		return viewExpiration;
	}

	public void setViewExpiration(HashMap<String, Integer> viewExpiration) {
		this.viewExpiration = viewExpiration;
	}

	@XmlPath("conditions/sites/site/text()")
	public List<String> getSites() {
		return sites;
	}

	public void setSites(List<String> sites) {
		this.sites = sites;
	}

	@XmlPath("conditions/countries/country/text()")
	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	
	public void addCountry (String country) {
		if (countries == null) {
			countries = new ArrayList<String>();
		}
		countries.add(country);
		
	}
	
	
	
}
