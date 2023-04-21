package by.ciao.utils;

import by.ciao.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class RestService {

    private final RestTemplate restTemplate;
    @Value("${userdata_endpoint}")
    private String userdataEndpoint;

    public RestService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getUsers() {
        return restTemplate.exchange(
                userdataEndpoint,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {}
        ).getBody();
    }
}
