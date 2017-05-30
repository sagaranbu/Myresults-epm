package com.canopus.mw.manager;

import org.springframework.stereotype.*;

@Component
public class SystemStateManagerImpl implements ISystemStateManager
{
    @Override
    public SystemState getSystemState() {
        return SystemState.HEALTHY;
    }
}
