package uz.community.javacommunity;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithJwtUserSecurityContextFactory.class)
public @interface WithAuthentication {

    String username() default "test@test.com";

    String[] roles() default {"ROLE_USER"};

    int id() default 1;

}