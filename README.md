# Bridge Ontology

An onotology for representing a bridge construction. Thereby, the focus lies on providing all necessary classes and properties for describing an existing bridge during maintenance or inspection processes.

For the core ontology, BROT (Bridge Topology Ontology) is used (see: https://github.com/kozak-taras/bridge_ontology), which defines the topology between bridge components and spatial zones (e.g. superstructure or substructure).
The ontology is then extended using ontologies e.g. for defining specific components, bridge types or providing structural analysis information.

Furthermore, an ABox has been created, which describes an existing box-girder bridge made of concrete. Additionally, an IFC Model is provided. In this regard, the construction components of the bridge are related to the IfcBuildingElements. To define these links explicitly, an Information Container for Data Drop (ICDD) (https://www.iso.org/standard/74389.html) has been serialized which contains a linkset for both models.
