package com.exdeeplearning4j;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.ui.standalone.ClassPathResource;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Word2VecTest extends BaseTest {

    @Test
    public void _01_Word2VecTest() throws Exception {

        // Gets Path to Text file
        String filePath = new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath();

        logger.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

         /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
        t.setTokenPreProcessor(new CommonPreprocessor());

        logger.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        logger.info("Fitting Word2Vec model....");
        vec.fit();


        logger.info("Save vectors....");
        WordVectorSerializer.writeWord2VecModel(vec, "pathToSaveModel.txt");

        logger.info("Writing word vectors to text file....");
        Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel("pathToSaveModel.txt");

        // Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
        logger.info("Closest Words:");
        Collection<String> lst = word2Vec.wordsNearestSum("day", 10);
        logger.info("10 Words closest to 'day': {}", lst);
    }

    Logger logger = LoggerFactory.getLogger(getClass());
}
