package airfilter.config;

import airfilter.config.entity.Config;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigServiceTest {

    private ConfigService configService;

    @Before
    public void setUp() {
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
    public void shouldReturnTheSameConfigObject() {

        // given
        Config config = PredefinedConfig.create();
        configService.setConfig(config);

        // when
        Config result = configService.getConfig();

        // then
        assertThat(result).isEqualTo(config);
    }

}