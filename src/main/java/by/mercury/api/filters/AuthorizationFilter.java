package by.mercury.api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String API_KEY_PREFIX = "ApiKey";
    
    private static final String ERROR_KEY = "error";
    private static final String ERROR_VALUE = "Authorization token is invalid";
    private static final String MESSAGE_KEY = "message";
    private static final String MESSAGE_VALUE = "Failed to authenticate";
    
    private String apiKey;
    
    private ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isAuthorized(request)) {
            filterChain.doFilter(request, response);
        } else {
            var errorDetails = new HashMap<>();
            errorDetails.put(ERROR_KEY, ERROR_VALUE);
            errorDetails.put(MESSAGE_KEY, MESSAGE_VALUE);
            
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);
        }
    }

    private boolean isAuthorized(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER_KEY))
                .filter(header -> header.startsWith(API_KEY_PREFIX) && header.endsWith(apiKey))
                .isPresent();
    }

    @Value("${api.key}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
