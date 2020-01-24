package by.mercury.core.command;

/**
 * Interface for preprocessor for command
 *
 * @author Yegor Ikbaev
 */
public interface CommandPreprocessor {

    /**
     * Preprocess {@link CommandContext} before execution in {@link Command}
     *
     * @param context must be not null
     */
    void preprocess(CommandContext context);

    /**
     * Determine order of execution preprocessors, the object with the lowest value has the highest priority
     *
     * @return not null {@link Integer} number
     */
    Integer getPriority();
}
