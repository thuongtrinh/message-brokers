package com.txt.eda.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private JwtTokenFilter jwtTokenFilter;
    // Details omitted for brevity

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();
        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Our private endpoints
//                .antMatchers("/api/v1/query/team-tasks")
//                .hasAnyAuthority(RolesConstant.ROLE_PS_MANAGER, RolesConstant.ROLE_PS_QC, RolesConstant.ROLE_PS_STAFF,
//                        RolesConstant.ROLE_UW_MANAGER, RolesConstant.ROLE_UW_QC, RolesConstant.ROLE_UW_STAFF,
//                        RolesConstant.ROLE_CL_MANAGER, RolesConstant.ROLE_CL_QC,
//                        RolesConstant.ROLE_BUSINESS_ADMIN, RolesConstant.ROLE_QA)
//                .antMatchers("/api/v1/azure/sas-token")
//                 .hasAnyAuthority(RolesConstant.ROLE_USER)
                .anyRequest().permitAll();

        // Add JWT token filter
//        http.addFilterBefore(
//                jwtTokenFilter,
//                UsernamePasswordAuthenticationFilter.class
//        );
    }
}
