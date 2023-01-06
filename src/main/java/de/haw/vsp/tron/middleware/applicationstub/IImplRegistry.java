package de.haw.vsp.tron.middleware.applicationstub;

import java.util.function.Function;

public interface IImplRegistry {
    
    public void registerSyncImplementation(String methodId, Function<Object[], Object> methodReference, boolean[] prefixedArgs);
    public void registerAsyncImplementation(String methodId, Function<Object[], Object> methodReference, boolean[] prefixedArgs);
}
