package airfilter.config;

import airfilter.config.entity.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfigController.class})
@WebAppConfiguration
@EnableWebMvc
public class ConfigControllerTest {

    @MockBean
    private ConfigService configService;

    private Config config;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Before
    public void setUp() {
        config = PredefinedConfig.create();
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void shouldReturnConfigObject()
            throws Exception {

        // given
        when(configService.getConfig()).thenReturn(config);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/config")
                .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        String json = mvcResult.getResponse().getContentAsString();
        Config result = new ObjectMapper().readValue(json, Config.class);
        assertThat(result).isEqualTo(config);
    }

    @Test
    public void shouldUpdateConfigObject() throws Exception {

        // when
        mockMvc.perform(put("/config")
                .content(new ObjectMapper().writeValueAsString(config))
                .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        verify(configService, times(1)).setConfig(eq(config));
    }

}