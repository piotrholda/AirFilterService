package airfilter.config;

import airfilter.config.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    ConfigService configService;

    @GetMapping()
    public Config getConfig() {
        return configService.getConfig();
    }

    @PutMapping
    public void setConfig(@RequestBody Config config) {
        configService.setConfig(config);
    }

}
