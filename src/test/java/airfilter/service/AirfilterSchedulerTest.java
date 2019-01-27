package airfilter.service;

import airfilter.airly.Cache;
import airfilter.config.ConfigService;
import airfilter.config.entity.Absence;
import airfilter.config.entity.Config;
import airfilter.irsend.Executor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private ConfigService configService;

    @Mock
    private Config config;

    @Mock
    private Absence absence;

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
    public void shouldNotChangeStatusWhenIsAbsenceAndThereIsPollution() throws IOException, InterruptedException {

        // given
        when(configService.isAbsence(any(LocalDateTime.class))).thenReturn(true);
        when(cache.isNormExceeded()).thenReturn(true);

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
    public void shouldTurnOnWhenIsNoAbsenceAndThereIsPollution() throws IOException, InterruptedException {

        // given
        when(configService.isAbsence(any(LocalDateTime.class))).thenReturn(false);
        when(cache.isNormExceeded()).thenReturn(true);
        when(airfilterProperties.getDeviceName()).thenReturn(DEVICE);
        when(airfilterProperties.getDeviceOn()).thenReturn(onCodes);

        // when
        airfilterScheduler.update();

        // then
        verify(executor, times(1)).execute(DEVICE, onCodes);
    }

    @Test
    public void shouldTurnOffWhenIsAbsenceAndThereIsPollution() throws IOException, InterruptedException {

        // given
        when(configService.isAbsence(any(LocalDateTime.class))).thenReturn(false).thenReturn(true);
        when(cache.isNormExceeded()).thenReturn(true);
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
    public void shouldTurnOffWhenIsAbsenceAndThereIsNoPollution() throws IOException, InterruptedException {

        // given
        when(configService.isAbsence(any(LocalDateTime.class))).thenReturn(false).thenReturn(true);
        when(cache.isNormExceeded()).thenReturn(true).thenReturn(false);
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
        when(configService.isAbsence(any(LocalDateTime.class))).thenReturn(false);
        when(cache.isNormExceeded()).thenReturn(true).thenReturn(false);
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
