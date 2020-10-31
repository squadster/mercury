package by.mercury.vkontakte.preprocessor;

import by.mercury.core.command.CommandPreprocessor;
import by.mercury.core.model.Channel;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public abstract class AbstractVkPreprocessor implements CommandPreprocessor {

    @Override
    public boolean support(Collection<Channel> channels) {
        return Optional.ofNullable(channels).orElseGet(Collections::emptyList).contains(Channel.VK);
    }
}
