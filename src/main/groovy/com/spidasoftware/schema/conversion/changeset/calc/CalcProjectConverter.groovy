package com.spidasoftware.schema.conversion.changeset.calc

import com.spidasoftware.schema.conversion.changeset.*
import groovy.util.logging.Log4j

@Log4j
class CalcProjectConverter extends AbstractConverter {

    @Override
    String getSchemaPath() {
        return "/schema/spidacalc/calc/project.schema"
    }

    @Override
    void updateVersion(Map json, int version) {
        json.put("version", version)
        boolean versionAllowedInLocationAndDesign = (version >= VERSION_ALLOWED_IN_LOCATION_DESIGN)
        json.get("leads")?.each { Map leadJSON ->
            leadJSON.get("locations")?.each { Map locationJSON ->
                if(versionAllowedInLocationAndDesign) {
                    locationJSON.put("version", version)
                } else if(locationJSON.containsKey("version")) {
                    locationJSON.remove("version")
                }
                locationJSON.get("designs")?.each { Map designJSON ->
                    if(versionAllowedInLocationAndDesign) {
                        designJSON.put("version", version)
                    } else if(designJSON.containsKey("version")) {
                        designJSON.remove("version")
                    }
                }
            }
        }
    }

    @Override
    void applyChangeset(ChangeSet changeSet, Map json) {
        changeSet.applyToProject(json)
    }

    @Override
    void revertChangeset(ChangeSet changeSet, Map json) {
        changeSet.revertProject(json)
    }
}
