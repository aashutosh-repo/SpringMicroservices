package com.spring.batch.processor;

import com.spring.batch.demoentity.IndiaPincodes;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FileProcessor implements ItemProcessor<IndiaPincodes, IndiaPincodes> {

    @Override
    public IndiaPincodes process(IndiaPincodes item) {
        if(!Objects.equals(item.getOfficeType(), "BO")){
            return item;
        }
        return null;
    }
}
