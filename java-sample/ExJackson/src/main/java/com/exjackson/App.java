package com.exjackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

public class App {
    final Logger logger = LoggerFactory.getLogger(getClass());

    public void readObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String resource = IOUtils.toString(this.getClass().getResourceAsStream("/sample.js"), "UTF-8");
        Portal portal = mapper.readValue(resource, Portal.class);

        logger.info("done {}", portal.getPortalSites().size());
    }

    public void readJsonTree() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String resource = IOUtils.toString(this.getClass().getResourceAsStream("/sample.js"), "UTF-8");
        JsonNode root = mapper.readTree(resource);

        /// Get Sub Json Node
        JsonNode portalSites = root.path("portalSites");

        if (portalSites.isArray()) {
            /// Iterate Json Node
            for (final JsonNode portalSite : portalSites) {
                JsonNode portalNode = portalSite.get("portal");
                String portal = portalNode.asText();

                if (false == portal.equalsIgnoreCase("NAVER")) {
                    continue;
                }

                JsonNode subPages = portalSite.get("subPages");

                if (subPages.isArray()) {

                    /// Iterate Json Node
                    for (final JsonNode subPage : subPages) {
                        Iterator<Map.Entry<String, JsonNode>> nodes = subPage.fields();

                        /// Iterate Key Value
                        while (nodes.hasNext()) {
                            Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
                            logger.info("{} : {}", entry.getKey(), entry.getValue());
                        }

                        logger.info("{}" , subPage.toString());
                    }
                }
            }
        }
    }

    public static void main( String[] args ) throws Exception {
        App app = new App();
        app.readJsonTree();
    }
}
