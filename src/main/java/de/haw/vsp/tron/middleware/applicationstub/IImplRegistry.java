package de.haw.vsp.tron.middleware.applicationstub;

import java.util.function.Function;

public interface IImplRegistry {
    
    public void registerImplementation(String methodId, Function<Object[], Object> methodReference);
}
