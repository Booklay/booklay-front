package com.nhnacademy.booklay.booklayfront.auth.aspect;

import com.nhnacademy.booklay.booklayfront.dto.common.PageData;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * pageData parse
 * @author
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PageDataAspect {
    @Around("@within(controller) && execution(* *.*(.., com.nhnacademy.booklay.booklayfront.dto.common.PageData, ..))")
    public Object injectPageData(ProceedingJoinPoint pjp, Controller controller) throws Throwable {

        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Map<String, String[]> paramMap = request.getParameterMap();
        PageData pageData= new PageData(readMap("page", paramMap), readMap("size", paramMap));
        Object[] args = Arrays.stream(pjp.getArgs()).map(arg -> {
            if (arg instanceof PageData) {
                arg = pageData;
            }
            return arg;
        }).toArray();
        return pjp.proceed(args);
    }

    private Integer readMap(String key, Map<String, String[]> map){
        if(map.containsKey(key)&&map.get(key).length>0){
            try{
                return Integer.parseInt(map.get(key)[0]);
            }catch (NumberFormatException e){
                return key.equals("page")?0:ControllerUtil.DEFAULT_PAGE_SIZE;
            }
        }else {
            return key.equals("page")?0:ControllerUtil.DEFAULT_PAGE_SIZE;
        }
    }
}
