package airfilter.service;

import airly.client.Cache;
import irsend.executor.Executor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class AirfilterSchedulerTest {

    private static final String DEVICE = "device";

    @Mock
    private List<String> onCodes;

    @Mock
    private List<String> offCodes;

    @Mock
    private AirfilterProperties airfilterProperties;

    @Mock
    TimeService timeService;

    @Mock
    private Cache cache;

    @Mock
    private Executor executor;

    @InjectMocks
    private AirfilterScheduler airfilterScheduler;

    @Before
    public void setUp() {
        initMocks(this);
        airfilterScheduler.init();
    }

    @Test
    public void shouldNotChangeStatusWhenIsWorkingDayAbsenceAndThereIsPollution() throws IOException, InterruptedException {

        // given
        Calendar calendar = new GregorianCalendar();
        calendar.set(2019, Calendar.JANUARY, 18, 11, 15, 49);
        when(timeService.getCurrentTime()).thenReturn(calendar.getTime());
        when(cache.isNormExceeded()).thenReturn(true);
        when(airfilterProperties.getAbsenceHourFrom()).thenReturn(11);
        when(airfilterProperties.getAbsenceHourTo()).thenReturn(12);

        // when
        airfilterScheduler.update();

        // then
        verifyZeroInteractions(executor);
    }

    @Test
    public void shouldNotChangeStatusWhenThereIsNoPollution() throws IOException, InterruptedException {

        // given
        when(cache.isNormExceeded()).thenReturn(false);

        // when
        airfilterScheduler.update();

        // then
        verifyZeroInteractions(executor);
    }

    @Test
    public void shouldTurnOnWhenIsNoWorkingDayAbsenceAndThereIsPollution() throws IOException, InterruptedException {

        // given
        Calendar calendar = new GregorianCalendar();
        calendar.set(2019, Calendar.JANUARY, 18, 12, 15, 49);
        when(timeService.getCurrentTime()).thenReturn(calendar.getTime());
        when(cache.isNormExceeded()).thenReturn(true);
        when(airfilterProperties.getAbsenceHourFrom()).thenReturn(11);
        when(airfilterProperties.getAbsenceHourTo()).thenReturn(12);
        when(airfilterProperties.getDeviceName()).thenReturn(DEVICE);
        when(airfilterProperties.getDeviceOn()).thenReturn(onCodes);

        // when
        airfilterScheduler.update();

        // then
        verify(executor, times(1)).execute(DEVICE, onCodes);
    }

    @Test
    public void shouldTurnOffWhenIsWorkingDayAbsenceAndThereIsPollution() throws IOException, InterruptedException {

        // given
        Calendar presence = new GregorianCalendar();
        presence.set(2019, Calendar.JANUARY, 18, 12, 15, 49);
        Calendar absence = new GregorianCalendar();
        absence.set(2019, Calendar.JANUARY, 18, 11, 15, 49);
        when(timeService.getCurrentTime()).thenReturn(presence.getTime()).thenReturn(absence.getTime());
        when(cache.isNormExceeded()).thenReturn(true);
        when(airfilterProperties.getAbsenceHourFrom()).thenReturn(11);
        when(airfilterProperties.getAbsenceHourTo()).thenReturn(12);
        when(airfilterProperties.getDeviceName()).thenReturn(DEVICE);
        when(airfilterProperties.getDeviceOn()).thenReturn(onCodes);
        when(airfilterProperties.getDeviceOff()).thenReturn(offCodes);

        // when
        airfilterScheduler.update();
        verify(executor, times(1)).execute(DEVICE, onCodes);
        airfilterScheduler.update();

        // then
        verify(executor, times(1)).execute(DEVICE, offCodes);
    }

    @Test
    public void shouldTurnOffWhenIsWorkingDayAbsenceAndThereIsNoPollution() throws IOException, InterruptedException {

        // given
        Calendar presence = new GregorianCalendar();
        presence.set(2019, Calendar.JANUARY, 18, 12, 15, 49);
        Calendar absence = new GregorianCalendar();
        absence.set(2019, Calendar.JANUARY, 18, 11, 15, 49);
        when(timeService.getCurrentTime()).thenReturn(presence.getTime()).thenReturn(absence.getTime());
        when(cache.isNormExceeded()).thenReturn(true).thenReturn(false);
        when(airfilterProperties.getAbsenceHourFrom()).thenReturn(11);
        when(airfilterProperties.getAbsenceHourTo()).thenReturn(12);
        when(airfilterProperties.getDeviceName()).thenReturn(DEVICE);
        when(airfilterProperties.getDeviceOn()).thenReturn(onCodes);
        when(airfilterProperties.getDeviceOff()).thenReturn(offCodes);

        // when
        airfilterScheduler.update();
        verify(executor, times(1)).execute(DEVICE, onCodes);
        airfilterScheduler.update();

        // then
        verify(executor, times(1)).execute(DEVICE, offCodes);
    }

    @Test
    public void shouldTurnOffWhenThereIsNoPollution() throws IOException, InterruptedException {

        // given
        Calendar presence = new GregorianCalendar();
        presence.set(2019, Calendar.JANUARY, 18, 12, 15, 49);
        when(timeService.getCurrentTime()).thenReturn(presence.getTime());
        when(cache.isNormExceeded()).thenReturn(true).thenReturn(false);
        when(airfilterProperties.getAbsenceHourFrom()).thenReturn(11);
        when(airfilterProperties.getAbsenceHourTo()).thenReturn(12);
        when(airfilterProperties.getDeviceName()).thenReturn(DEVICE);
        when(airfilterProperties.getDeviceOn()).thenReturn(onCodes);
        when(airfilterProperties.getDeviceOff()).thenReturn(offCodes);

        // when
        airfilterScheduler.update();
        verify(executor, times(1)).execute(DEVICE, onCodes);
        airfilterScheduler.update();

        // then
        verify(executor, times(1)).execute(DEVICE, offCodes);
    }

}