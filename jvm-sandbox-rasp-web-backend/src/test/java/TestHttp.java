import com.dtflys.forest.Forest;

import java.util.List;
import java.util.Map;

public class TestHttp {
    public static void main(String[] args) {
        Object result = Forest.get("/sandbox/default/module/http/sandbox-module-mgr/list?1=1")
                .backend("okhttp3")        // 设置后端为 okhttp3
                .host("127.0.0.1")         // 设置地址的host为 127.0.0.1
                .port(40001)                // 设置地址的端口为 8080
                .maxRetryCount(3)          // 设置请求最大重试次数为 3
                // 设置请求成功判断条件回调函数
                .successWhen((req, res) -> res.noException() && res.statusOk())
                // 执行并返回Map数据类型对象
                .execute();
        System.out.println(result);
    }
}
