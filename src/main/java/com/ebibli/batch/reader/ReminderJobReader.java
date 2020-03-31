package com.ebibli.batch.reader;

import com.ebibli.dto.UtilisateurDto;
import org.springframework.batch.item.support.ListItemReader;

import java.util.List;

public class ReminderJobReader extends ListItemReader<UtilisateurDto> {

    public ReminderJobReader(List list) {
        super(list);
    }

}
