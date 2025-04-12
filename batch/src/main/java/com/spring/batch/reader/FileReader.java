package com.spring.batch.reader;

import com.spring.batch.configuration.InputFileProperties;
import com.spring.batch.demoentity.IndiaPincodes;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileReader {

//    @Value("${input.file.path}")
    private Resource inputResource;

    private final InputFileProperties inputFileProperties;

    @Bean
    public FlatFileItemReader<IndiaPincodes> reader() {
        FlatFileItemReader<IndiaPincodes> reader = new FlatFileItemReader<>();
//        reader.setResource(new ClassPathResource("inputdata/IndiaPincodes.csv"));
        reader.setResource(new ClassPathResource(inputFileProperties.getPath()));
        reader.setLinesToSkip(1); // ⬅️ This skips the header row

        DefaultLineMapper<IndiaPincodes> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("circleName", "regionName", "divisionName", "officeName", "pincode", "officeType",
                "delivery", "district", "stateName", "latitude", "longitude");

        BeanWrapperFieldSetMapper<IndiaPincodes> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(IndiaPincodes.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }
}
