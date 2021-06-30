package com.basoft.core.ware.wechat.domain.pay;

import java.util.ArrayList;
import java.util.List;

public class OrderBill {
	private String[] header;
	private List<String[]> data = new ArrayList<String[]>();
	private String[] statisticsHeader;
	private String[] statisticsData;

	public String[] getHeader() {
		return header;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public List<String[]> getData() {
		return data;
	}

	public void setData(List<String[]> data) {
		this.data = data;
	}

	public String[] getStatisticsHeader() {
		return statisticsHeader;
	}

	public void setStatisticsHeader(String[] statisticsHeader) {
		this.statisticsHeader = statisticsHeader;
	}

	public String[] getStatisticsData() {
		return statisticsData;
	}

	public void setStatisticsData(String[] statisticsData) {
		this.statisticsData = statisticsData;
	}
}