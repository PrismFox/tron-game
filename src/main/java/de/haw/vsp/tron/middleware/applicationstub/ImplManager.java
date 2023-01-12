package de.haw.vsp.tron.middleware.applicationstub;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import de.haw.vsp.tron.middleware.serverstub.IServerStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ImplManager implements IImplRegistry, IImplCaller {

    @Autowired
    private IServerStub serverStub;
    private Map<String, Function<Object[], Object>> referenceMap = new HashMap<>();
    private Map<String, boolean[]> paramPrefixMap = new HashMap<>();
    private Map<String, Boolean> asyncMethodMap = new HashMap<>();

    @Override
    public void registerSyncImplementation(String methodId, Function<Object[], Object> methodReference, boolean[] prefixedArgs) {
        referenceMap.put(methodId, methodReference);
        paramPrefixMap.put(methodId, prefixedArgs);
        asyncMethodMap.put(methodId, false);
        serverStub.registerMethod(methodId);
    }

    @Override
    public void registerAsyncImplementation(String methodId, Function<Object[], Object> methodReference, boolean[] prefixedArgs) {
        referenceMap.put(methodId, methodReference);
        paramPrefixMap.put(methodId, prefixedArgs);
        asyncMethodMap.put(methodId, true);
        serverStub.registerMethod(methodId);
    }

    @Override
    public boolean isPrefixedArg(String methodId, int index) {
        return paramPrefixMap.get(methodId)[index];
    }

    @Override
    public boolean isAsyncMethod(String methodId) {
        return asyncMethodMap.get(methodId);
    }

    @Override
    public Object callImplementation(String methodId, Object... args) {
        return referenceMap.get(methodId).apply(args);
    }
    
}
