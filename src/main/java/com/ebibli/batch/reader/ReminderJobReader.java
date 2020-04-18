package com.ebibli.batch.reader;

import com.ebibli.dto.EmpruntDto;
import org.springframework.batch.item.support.ListItemReader;

import java.util.List;

public class ReminderJobReader extends ListItemReader<EmpruntDto> {

    public ReminderJobReader(List list) {
        super(list);
    }

}
