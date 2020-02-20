package JsonUtilities;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import net.minidev.json.JSONArray;

public class GenerateCusomList extends Utility {
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void BuildCustomList() throws FileNotFoundException {

		String basePath = null, customReader = null, fhirReader = null, mapReader = null, sampleFhir = null,
				sampleCustom = null, totalFhir = null, totalCustom = null, mapperFhir = null, mapperCustom = null;
		Object customJson = null, fhirJson = null, mapJson = null;

		basePath = "src/main/java/JsonUtilities/JsonFiles/";

		customReader = readFile(basePath + "customList.json");
		fhirReader = readFile(basePath + "fhirR4List.json");
		mapReader = readFile(basePath + "mapper.json");

		customJson = JsonPath.parse(customReader).json();
		fhirJson = JsonPath.parse(fhirReader).json();
		mapJson = JsonPath.parse(mapReader).json();
		JSONArray feildList = null;

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.list");
		Set listSet = mapObj.entrySet();
		Iterator listIterate = listSet.iterator();

		while (listIterate.hasNext()) {
			Map.Entry list = (Map.Entry) listIterate.next();
			switch (list.getKey().toString()) {
			case "length":
				LinkedHashMap<String, String> listTotal = (LinkedHashMap<String, String>) list.getValue();
				totalFhir = cleanString(listTotal.keySet().toString(), 1);
				totalCustom = cleanString(listTotal.values().toString(), 1);
				break;
			case "iterateObj":
				LinkedHashMap<String, String> listMapper = (LinkedHashMap<String, String>) list.getValue();
				mapperFhir = cleanString(listMapper.keySet().toString(), 1);
				mapperCustom = cleanString(listMapper.values().toString(), 1);
				break;
			case "sampleObj":
				LinkedHashMap<String, String> listSample = (LinkedHashMap<String, String>) list.getValue();
				sampleFhir = cleanString(listSample.keySet().toString(), 1);
				sampleCustom = cleanString(listSample.values().toString(), 1);
				break;
			}
		}
		DocumentContext cstm = JsonPath.parse(customJson);
		String sampleJson = cleanString(JsonPath.parse(customJson).read(mapperCustom), 1);
		ReadContext rCtx = JsonPath.parse(sampleJson);
		Object sampleObject = rCtx.json();
		JSONArray ja = new JSONArray();
		DocumentContext newCstm = JsonPath.parse(sampleObject);
		Integer totalFhirCount = JsonPath.parse(fhirJson).read(totalFhir);
		for (Integer f = 0; f < totalFhirCount; f++) {
			LinkedHashMap<String, String> mapFields = JsonPath.parse(mapJson).read("$.list.fieldObj");
			Set mapFieldSet = mapFields.entrySet();
			Iterator mapFieldIterate = mapFieldSet.iterator();
			
			while (mapFieldIterate.hasNext()) {
				Map.Entry mapField = (Map.Entry) mapFieldIterate.next();
				newCstm = setJson(newCstm, fhirJson, JsonPath.parse(sampleJson).json(), mapField.getKey().toString(),
						(mapField.getValue()).toString().replace("*", f.toString()));
			}
			
			ReadContext dataCtx = JsonPath.parse(newCstm.jsonString());
			JsonPath.parse(customJson).add(mapperCustom,dataCtx.json());
		}
		JsonPath.parse(customJson).delete(sampleCustom);
		System.out.println(customJson);
	}

}
