package com.exelasticbatch;

import com.exelasticbatch.model.Alphabet;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class CustomItemProcessor implements ItemProcessor<List<Alphabet>, List<Alphabet>> {

    @Override
    public List<Alphabet> process(List<Alphabet> alphabets) throws Exception {

        for (Alphabet alphabet : alphabets) {
            alphabet.setName(alphabet.getName().toUpperCase());
        }

        return alphabets;
    }
}

