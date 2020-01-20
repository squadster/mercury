package by.mercury.core.job;

/**
 * Interface for classes, which executes code periodically
 *
 * @author Yegor Ikbaev
 */
public interface Job {

    /**
     * Executes code periodically
     * <p>Annotate this method with {@link org.springframework.scheduling.annotation.Scheduled}
     */
    void execute();
}
