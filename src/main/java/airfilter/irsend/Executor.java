package airfilter.irsend;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class Executor {

    public int execute(String device, Collection<String> codes) throws InterruptedException, IOException {
        List<String> commands = new ArrayList<>();
        commands.add("irsend");
        commands.add("SEND_ONCE");
        commands.add(device);
        commands.addAll(codes);
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(commands.toArray(new String[0]), null);
        return process.waitFor();
    }

}
