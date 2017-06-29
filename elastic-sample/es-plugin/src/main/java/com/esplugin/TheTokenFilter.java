package com.esplugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by jhkwon78 on 2017-04-27.
 */
public class TheTokenFilter extends FilteringTokenFilter {
    private final Logger LOGGER = LogManager.getLogger(getClass());

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public TheTokenFilter(TokenStream in) {
        super(in);
    }

    @Override
    protected boolean accept() throws IOException {
        LOGGER.info("Term to check for 'google' {}", termAtt.toString());

        /// google 이란 키워드를 abc 로 색인한다.
        if (termAtt.toString().equals("google")) {
            termAtt.setEmpty().append("abc");
            return true;
        } else if (termAtt.toString().equals("abc")) {
            return true;
        }

        return false;
    }
}

