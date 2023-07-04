import com.lue.rasp.service.Impl.LogServiceImpl;
import com.lue.rasp.service.LogService;

public class TestLog {
    public static void main(String[] args) {
        LogServiceImpl logService = new LogServiceImpl();
        logService.getList();
    }
}
