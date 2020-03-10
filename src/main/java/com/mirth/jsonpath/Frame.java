package com.mirth.jsonpath;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class Frame extends Utility {

	@SuppressWarnings("rawtypes")
	public static String MakeJson(String src, String dest, String map) {

		Object srcJson = null, destJson = null, mapJson = null;

		srcJson = JsonPath.parse(src).json();
		destJson = JsonPath.parse(dest).json();
		mapJson = JsonPath.parse(map).json();
		DocumentContext newJson = JsonPath.parse(destJson);

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.write");
		Set writeSet = mapObj.entrySet();
		Iterator writeIterate = writeSet.iterator();
		while (writeIterate.hasNext()) {
			Map.Entry write = (Map.Entry) writeIterate.next();
			newJson = setJson(newJson, srcJson, destJson, write.getKey().toString(), write.getValue().toString());
		}
		return newJson.jsonString();
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static String MakeJsonList(String src, String dest, String map) {

		String basePath = null, customReader = null, fhirReader = null, 
				mapReader = null, sample = null, totalFhir = null, 
				mapperCustom = null,nonIterateCustom=null;
		Object srcJson = null, destJson = null, mapJson = null;

		srcJson = JsonPath.parse(src).json();
		destJson = JsonPath.parse(dest).json();
		mapJson = JsonPath.parse(map).json();
		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.list");
		Set<?> listSet = mapObj.entrySet();
		Iterator<?> listIterate = listSet.iterator();

		while (listIterate.hasNext()) {
			Map.Entry list = (Map.Entry) listIterate.next();
			switch (list.getKey().toString()) {
			case "length":
				LinkedHashMap<String, String> listTotal = (LinkedHashMap<String, String>) list.getValue();
				totalFhir = cleanString(listTotal.keySet().toString(), 1);
				break;
			case "iterateObj":
				LinkedHashMap<String, String> listMapper = (LinkedHashMap<String, String>) list.getValue();
				mapperCustom = cleanString(listMapper.values().toString(), 1);
				break;
			case "nonIterateFieldObj":
				LinkedHashMap<String, String> nonIterateMap = (LinkedHashMap<String, String>) list.getValue();
				nonIterateCustom = cleanString(nonIterateMap.values().toString(), 1);
				break;
			case "sampleObj":
				LinkedHashMap<String, String> listSample = (LinkedHashMap<String, String>) list.getValue();
				sample = cleanString(listSample.values().toString(), 1);
				break;
			}
		}
		DocumentContext cstm = JsonPath.parse(srcJson);
		String sampleJson = cleanString(JsonPath.parse(srcJson).read(mapperCustom), 1);
		ReadContext rCtx = JsonPath.parse(sampleJson);
		Object sampleObject = rCtx.json();
		DocumentContext newCstm = JsonPath.parse(sampleObject);
		Integer totalFhirCount = JsonPath.parse(destJson).read(totalFhir);
		for (Integer f = 0; f < totalFhirCount; f++) {
			LinkedHashMap<String, String> iterateFields = JsonPath.parse(mapJson).read("$.list.iterateFieldObj");
			Set iterateFieldSet = iterateFields.entrySet();
			Iterator iterateField = iterateFieldSet.iterator();
			
			while (iterateField.hasNext()) {
				Map.Entry mapField = (Map.Entry) iterateField.next();
				newCstm = setJson(newCstm, destJson, JsonPath.parse(sampleJson).json(), mapField.getKey().toString(),
						(mapField.getValue()).toString().replace("*", f.toString()));
			}
			
			ReadContext dataCtx = JsonPath.parse(newCstm.jsonString());
			JsonPath.parse(srcJson).add(mapperCustom,dataCtx.json());
		}
		JsonPath.parse(srcJson).delete(sample);
		return srcJson.toString();
	}
}
