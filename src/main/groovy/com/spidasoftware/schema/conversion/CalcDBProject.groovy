package com.spidasoftware.schema.conversion

import net.sf.json.JSONObject

/**
 * Represents a Project that exists in SPIDAdb.
 */
class CalcDBProject extends AbstractCalcDBComponent {

    CalcDBProject(JSONObject calcdbProjectJson) {
        super(calcdbProjectJson)
    }

    /**
     * @return the SPIDAdb ids of the locations contained in this project
     */
    List<String> getChildLocationIds(){
        return getCalcJSON().getJSONArray("leads")?.collect{JSONObject lead-> lead.locations}?.flatten()?.collect {JSONObject location-> location.id }
    }

    void updateLocationIds(Map<String, String> oldToNew){
      getCalcJSON().getJSONArray("leads").each{JSONObject lead ->
           lead.locations.each{JSONObject location ->
	           if(oldToNew.get(location.id)){
		           location.put("id", oldToNew.get(location.id))
	           }
           }
      }
    }

	@Override
	String getClientFileName() {
		return getCalcJSON().getString('clientFile')
	}

	@Override
	JSONObject getCalcJSON() {
		return getJSON().getJSONObject('calcProject')
	}

	@Override
    String toString() {
        return getName()?: "Project"
    }

}