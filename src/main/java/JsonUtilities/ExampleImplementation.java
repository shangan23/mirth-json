package JsonUtilities;

import java.io.FileNotFoundException;

public class ExampleImplementation {

	public static void main(String[] args) throws FileNotFoundException {
			//GenerateCusomList gcl = new GenerateCusomList();
			//gcl.BuildCustomList();
			GenerateFhirCreate gfc = new GenerateFhirCreate();
			gfc.CreateFhirbyFile();
			
			String custom = null,fhir = null,mapper =null;
			custom = "{\n" + 
					"  \"ViewName\" : \"getPatientDemographicsInfo\",\n" + 
					"  \"Action\" : \"Save\",\n" + 
					"  \"Status\" : \"\",\n" + 
					"  \"PatientId\" : \"200000\",\n" + 
					"  \"System\" : \"Cerner\",\n" + 
					"  \"Data\" : [\n" + 
					"    {\n" + 
					"      \"FieldId\" : 2308,\n" + 
					"      \"Value\" : \"{code:'22222',display:'Shankar',text:'Something wired'}\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"FieldId\" : 2309,\n" + 
					"      \"Value\" : \"{code:'22222',display:'Shankar',text:'Something wired'}\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"FieldId\" : 2307,\n" + 
					"      \"Value\" : \"Resolved\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"FieldId\" : 2306,\n" + 
					"      \"Value\" : \"Confirmed\"\n" + 
					"    }\n" + 
					"  ],\n" + 
					"  \"DataUpdated\" : [ ]\n" + 
					"}";
			fhir = "{\n" + 
					"  \"resourceType\": \"Appointment\",\n" + 
					"  \"slot\": [{\n" + 
					"    \"reference\": \"Slot/5038369-4048296-7725872-150\"\n" + 
					"  }],\n" + 
					"  \"participant\": [\n" + 
					"    {\n" + 
					"      \"actor\": {\n" + 
					"        \"reference\": \"Patient/7422007\"\n" + 
					"      },\n" + 
					"      \"status\": \"accepted\"\n" + 
					"    }\n" + 
					"  ],\n" + 
					"  \"status\": \"booked\"\n" + 
					"}\n" + 
					"";
			mapper = "{\n" + 
					"	\"write\": {\n" + 
					"		\"$.participant[0].status\": \"$.Data[?(@.FieldId=='30008')].Value\",\n" + 
					"		\"$.status\": \"$.Data[?(@.FieldId=='30000')].Value\",\n" + 
					"		\"$.slot[0].reference\": \"$.Data[?(@.FieldId=='30007')].Value\"\n" + 
					"	},\n" + 
					"	\"list\": {\n" + 
					"		\"length\": {\n" + 
					"		},\n" + 
					"		\"iterateObj\": {\n" + 
					"		},\n" + 
					"		\"sampleObj\": {\n" + 
					"		},\n" + 
					"		\"iterateFieldObj\": {\n" + 
					"		},\n" + 
					"		\"nonIterateFieldObj\": {\n" + 
					"		}\n" + 
					"	}\n" + 
					"}\n" + 
					"";
			//gfc.CreateFhir(custom, fhir, mapper);
	}
	
}
