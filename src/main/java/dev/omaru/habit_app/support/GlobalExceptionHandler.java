package dev.omaru.habit_app.support;

import jakarta.validation.ConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingRequestHeaderException;


//例外を集めてここで返す役割(なくても動くがあったほうが情報漏洩のリスクなどがない品質向上につながる)
@RestControllerAdvice
public class GlobalExceptionHandler {

    //コントローラーで@Valid/@Validatedを使ったバリデーションに失敗したとき、このメソッドが呼び出される。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");//エラーのタイトルを表示
        pd.setDetail(ex.getBindingResult().toString());//どのフィールドが、どの制約に違反したか
        pd.setProperty("path", req.getRequestURI());//実際に起きたリクエストのパスをJSONに追加する
        return pd;//これでAPIクライアントにはJSONがかえるようになる
    }

    //リクエストが不正な形式だった時(Bad request400)
    //
    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentTypeMismatchException.class, ConstraintViolationException.class})
    ProblemDetail handleBadRequest(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad Request");
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    //上記の例外で取り切れなかった時のものを集約するハンドラ
    @ExceptionHandler(Exception.class)
    ProblemDetail handleOthers(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal Server Error");
        pd.setDetail("Unexpected error");
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

}
