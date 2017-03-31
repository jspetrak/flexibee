package com.adleritech.flexibee.core.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Root(name = "adresar", strict = false)
public class AddressBook {

    @Attribute(name = "update", required = false)
    private Update update;

    @Element(name = "id", required = false)
    private String id;

    @Element(name = "ic")
    private String regNo;

    @Element(name = "psc")
    private String postCode;

    @Element(name = "stat", required = false)
    private String country;

    @Element(name = "nazev")
    private String name;

    @Element(name = "mesto")
    private String city;

    @Element(name = "dic", required = false)
    private String vatId;

    @Element(name = "platceDph", required = false)
    private Boolean paysVat;

    @Element(name = "ulice")
    private String street;

    @Element(name = "kod", required = false)
    private String code;

}
