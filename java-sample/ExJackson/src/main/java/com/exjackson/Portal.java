package com.exjackson;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Portal {

    List<String>    customText;
    List<PortalSite> portalSites = new ArrayList<>();

    public List<PortalSite> getPortalSites() {
        return this.portalSites;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
    public static class PortalSite {
        String portal;
        String nameText;
        List<SubPage> subPages = new ArrayList<>();

        public PortalSite() {}
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
    public static class SubPage {
        String name;
        String url;

        public SubPage() {}
    }
}
