package com.zfsoft.platform.auth.component;

import com.zfsoft.platform.auth.constant.SecurityConstants;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
			return accessToken;
		}

		final Map<String, Object> additionalInfo = new HashMap<>(8);
		AuthUserDetails userDetails = (AuthUserDetails) authentication.getUserAuthentication().getPrincipal();
		userDetails.setToken(accessToken.getValue());
		additionalInfo.put(SecurityConstants.DETAILS_USER, userDetails);
		additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.ZF_LICENSE);
		additionalInfo.put(SecurityConstants.ACTIVE, Boolean.TRUE);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}

}
