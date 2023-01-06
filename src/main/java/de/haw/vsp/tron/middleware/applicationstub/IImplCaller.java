package de.haw.vsp.tron.middleware.applicationstub;

public interface IImplCaller {
    public Object callImplementation(String methodId, Object... args);
}
