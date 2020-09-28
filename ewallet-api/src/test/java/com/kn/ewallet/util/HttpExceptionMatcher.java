package com.kn.ewallet.util;

import com.kn.ewallet.exception.HttpException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpStatus;

public class HttpExceptionMatcher extends TypeSafeMatcher<HttpException> {

    private final HttpStatus expectedStatus;
    private HttpStatus foundStatus;

    public HttpExceptionMatcher(HttpStatus expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    public static HttpExceptionMatcher hasHttpStatus(HttpStatus expectedStatus) {
        return new HttpExceptionMatcher(expectedStatus);
    }

    @Override
    protected boolean matchesSafely(HttpException exception) {
        foundStatus = exception.getStatus();
        return foundStatus == expectedStatus;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("found status code ").appendText(foundStatus.toString())
                .appendText(" instead of ").appendText(expectedStatus.toString());

    }
}
