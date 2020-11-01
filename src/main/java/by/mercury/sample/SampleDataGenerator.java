package by.mercury.sample;

import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class SampleDataGenerator implements ApplicationRunner, Ordered {

    private static final int PRIORITY = Ordered.LOWEST_PRECEDENCE;
    private static final Long ID = 1L;
    
    private UserService userService;
    
    private String peerId;

    public SampleDataGenerator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!"none".equals(peerId) && userService.findById(ID).isEmpty()) {
            var user = UserModel.builder()
                    .id(ID)
                    .uid(peerId)
                    .peerId(Integer.parseInt(peerId))
                    .build();
            userService.save(user);
        }
    }

    @Override
    public int getOrder() {
        return PRIORITY;
    }

    @Value("${sample.user.peer.id}")
    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }
}
