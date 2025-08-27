package dev.omaru.habit_app.support;

import  jakarta.servlet.FilterChain;
import  jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

//追跡ログ作成
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req,jakarta.servlet.http.HttpServletResponse res,FilterChain chain)
        throws ServletException, IOException {//サーブレットやフィルター処理に関する一般的なエラーと入出力エラーをthrowsする。
        String traceId = req.getHeader("X-Request-ID");//traceIdの作成
        if (traceId == null || traceId.isEmpty()) {//ここでtraceIdが必ず生成されるようにする
            traceId = UUID.randomUUID().toString();

            String userId = req.getHeader("X-User-ID");//認証導入までは暫定取得(ヘッダから直接)

            MDC.put("traceId", traceId);
            if (userId != null)MDC.put("userId", userId);
            try{
                chain.doFilter(req,res);
            }finally {
                MDC.clear();
            }
        }
    }
}

