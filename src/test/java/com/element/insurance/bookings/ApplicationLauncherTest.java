package com.element.insurance.bookings;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.UnknownHostException;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class ApplicationLauncherTest
{
    @Mock
    private ConfigurableApplicationContext context;

    @Mock
    private ConfigurableEnvironment environment;

    @Test
    public void testMainShouldExecuteWithoutErrors() throws UnknownHostException
    {
        String[] args = new String[0];
        PowerMockito.mockStatic(SpringApplication.class);
        Mockito.when(SpringApplication.run(ApplicationLauncher.class, args)).thenReturn(context);
        Mockito.when(context.getEnvironment()).thenReturn(environment);
        ApplicationLauncher app = new ApplicationLauncher();
        assertNotNull("Application can be constructed.", app);
        ApplicationLauncher.main(args);
        Assert.assertEquals(context.getEnvironment(), environment);
    }
}