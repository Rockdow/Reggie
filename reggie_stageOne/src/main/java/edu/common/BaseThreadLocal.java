package edu.common;
/*
*LoginFilter中的doFilter方法，EmployeeController中的方法，MyMetaHandler中的公共字段处理方法都是同一个线程调用的
*而 MyMetaHandler中的公共字段处理方法无法访问HttpServlet对象，就想到用ThreadLocal，ThreadLocal并不是Thread，它是
* Thread的局部变量，为每个线程单独提供一份存储空间*/
public class BaseThreadLocal {
    public static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long get(){
        return threadLocal.get();
    }

    public static void set(Long id){
        threadLocal.set(id);
    }
}
