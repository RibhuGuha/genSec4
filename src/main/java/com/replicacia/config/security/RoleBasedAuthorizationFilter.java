package com.replicacia.config.security;

import com.replicacia.model.AppUser;
import com.replicacia.model.Role;
import com.replicacia.model.security.UserPrincipal;
import com.replicacia.service.impl.UserDetailsServiceImpl;
import com.replicacia.rest.security.util.JwtTokenUtil;
import com.replicacia.web.ApiError;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleBasedAuthorizationFilter extends OncePerRequestFilter {

  private final UserDetailsServiceImpl jwtUserDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if(!StringUtils.isEmpty(request.getRequestURI()) && request.getRequestURI().contains("/gateway")) {
      AppUser appUser = null;
      if (SecurityContextHolder.getContext() != null
          && SecurityContextHolder.getContext().getAuthentication() != null) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        appUser = userPrincipal.getUser();
      } else {
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null) {
          if (requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
          }
        }
        if (username != null) {
          appUser = ((UserPrincipal) jwtUserDetailsService.loadUserByUsername(username)).getUser();
        }
      }

      String requestUrl = request.getRequestURI().replaceFirst("/gateway", "");

      if (!isUserAuthorized(appUser, request)) {
        log.error("User not authorized to access API: " + requestUrl);
        setErrorResponse(response, "User not authorized to access API: " + requestUrl);
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean isUserAuthorized(AppUser appUser, HttpServletRequest request){
    String api = request.getRequestURI().replaceFirst("/gateway", "");
    String method = request.getMethod();
    Set<Role> roles = appUser != null ? appUser.getRoles() : Collections.EMPTY_SET;
    return roles.stream().anyMatch(role -> role.equals(new Role(api, method)));
  }


  private void setErrorResponse(HttpServletResponse response, String message) {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(ContentType.APPLICATION_JSON.toString());

    List<String> details = new ArrayList<String>();
    details.add(message);

    ApiError err = new ApiError(LocalDateTime.now(),HttpStatus.UNAUTHORIZED, "Authorization failed" ,details);
    try {
      String json = err.convertToJson();
      response.getWriter().write(json);
    } catch (IOException e) {
      log.error("Exception in parsing ApiError",e);
    }
  }
}
