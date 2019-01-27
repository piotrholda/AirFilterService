package airfilter.config;

import airfilter.config.entity.Absence;
import airfilter.config.entity.Config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.LinkedList;

class PredefinedConfig {

    static Config create() {
        Config config = new Config();
        config.setAbsences(new LinkedList<>());

        Absence absence1 = new Absence();
        absence1.setDateFrom(LocalDate.of(2019, Month.JANUARY, 10));
        absence1.setDateTo(LocalDate.of(2019, Month.FEBRUARY, 1));
        config.getAbsences().add(absence1);

        Absence absence2 = new Absence();
        absence2.setTimeFrom(LocalTime.of(10, 0));
        absence2.setTimeTo(LocalTime.of(20, 30));
        absence2.setDaysOfWeek(new LinkedList<>());
        absence2.getDaysOfWeek().add(DayOfWeek.MONDAY);
        absence2.getDaysOfWeek().add(DayOfWeek.TUESDAY);
        absence2.getDaysOfWeek().add(DayOfWeek.WEDNESDAY);
        config.getAbsences().add(absence2);

        return config;
    }
}
