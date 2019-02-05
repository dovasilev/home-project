package home.env;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import home.base.TestBase;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EnvRegistry {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestBase.class);
    private static final String ENV_NAME = "TEST_CONFIG";
    private static Collection<EnvJson> collection;
    private static Queue<Env> freePool = new ConcurrentLinkedQueue<>();
    private static boolean inited = false;
    private static boolean freeEnvEnable = true;

    public synchronized static void init() throws Exception {
        if (inited) return;
        logger.info("Read TEST_CONFIG");
        Type collectionType = new TypeToken<Collection<EnvJson>>() {
        }.getType();
        String configContent = System.getenv(EnvRegistry.ENV_NAME);
        collection = (new Gson()).fromJson(configContent, collectionType);

        if (collection == null || collection.isEmpty()) {
            throw new Exception("Unspecified test " + EnvRegistry.ENV_NAME);
        }

        for (EnvJson item : collection) {
            freePool.add(new Env(item));
            logger.info("New env added: " + item.getUiURL());
        }
        inited = true;
    }

    public synchronized static Env getFreeEnv() {
        if (freeEnvEnable) {
            if (EnvRegistry.freePool.isEmpty()) {
                return null;
            } else {
                return freePool.poll();
            }
        }
        else return freePool.element();
    }

    public synchronized static void releaseEnv(Env env) {
        EnvRegistry.freePool.add(env);
    }

    public synchronized static Collection<EnvJson> getCollection() {
        return collection;
    }
}