package com.spiritcoder.musalalogistics.identity.filter;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.wrappers.BeanWrapper;
import com.spiritcoder.musalalogistics.identity.service.JwtService;
import com.spiritcoder.musalalogistics.identity.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements JwtAuthenticationManager {

    private JwtService jwtService;

    private UserRepository userRepository;

    //private UserDetailsService userDetailsService;

    public void authenticate(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String jwt;
        final String userEmail;
        final String authenticationHeader = getHeaderValue(request);

        if( isNullAuthenticationHeader(authenticationHeader) || !isAuthenticationHeaderStartsWithBearer(authenticationHeader) ){
            continueFilterChain(request, response, filterChain);
            return;
        }

        jwt = extractJwtToken(authenticationHeader);
        userEmail = jwtService.getUsername(jwt);

        if( isValidUserEmail(userEmail) && isEmptySecurityContext() ) {

            UserDetails userDetails = getUserDetails(userEmail);
            setSecurityContextIfTokenValid(request, userDetails, jwt);
        }

        continueFilterChain(request, response, filterChain);
    }

//    private UserDetails getUserDetails(String userEmail) {
//        return userDetailsService.loadUserByUsername(userEmail);
//    }

    private UserDetails getUserDetails(String userEmail) {
        return getUserByEmail(userEmail);
    }


    public UserDetails getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void setSecurityContextIfTokenValid(HttpServletRequest request, UserDetails userDetails, String jwt) {

        if (jwtService.isTokenValid(jwt, userDetails)) {

            UsernamePasswordAuthenticationToken authToken
                    = BeanWrapper.getAuthenticationTokenBean(userDetails);
            authToken.setDetails(BeanWrapper.getAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private boolean isEmptySecurityContext() {
         return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean isValidUserEmail(String userEmail) {
        return userEmail != null;
    }

    private String extractJwtToken(String authenticationHeader) {
        return authenticationHeader.substring(7);
    }

    private String getHeaderValue(HttpServletRequest request){
        return request.getHeader(AppConstants.AUTHORIZATION_HEADER_VALUE);
    }

    private boolean isAuthenticationHeaderStartsWithBearer(String authenticationHeader) {
        return authenticationHeader.startsWith(AppConstants.AUTHORIZATION_HEADER_PREFIX);
    }

    private boolean isNullAuthenticationHeader(String authenticationHeader) {
        return authenticationHeader == null;
    }

    public void continueFilterChain(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
