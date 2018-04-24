package be.ward.ticketing.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SQL_USERS_BY_USERNAME_QUERY =
            "SELECT username, password, true AS enabled FROM user WHERE username = ?;";
    private static final String SQL_AUTHORITIES_BY_USERNAME_QUERY =
            "SELECT username, role AS authority FROM user JOIN role ON user.role_id = role.role_id WHERE username = ?;";
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          @Qualifier("dataSource") DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/tickets", "/login")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers("/**")
                .hasAuthority("ADMIN");
        http
                .formLogin()                // login
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index");
        http
                .logout()                   // logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index");
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery(SQL_USERS_BY_USERNAME_QUERY)
                .authoritiesByUsernameQuery(SQL_AUTHORITIES_BY_USERNAME_QUERY);
    }
}