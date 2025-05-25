package com.spring.batch.reader;

import com.spring.batch.configuration.CustomFieldSetMapper;
import com.spring.batch.demoentity.PopulationCensusIndia;
import com.spring.batch.mapper.CleanPopulationCensusFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class GenericFlatFileReaderFactory {

    public <T> FlatFileItemReader<T> buildReader(String filePath,
                                                 Class<T> type,
                                                 String[] columnNames,
                                                 int linesToSkip) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(filePath));
        reader.setLinesToSkip(linesToSkip);

        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(columnNames);
        lineMapper.setLineTokenizer(tokenizer);
        if (type == PopulationCensusIndia.class) {
            lineMapper.setFieldSetMapper((FieldSetMapper<T>) new CleanPopulationCensusFieldSetMapper());
        } else {
            // Default mapper for all other types
            BeanWrapperFieldSetMapper<T> mapper = new BeanWrapperFieldSetMapper<>();
            mapper.setTargetType(type);
            lineMapper.setFieldSetMapper(new CustomFieldSetMapper<>(type));
        }
        reader.setLineMapper(lineMapper);
        return reader;
    }
}

