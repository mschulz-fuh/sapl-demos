package io.sapl.demo.jwt.clientapplication.web;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class AuthorizationController {
	private final WebClient webClient;
	private final String messagesBaseUri;

	public AuthorizationController(WebClient webClient, @Value("${miskatonic.base-uri}") String messagesBaseUri) {
		this.webClient = webClient;
		this.messagesBaseUri = messagesBaseUri;
	}

	@GetMapping(value = "/authorize", params = "grant_type=authorization_code")
	public String authorizationCodeGrant(Model model,
			@RegisteredOAuth2AuthorizedClient("miskatonic-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {

		String[] books = fetchWithAttributes("/books", oauth2AuthorizedClient(authorizedClient));
		model.addAttribute("books", books);

		String[] faculty = fetchWithAttributes("/faculty", oauth2AuthorizedClient(authorizedClient));
		model.addAttribute("faculty", faculty);

		String[] bestiary = fetchWithAttributes("/bestiary", oauth2AuthorizedClient(authorizedClient));
		model.addAttribute("bestiary", bestiary);

		return "index";
	}

	// '/authorized' is the registered 'redirect_uri' for authorization_code
	@GetMapping(value = "/authorized", params = OAuth2ParameterNames.ERROR)
	public String authorizationFailed(Model model, HttpServletRequest request) {
		String errorCode = request.getParameter(OAuth2ParameterNames.ERROR);
		if (StringUtils.hasText(errorCode)) {
			model.addAttribute("error",
					new OAuth2Error(errorCode, request.getParameter(OAuth2ParameterNames.ERROR_DESCRIPTION),
							request.getParameter(OAuth2ParameterNames.ERROR_URI)));
		}

		return "index";
	}

	@GetMapping(value = "/authorize", params = "grant_type=client_credentials")
	public String clientCredentialsGrant(Model model) {

		String[] books = fetchWithAttributes("/books", clientRegistrationId("miskatonic-client-client-credentials"));
		model.addAttribute("books", books);

		String[] faculty = fetchWithAttributes("/faculty",
				clientRegistrationId("miskatonic-client-client-credentials"));
		model.addAttribute("faculty", faculty);

		String[] bestiary = fetchWithAttributes("/bestiary",
				clientRegistrationId("miskatonic-client-client-credentials"));
		model.addAttribute("bestiary", bestiary);

		return "index";
	}

	private String[] fetchWithAttributes(String path, Consumer<Map<String, Object>> attributesConsumer) {
		return this.webClient.get().uri(this.messagesBaseUri + path).attributes(attributesConsumer).retrieve()
				.bodyToMono(String[].class).block();
	}
}
