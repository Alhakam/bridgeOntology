package de.tud.cib.IFCToBROTConverter.ifcFilter;

import java.util.ArrayList;

/**
 * Representation of an IFC entity
 */
public class IfcEntity {

    private String ifcID;
    private String type;
    private ArrayList<String> decomposedBuildingElementGUIDs;

    public IfcEntity(String ifcID, String type) {
        this.ifcID = ifcID;
        this.type = type;
        this.decomposedBuildingElementGUIDs = new ArrayList<>();
    }

    public IfcEntity(){}

    public String getIfcID() {
        return ifcID;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getDecomposedBuildingElementGUIDs() {
        return decomposedBuildingElementGUIDs;
    }

    public void setIfcID(String ifcID) {
        this.ifcID = ifcID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDecomposedBuildingElementGUIDs(ArrayList<String> decomposedBuildingElementGUIDs) {
        this.decomposedBuildingElementGUIDs = decomposedBuildingElementGUIDs;
    }

    public void addBuildingElementAsDecomposed(String guid) {
        this.decomposedBuildingElementGUIDs.add(guid);
    }
}
