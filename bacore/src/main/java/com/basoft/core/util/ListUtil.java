package com.basoft.core.util;

import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengtaifan
 */
public class ListUtil {
	//private static final String  SEPARATOR 		= "==================================================";
	private static final String  SEPARATOR_START = "======================== Show List Contents Satrt ==========================";
	private static final String  SEPARATOR_END 	= "======================== Show List Contents End ============================";
	
	public static void printListMap(List<Map<String,Object>> list){
		System.out.println(SEPARATOR_START);
		if(list != null && !list.isEmpty()){
			int j = 0;
			System.out.println("[");
			for (Map<String, Object> map : list) {
				int i = 0;
				System.out.print("    {");
				List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());

				Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {   
				    public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {      
				        //return (o2.getValue() - o1.getValue()); 
				        return (o1.getKey()).toString().compareTo(o2.getKey());
				    }
				}); 
				
				for (int k = 0; k < infoIds.size(); k++) {
				    String key = infoIds.get(k).toString().split("=")[0];
				    Object value = map.get(key);
				    if(k > 0){
						System.out.print(",");
					}
				    if(value instanceof Date){
						System.out.print("\"" + key + "\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + "\"");
					}else{
						System.out.print("\"" + key + "\":\"" + value+ "\"");
					}
				}
				/*Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry<String, Object>  entry = iter.next();
					if(i++ > 0){
						System.out.print(",");
					}

					if(entry.getValue() instanceof Date){
						System.out.print("\"" + entry.getKey() + "\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getValue()) + "\"");
					}else{
						System.out.print("\"" + entry.getKey() + "\":\"" + entry.getValue()+ "\"");
					}
				}*/
				
				System.out.print("}");
				if(++j != list.size()){
					System.out.print(",\n");
				}else{
					System.out.print("\n");
				}
				 
			}
			System.out.println("]");
			System.out.println("List Size = " + list.size());
		}else{
			System.out.println("List is Empty!!");
		}
		
		System.out.println(SEPARATOR_END);
	}
	

	public static void convertClob2String(List<Map<String,Object>> list){
		for (Map<String, Object> map : list) {
			for(Map.Entry<String, Object> entry : map.entrySet()){ 
				Object value = entry.getValue();
				if(value instanceof Clob){
					Clob clob = (Clob) value;
					String val = "";
					try {
						int size = (int) clob.length();
						val = clob.getSubString(1, size);   
						if(!"".equals(val)){
							val = val.trim();
						}
						entry.setValue(val);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} 
		}
	}
	
	
	public static void main(String[] args) {
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("NAME", "jong");
		map.put("age", 17);
		map.put("birthdate", new Date());
		
		listmap.add(map);
		map = new HashMap<String,Object>();
		
		map.put("NAME", "taebem");
		map.put("age", 11);
		map.put("birthdate", new Date());
		
		listmap.add(map);
		map = new HashMap<String,Object>();
		map.put("NAME", "dddd");
		map.put("age", 12);
		map.put("birthdate", new Date());
		listmap.add(map);
		
		// printListMap(listmap);
		// printListMap(null);
	}
}
