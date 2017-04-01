package br.com.agenciacodeplus.socialcron.apis.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.RawAPIResponse;
import facebook4j.auth.AccessToken;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FacebookTokenGenerator {

  private Logger LOGGER = LoggerFactory.getLogger(FacebookTokenGenerator.class);
  
  public AccessToken refreshToken(AccessToken currentToken) {
    String clientId = "1052750751465673";
    String clientSecret = "a74c62d2d0037517db79073c52a12183";

    Facebook facebook = new FacebookFactory().getInstance(currentToken);
    
    Map<String, String> params = new HashMap<String, String>();
    params.put("client_id", clientId);
    params.put("client_secret", clientSecret);
    params.put("grant_type", "fb_exchange_token");
    params.put("fb_exchange_token", currentToken.getToken());

    RawAPIResponse apiResponse;
    try {
      apiResponse = facebook.callGetAPI("/oauth/access_token", params);
      String response = apiResponse.asJSONObject().getString("access_token");
      AccessToken newAccessToken = new AccessToken(response);
      LOGGER.debug("Facebook access token generated: " + newAccessToken.getToken());
      LOGGER.debug("Expires at: " + newAccessToken.getExpires());
      return newAccessToken;
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return null;
    }

  }
  
}