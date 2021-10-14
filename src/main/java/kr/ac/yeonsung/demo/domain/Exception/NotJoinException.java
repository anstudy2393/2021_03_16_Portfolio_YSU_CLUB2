package kr.ac.yeonsung.demo.domain.Exception;

//=====동아리 가입불가 Exception=====//
public class NotJoinException extends RuntimeException{

    public NotJoinException() { }

    public NotJoinException(String message) { super(message); }

    public NotJoinException(String message, Throwable cause) { super(message, cause); }

    public NotJoinException(Throwable cause) { super(cause); }

}
