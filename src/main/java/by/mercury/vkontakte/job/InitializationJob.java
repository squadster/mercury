package by.mercury.vkontakte.job;

import by.mercury.core.service.LoadUsersByConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Job call's all services to initialize application
 */
@Component
public class InitializationJob implements ApplicationRunner, Ordered {

    private static final int PRIORITY = Ordered.HIGHEST_PRECEDENCE;

    private LoadUsersByConversationService loadUsersByConversationService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        loadUsersByConversationService.load();
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

    @Autowired
    public void setLoadUsersByConversationService(LoadUsersByConversationService loadUsersByConversationService) {
        this.loadUsersByConversationService = loadUsersByConversationService;
    }
}
