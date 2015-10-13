/*
 * #%L
 * Wildfly Camel :: Example :: Camel JMS
 * %%
 * Copyright (C) 2013 - 2014 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wildfly.camel.examples.jms;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.jms.JmsComponent;

import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;

@Startup
@ApplicationScoped
@ContextName("jms-cdi-context")
public class JmsRouteBuilder extends RouteBuilder {

    @Resource(mappedName = "java:jboss/DefaultJMSConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Override
    public void configure() throws Exception {
        /**
         * Configure the JMSComponent to use the connection factory
         * injected into this class
         */
        JmsComponent component = new JmsComponent();
        component.setConnectionFactory(connectionFactory);

        getContext().addComponent("jms", component);

        Namespaces ns = new Namespaces("blacktie", "http://www.jboss.org/blacktie");

        from("jms:queue:BTR_CBR_Request")
            .choice()
                .when().xpath("/blacktie:employee/blacktie:id > 1000", ns)
                    .log("Sending request to the CBR_TestOne")
                    .to("jms:queue:BTR_CBR_TestOne")
                .when().xpath("/blacktie:employee/blacktie:id <= 1000", ns)
                    .log("Sending request to the CBR_TestTwo")
                    .to("jms:queue:BTR_CBR_TestTwo")
                .otherwise()
                    .log("can not send the message")
                    .to("file://{{jboss.server.data.dir}}/blacktie/cbr/messages");
    }
}
