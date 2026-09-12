package com.project.NexPay.merchant.security;

import com.project.NexPay.comman.Constants;
import com.project.NexPay.merchant.cache.ApiKeyCache;
import com.project.NexPay.merchant.cache.entry.ApiKeyCacheEntry;
import com.project.NexPay.merchant.entity.ApiKey;
import com.project.NexPay.merchant.mapper.ApiKeyMapper;
import com.project.NexPay.merchant.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.project.NexPay.comman.Constants.Security.AUTHORIZATION_HEADER;
import static com.project.NexPay.comman.Constants.Security.BASIC_PREFIX;

@Component
@RequiredArgsConstructor
@Slf4j
public class  ApiKeyAuthenticationFilter extends OncePerRequestFilter {


    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    private final MerchantContext merchantContext;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final ApiKeyCache apiKeyCache;
    private final ApiKeyMapper apiKeyMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming Request: {}", request.getRequestURI());

        try {

            final String authorizationHeader= request.getHeader(AUTHORIZATION_HEADER);

            if(authorizationHeader ==null || !authorizationHeader.startsWith(BASIC_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }

            // Authorization : Basic key:secret -> format
            // Authorization : Basic base64Encoded(key:secret) -> actually stored and transferred in this format

            String[] credentials = decode(authorizationHeader);
            if(credentials == null) {
                throw new BadRequestException("Malformed API Key Header");
            }

            String keyId = credentials[0];
            String rawSecret = credentials[1];

            ApiKeyCacheEntry apiKeyEntry = apiKeyCache.get(keyId).orElseGet(() -> loadAndCache(keyId));

            if(apiKeyEntry == null || !apiKeyEntry.enabled() || !secretMatches(rawSecret,apiKeyEntry)){
                throw new BadRequestException("Invalid or Missing API Key");
            }

            var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                     List.of(new SimpleGrantedAuthority("API_KEY_ROLE")));

            SecurityContextHolder.getContext().setAuthentication(auth);
            merchantContext.setMerchantId(apiKeyEntry.merchantId());
            merchantContext.setKeyId(apiKeyEntry.keyId());

            filterChain.doFilter(request,response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request,response,null,e);
        }

    }

    private ApiKeyCacheEntry loadAndCache(String keyId) {

        ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElse(null);
        if(apiKey == null)  return null;

        ApiKeyCacheEntry apiKeyCacheEntry = new ApiKeyCacheEntry(
                apiKey.getKeyId(),
                apiKey.getKeySecretHash(),
                apiKey.getPreviousKeySecretHash(),
                apiKey.getGracePeriodExpiresAt(),
                apiKey.getMerchant().getId(),
                apiKey.getEnvironment(),
                apiKey.isEnabled()
        );

        apiKeyCache.put(keyId,apiKeyCacheEntry);
        return apiKeyCacheEntry;

    }

    private boolean secretMatches(String rawSecret, ApiKeyCacheEntry apiKey){

        if(passwordEncoder.matches(rawSecret,apiKey.keySecretHash())) return true;

        boolean isInGracePeriod = apiKey.gracePeriodExpiresAt() != null &&
                LocalDateTime.now().isBefore(apiKey.gracePeriodExpiresAt());
        // secret is replaced but is in grace period
        return isInGracePeriod &&
                apiKey.previousKeySecretHash() != null &&
                passwordEncoder.matches(rawSecret,apiKey.previousKeySecretHash());

    }
    private String[] decode(String header){
        String encoded = header.split(" ")[1];
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        if(decoded.indexOf(":") < 1)    return null;

        return decoded.split(":"); // output -> [[key],[secret]]
    }
}
