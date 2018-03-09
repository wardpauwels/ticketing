package be.ward.ticketing.camundarest;

import org.camunda.bpm.engine.rest.impl.CamundaRestResources;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class CamundaRestApi
        extends Application {
    public CamundaRestApi() {
    }

    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet();

        classes.addAll(CamundaRestResources.getResourceClasses());
        classes.addAll(CamundaRestResources.getConfigurationClasses());

        return classes;
    }
}