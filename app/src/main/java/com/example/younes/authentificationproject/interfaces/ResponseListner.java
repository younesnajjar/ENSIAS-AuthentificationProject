package com.example.younes.authentificationproject.interfaces;

/**
 * Created by younes on 8/20/2018.
 */

public interface ResponseListner {
    public void onSuccessfulResponse();
    public void onFailureResponse();
    public int responsesCount();
    public void onResponsesEnd();
}
