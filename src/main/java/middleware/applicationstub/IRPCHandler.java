package middleware.applicationstub;

public interface IRPCHandler {
    public Object invokeSynchronously(String methodName, Object... args);
    public void invokeAsynchronously(String methodName, Object... args);
}
