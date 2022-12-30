package middleware.clientstub;

public interface IClientStub {
    
    public Object invokeSynchronously(String methodName, Class<?> returnType, Object... args);
}
