package com.spring.batch.FileWriter;

import com.spring.batch.demoentity.IndiaPincodes;
import com.spring.batch.repositories.demography.IndiaPincodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchFileWriter implements ItemWriter<IndiaPincodes> {

    private final IndiaPincodesRepository pincodesRepository;
    int count;

    @Override
    public void write(Chunk<? extends IndiaPincodes> chunk) throws Exception {
        count+=chunk.size();
        pincodesRepository.saveAllAndFlush(chunk);
        System.out.println("Writing: "+ count);
    }
}

