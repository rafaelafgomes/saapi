package br.edu.ifsudestemg.saapi.config;

import br.edu.ifsudestemg.saapi.security.JwtAuthFilter;
import br.edu.ifsudestemg.saapi.security.JwtService;
import br.edu.ifsudestemg.saapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()

                .antMatchers("/api/v1/ajustesfinanceiros/**")
                .hasAnyRole("ADMIN")
                //.authenticated()
                .antMatchers("/api/v1/aulas/**")
                .permitAll()

                .antMatchers("/api/v1/cargos/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/api/v1/clientes/**")
                .permitAll()

                .antMatchers("/api/v1/equipamentos/**")
                .permitAll()

                .antMatchers("/api/v1/exercicios/**")
                .permitAll()

                .antMatchers("/api/v1/faturas/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/api/v1/fichasavaliacaofisica/**")
                .permitAll()

                .antMatchers("/api/v1/formaspagamentos/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/api/v1/funcionarios/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/api/v1/listasajustesfinanceirosfaturas/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/api/v1/listasclientesaulas/**")
                .permitAll()

                .antMatchers("/api/v1/listasequipamentosaulas/**")
                .permitAll()

                .antMatchers("/api/v1/listasexerciciostreinos/**")
                .permitAll()

                .antMatchers("/api/v1/planos/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/api/v1/treinos/**")
                .permitAll()

                .antMatchers( "/api/v1/usuarios/**")
                .permitAll()

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }


}
