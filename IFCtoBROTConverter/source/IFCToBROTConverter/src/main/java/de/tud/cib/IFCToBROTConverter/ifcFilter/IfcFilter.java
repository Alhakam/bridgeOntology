package de.tud.cib.IFCToBROTConverter.ifcFilter;

import com.apstex.ifctoolbox.ifc.*;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.step.core.SET;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class IfcFilter {

    private IfcModel ifcModel;

    /**
     * Initializes the IFC model representation for further filtering operations
     * @param ifc IFC-File
     */
    public IfcFilter(Path ifc) throws Exception {
        ifcModel = new IfcModel();
        ifcModel.readStepFile(ifc.toFile());
    }

    /**
     * Filters the IFC Model for a single entity of IfcSite.
     * If multiple IfcSite entities exist, none is returned.
     * @return IfcEntity representing IfcSite
     */
    public IfcEntity filterSite() {
        Collection<IfcSite> ifcSites = ifcModel.getCollection(IfcSite.class);
        if (ifcSites.isEmpty())
            return null;
        if (ifcSites.size()>1)
            return null;
        IfcSite ifcSite = ifcSites.iterator().next();
        IfcEntity ifcEntity = new IfcEntity(ifcSite.getGlobalId().getDecodedValue(), "Site");
        ifcEntity.setDecomposedBuildingElementGUIDs(this.addFilteredAggregations((IfcProduct) ifcEntity));
        return ifcEntity;
    }

    /**
     * Filters the IFC Model for a single entity of IfcBuilding that represents a Bridge in this scenario.
     * If multiple IfcBuilding entities exist, none is returned.
     * @return IfcEntity representing the bridge (IfcBuilding)
     */
    public IfcEntity filterBridge() {
        Collection<IfcBuilding> ifcBuildings = ifcModel.getCollection(IfcBuilding.class);
        if (ifcBuildings.isEmpty())
            return null;
        if (ifcBuildings.size()>1)
            return null;
        IfcBuilding ifcBuilding = ifcBuildings.iterator().next();
        IfcEntity ifcEntity = new IfcEntity(ifcBuilding.getGlobalId().getDecodedValue(), "Bridge");
        ifcEntity.setDecomposedBuildingElementGUIDs(this.addFilteredAggregations((IfcProduct) ifcEntity));
        return ifcEntity;
    }

    /**
     * Filters the IFC Model for entities of IfcBuildingElement
     * @return ArrayList of IfcEntity
     */
    public ArrayList<IfcEntity> filterBuildingElements() {
        ArrayList<IfcEntity> entityList = new ArrayList<>();
        Collection<IfcBuildingElement> ifcBuildingElementList = ifcModel.getCollection(IfcBuildingElement.class);
        for (IfcBuildingElement ifcBuildingElement :
                ifcBuildingElementList) {
            IfcEntity ifcEntity = new IfcEntity(ifcBuildingElement.getGlobalId().getDecodedValue(), "Component");
            ifcEntity.setDecomposedBuildingElementGUIDs(this.addFilteredAggregations((IfcProduct) ifcBuildingElement));
            entityList.add(ifcEntity);
        }
        return entityList;
    }

    /**
     * Filters the IFC Model for all IfcBuidlingElement, IfcSite and IfcBuilding (for bridge representation) entities
     * @return Arraylist of IfcEntity
     */
    public ArrayList<IfcEntity> filterAllTypes() {
        ArrayList<IfcEntity> entityList = this.filterBuildingElements();
        if(this.filterBridge() != null)
            entityList.add(this.filterBridge());
        if(this.filterBridge() != null)
            entityList.add(this.filterSite());
        return entityList;
    }

    private ArrayList<String> addFilteredAggregations(IfcProduct product) {
        ArrayList<String> decomposedEntities = new ArrayList<>();
        Collection<IfcRelAggregates> aggregationList = ifcModel.getCollection(IfcRelAggregates.class);
        for (IfcRelAggregates aggregation :
                aggregationList) {
            if (aggregation.getRelatingObject().getGlobalId().getDecodedValue().equals(product.getGlobalId().getDecodedValue())) {
                SET<? extends IfcObjectDefinition> aggregatedObjects = aggregation.getRelatedObjects();
                for (IfcObjectDefinition aggregatedObject :
                        aggregatedObjects) {
                    decomposedEntities.add(aggregatedObject.getGlobalId().getDecodedValue());
                }
            }
        }
        return decomposedEntities;
    }

}
