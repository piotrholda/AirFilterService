package airfilter.config;

import airfilter.AirfilterException;
import airfilter.config.entity.Config;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.*;

@Service
public class ConfigService {

    private Config config;
    private ReadWriteLock lock;

    @PostConstruct
    public void init() {
        lock = new ReentrantReadWriteLock();
        setConfig(new Config());
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
