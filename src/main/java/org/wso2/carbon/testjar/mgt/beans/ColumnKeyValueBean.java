package org.wso2.carbon.testjar.mgt.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ColumnKeyValueBean {
    @XmlElement(
            name = "valueBatches",
            required = true
    )
    private Map<String , Object> valueBatches;

    public ColumnKeyValueBean() {

    }


    public Map<String , Object> getColumns() {
        return this.valueBatches;
    }
}