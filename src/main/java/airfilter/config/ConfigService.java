package airfilter.config;

import airfilter.AirfilterException;
import airfilter.config.entity.Absence;
import airfilter.config.entity.Config;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Service
public class ConfigService {

    private Config config;
    private ReadWriteLock lock;

    @PostConstruct
    public void init() {
        lock = new ReentrantReadWriteLock();
        setConfig(new Config());
    }

    public boolean isAbsence(LocalDateTime current) {
        return Optional.of(getConfig())
                .map(Config::getAbsences)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .anyMatch(isAbsenceMatch(current));
    }

    private Predicate<Absence> isAbsenceMatch(LocalDateTime current) {
        return a -> {
            boolean dateSet = isDateSet(current).test(a);
            boolean dateMatch = isDateInRange(current).test(a);
            boolean timeSet = isTimeSet(current).test(a);
            boolean timeMatch = isTimeInRange(current).test(a);
            boolean dayOfWeekSet = isDayOfWeekSet(current).test(a);
            boolean dayOfWeekMatch = isDayOfWeekInRange(current).test(a);
            return (!dateSet || dateMatch) && checkTime(dateSet, timeSet, timeMatch, dayOfWeekSet, dayOfWeekMatch);
        };
    }

    private boolean checkTime(boolean dateSet, boolean timeSet, boolean timeMatch, boolean dayOfWeekSet, boolean dayOfWeekMatch) {
        return (!timeSet || timeMatch) && checkDayOfWeek(dateSet, timeSet, dayOfWeekSet, dayOfWeekMatch);
    }

    private boolean checkDayOfWeek(boolean dateSet, boolean timeSet, boolean dayOfWeekSet, boolean dayOfWeekMatch) {
        return dayOfWeekSet ? dayOfWeekMatch : dateSet || timeSet;
    }

    private Predicate<Absence> isDayOfWeekSet(LocalDateTime current) {
        return a -> nonNull(a.getDaysOfWeek()) && !a.getDaysOfWeek().isEmpty();
    }

    private Predicate<Absence> isDayOfWeekInRange(LocalDateTime current) {
        return a -> a.getDaysOfWeek()
                .stream()
                .anyMatch(d -> d.equals(current.getDayOfWeek()));
    }

    private Predicate<Absence> isDateInRange(LocalDateTime current) {
        return isDateFromToday(current)
                .or(isDateFromInThePast(current)
                        .and(isDateToInTheFuture(current)
                                .or(isDateToToday(current))));
    }

    private Predicate<Absence> isTimeSet(LocalDateTime current) {
        return a -> nonNull(a.getTimeFrom()) && nonNull(a.getTimeTo());
    }

    private Predicate<Absence> isDateSet(LocalDateTime current) {
        return a -> nonNull(a.getDateFrom());
    }

    private Predicate<Absence> isTimeInRange(LocalDateTime current) {
        return a -> nonNull(a.getTimeFrom()) && !a.getTimeFrom().isAfter(current.toLocalTime())
                && nonNull(a.getTimeTo()) && !a.getTimeTo().isBefore(current.toLocalTime());
    }

    private Predicate<Absence> isDateFromToday(LocalDateTime current) {
        return a -> nonNull(a.getDateFrom()) && a.getDateFrom().isEqual(current.toLocalDate());
    }

    private Predicate<Absence> isDateToToday(LocalDateTime current) {
        return a -> nonNull(a.getDateTo()) && a.getDateTo().isEqual(current.toLocalDate());
    }

    private Predicate<Absence> isDateFromInThePast(LocalDateTime current) {
        return a -> nonNull(a.getDateFrom()) && a.getDateFrom().isBefore(current.toLocalDate());
    }

    private Predicate<Absence> isDateToInTheFuture(LocalDateTime current) {
        return a -> nonNull(a.getDateTo()) && a.getDateTo().isAfter(current.toLocalDate());
    }

    public Config getConfig() {
        Config copy;
        lock.readLock().lock();
        try {
            copy = (Config) config.clone();
        } catch (CloneNotSupportedException e) {
            throw new AirfilterException(e);
        } finally {
            lock.readLock().unlock();
        }
        return copy;
    }

    public void setConfig(Config config) {
        lock.writeLock().lock();
        try {
            this.config = config;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
