package com.canopus.interceptor;

import com.canopus.dac.*;
import com.canopus.interceptor.vo.*;

public interface TestEntityDataService extends DataAccessService
{
    public static final String ERR_SAVE_TEST_ENITITY_TWO = "ENT2_SAV-000";
    public static final String ERR_SAVE_TEST_ENITITY_ONE = "ENT1_SAV-000";
    
    Integer saveTestEntity1(final TestEntity1Data p0);
    
    Integer saveTestEntity2(final TestEntity2Data p0);
}
