package by.mercury.vkontakte.runner;

import by.mercury.core.service.LoadUsersByConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Job call's all services to initialize application
 */
@Component
public class InitializationApplicationRunner implements ApplicationRunner, Ordered {

    private static final int PRIORITY = Ordered.HIGHEST_PRECEDENCE;

    private boolean runAutoloadingUsers;
    
    private LoadUsersByConversationService loadUsersByConversationService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        if (runAutoloadingUsers) {
            loadUsersByConversationService.load();
        }
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority
     *
     * @return the order value
     */
    @Override
    public int getOrder() {
        return PRIORITY;
    }

    @Value("${run.autoloading.users}")
    public void setRunAutoloadingUsers(boolean runAutoloadingUsers) {
        this.runAutoloadingUsers = runAutoloadingUsers;
    }

    @Autowired
    public void setLoadUsersByConversationService(LoadUsersByConversationService loadUsersByConversationService) {
        this.loadUsersByConversationService = loadUsersByConversationService;
    }
}
