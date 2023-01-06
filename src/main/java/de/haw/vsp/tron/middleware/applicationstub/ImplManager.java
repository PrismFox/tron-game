package de.haw.vsp.tron.middleware.applicationstub;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ImplManager implements IImplRegistry, IImplCaller {

    private Map<String, Function<Object[], Object>> referenceMap = new HashMap<>();

    @Override
    public void registerImplementation(String methodId, Function<Object[], Object> methodReference) {
        referenceMap.put(methodId, methodReference);
        //TODO: in ServerStub registrieren
    }

    @Override
    public Object callImplementation(String methodId, Object... args) {
        return referenceMap.get(methodId).apply(args);
    }
    
}
