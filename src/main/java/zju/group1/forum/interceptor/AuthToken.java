package zju.group1.forum.interceptor;

import java.lang.annotation.*;
;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthToken {

}
