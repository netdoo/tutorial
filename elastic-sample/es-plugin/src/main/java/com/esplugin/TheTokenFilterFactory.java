package com.esplugin;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

/**
 * Created by jhkwon78 on 2017-04-27.
 */
public class TheTokenFilterFactory extends AbstractTokenFilterFactory {
    public TheTokenFilterFactory(IndexSettings indexSettings,
                                    Environment environment,
                                    String name,
                                    Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new TheTokenFilter(tokenStream);
    }
}
