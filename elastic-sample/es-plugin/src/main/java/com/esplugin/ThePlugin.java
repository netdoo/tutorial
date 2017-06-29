package com.esplugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ThePlugin extends Plugin implements AnalysisPlugin, ActionPlugin {
    final static Logger logger = LogManager.getLogger(ThePlugin.class);
    static final String FILTER_NAME = "es-filter";

    public ThePlugin() {
        super();
        logger.info("Create The Plugin");
    }

    @Override
    public List<Class<? extends RestHandler>> getRestHandlers() {
        return Collections.singletonList(TheRestAction.class);
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return Collections.singletonMap(FILTER_NAME, TheTokenFilterFactory::new);
    }
}
