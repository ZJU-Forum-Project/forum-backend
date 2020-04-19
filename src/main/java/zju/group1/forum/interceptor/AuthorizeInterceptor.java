package zju.group1.forum.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import zju.group1.forum.provider.RedisProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Service
public class AuthorizeInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisProvider redisProvider;

    private String httpHeaderName = "Authorization";

    private String unauthorizedErrorMessage = "401 unauthorized";

    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    public static final String REQUEST_CURRENT_KEY = "REQUEST_CURRENT_KEY";

    private void sendUnAuthorizedInfo(HttpServletResponse response) throws IOException {
        response.setStatus(unauthorizedErrorCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", response.getStatus());
        jsonObject.put("message", HttpStatus.UNAUTHORIZED);
        response.getWriter().println(jsonObject);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod))
            return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            String requestToken = request.getParameter(httpHeaderName);
            if (requestToken == null) {
                sendUnAuthorizedInfo(response);
                return false;
            }


            String authorizedName = redisProvider.getAuthorizedName(requestToken);
            if (authorizedName == null){
                sendUnAuthorizedInfo(response);
                return false;
            }
        }

        return true;
    }
}
