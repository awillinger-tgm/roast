//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package roast.beef;

import io.spring.guides.gs_producing_web_service.Country;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"item"}
)
@XmlRootElement(
        name = "getCountryResponse"
)
public class GetItemResponse {
    @XmlElement(
            required = true
    )
    protected Item item;

    public GetItemResponse() {
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Country value) {
        this.item = value;
    }
}
