package middleware.applicationstub;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class RPCHandlerImpl implements IRPCHandler {

    @Override
    public Object invokeSynchronously(String methodName, Object... args) {
        throw new NotImplementedException();
        // TODO Auto-generated method stub
    }

    @Override
    public void invokeAsynchronously(String methodName, Object... args) {
        throw new NotImplementedException();
        // TODO Auto-generated method stub
        
    }
    
}
