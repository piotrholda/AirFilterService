package airfilter.config;

import airfilter.config.entity.Absence;
import airfilter.config.entity.Config;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.time.DayOfWeek.*;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConfigServiceTest {

    private ConfigService configService;

    @Mock
    private Config config;

    @Mock
    private Absence absence;

    @Before
    public void setUp() throws CloneNotSupportedException {
        initMocks(this);
        when(config.clone()).thenReturn(config);
        configService = new ConfigService();
        configService.init();
    }

    @Test
    public void shouldReturnDefaultObject() {

        // given
        Config config = new Config();

        // when
        Config result = configService.getConfig();

        // then
        assertThat(result).isEqualTo(config);
    }

    @Test
    public void shouldReturnEqualConfigObject() {

        // given
        Config config = PredefinedConfig.create();
        configService.setConfig(config);

        // when
        Config result = configService.getConfig();

        // then
        assertThat(result).isEqualTo(config);
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsToday() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsInThePastAndDateToIsNull() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsInThePastAndDateToIsInTheFuture() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDateTo()).thenReturn(LocalDate.of(2019, JANUARY, 11));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsInThePastAndDateToIsToday() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDateTo()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsTodayAndTimeIsNow() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(13, 0, 0));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsTodayAndTimeIsBeforeNow() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(11, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(12, 0, 0));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsTodayAndTimeIsAfterNow() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(14, 0, 0));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenTimeIsNow() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(13, 0, 0));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldNotReturnAbsenceWhenTimeIsBeforeNow() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(11, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(12, 0, 0));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenTimeIsAfterNow() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(14, 0, 0));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsTodayAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsInThePastAndDateToIsNullAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsInThePastAndDateToIsInTheFutureAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDateTo()).thenReturn(LocalDate.of(2019, JANUARY, 11));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsInThePastAndDateToIsTodayAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDateTo()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsTodayAndTimeIsNowAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsTodayAndTimeIsBeforeNowAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(11, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsTodayAndTimeIsAfterNowAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(14, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenTimeIsNowAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isTrue();
    }

    @Test
    public void shouldNotReturnAbsenceWhenTimeIsBeforeNowAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(11, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenTimeIsAfterNowAndDayOfWeekMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(14, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(THURSDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsTodayAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsInThePastAndDateToIsNullAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsInThePastAndDateToIsInTheFutureAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDateTo()).thenReturn(LocalDate.of(2019, JANUARY, 11));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsInThePastAndDateToIsTodayAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 9));
        when(absence.getDateTo()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenDateFromIsTodayAndTimeIsNowAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsTodayAndTimeIsBeforeNowAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(11, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenDateFromIsTodayAndTimeIsAfterNowAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getDateFrom()).thenReturn(LocalDate.of(2019, JANUARY, 10));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(14, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldReturnAbsenceWhenTimeIsNowAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenTimeIsBeforeNowAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(11, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(12, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

    @Test
    public void shouldNotReturnAbsenceWhenTimeIsAfterNowAndDayOfWeekDontMatch() {

        // given
        LocalDateTime current = LocalDateTime.of(2019, JANUARY, 10, 12, 38, 50);
        when(config.getAbsences()).thenReturn(Lists.newArrayList(absence));
        when(absence.getTimeFrom()).thenReturn(LocalTime.of(13, 0, 0));
        when(absence.getTimeTo()).thenReturn(LocalTime.of(14, 0, 0));
        when(absence.getDaysOfWeek()).thenReturn(list(WEDNESDAY, FRIDAY));
        configService.setConfig(config);

        // when
        boolean isAbsence = configService.isAbsence(current);

        // then
        assertThat(isAbsence).isFalse();
    }

}
