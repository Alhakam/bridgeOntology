package de.tud.cib.IFCToBROTConverter.annotation;

import de.tud.cib.IFCToBROTConverter.ifcFilter.IfcEntity;

/**
 * Representation of an annotated element
 */
public class AnnotatedIfcEntity extends IfcEntity {

    private String ifcID;
    private String stepID;
    private String annotation;

    public AnnotatedIfcEntity(String ifcID, String stepID, String annotation) {
        super(ifcID, annotation);
        this.stepID = stepID;
        this.annotation = annotation;
    }

    public AnnotatedIfcEntity() {}

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setStepID(String stepID) {
        this.stepID = stepID;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getStepID() {
        return stepID;
    }
}
