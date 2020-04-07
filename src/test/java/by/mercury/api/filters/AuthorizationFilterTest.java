package by.mercury.api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorizationFilterTest {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String API_KEY = "apiKey";
    private static final String API_KEY_WITH_PREFIX = "ApiKey apiKey";
    private static final String INVALID_API_KEY_WITH_PREFIX = "ApiKey invalid";
    
    @InjectMocks
    private AuthorizationFilter testedInstance;
    
    @Mock
    private ObjectMapper mapper;
    
    @Mock
    private FilterChain filterChain;
    
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    
    @BeforeEach
    public void setUp() {
        testedInstance.setApiKey(API_KEY);
    }
    
    @Test
    public void shouldCallDoFilterIfPresent() throws ServletException, IOException {
        when(request.getHeader(eq(AUTHORIZATION_HEADER_KEY))).thenReturn(API_KEY_WITH_PREFIX);

        testedInstance.doFilterInternal(request, response, filterChain);
        
        verify(filterChain, only()).doFilter(eq(request), eq(response));
    }

    @Test
    public void shouldWriteForbiddenResponseIfNotPresent() throws ServletException, IOException {
        when(request.getHeader(eq(AUTHORIZATION_HEADER_KEY))).thenReturn(INVALID_API_KEY_WITH_PREFIX);

        testedInstance.doFilterInternal(request, response, filterChain);
        
        verify(response).setStatus(eq(HttpStatus.FORBIDDEN.value()));
        verify(response).setContentType(eq(MediaType.APPLICATION_JSON_VALUE));
    }
}