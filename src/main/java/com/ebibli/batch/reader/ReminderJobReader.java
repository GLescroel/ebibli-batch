package com.ebibli.batch.reader;

import com.ebibli.dto.LivreDto;
import org.springframework.batch.item.support.ListItemReader;

import java.util.List;

public class ReminderJobReader extends ListItemReader<LivreDto> {

    public ReminderJobReader(List list) {
        super(list);
    }

}
