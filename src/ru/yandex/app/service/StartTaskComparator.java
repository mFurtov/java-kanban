package ru.yandex.app.service;

import ru.yandex.app.model.AbstractTask;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class StartTaskComparator implements Comparator<AbstractTask> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    @Override
    public int compare(AbstractTask o1, AbstractTask o2) {
        if (o1.getStartTime() == null || o2.getStartTime()==null){
            return 1;
        }
        LocalDateTime o1Time = LocalDateTime.parse(o1.getStartTime(), FORMATTER);
        LocalDateTime o2Time = LocalDateTime.parse(o2.getStartTime(), FORMATTER);
        int result = o1Time.compareTo(o2Time);
        if (result == 0) {
            return Integer.compare(o1.getIdTask(), o2.getIdTask());
        }
        return result;
    }
}
