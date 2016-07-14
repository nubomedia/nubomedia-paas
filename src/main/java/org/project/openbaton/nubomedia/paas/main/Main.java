/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.main;

import org.project.openbaton.nubomedia.paas.core.openshift.OpenshiftConfiguration;
import org.project.openbaton.nubomedia.paas.events.ConfigurationBeans;
import org.project.openbaton.nubomedia.paas.main.utils.BeanSchedulerConfiguration;
import org.project.openbaton.nubomedia.paas.main.utils.OpenbatonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by lto on 24/09/15.
 */
@SpringBootApplication
@ContextConfiguration(
  classes = {
    OpenshiftConfiguration.class,
    OpenbatonConfiguration.class,
    BeanSchedulerConfiguration.class,
    ConfigurationBeans.class,
  }
)
@EnableJpaRepositories("org.project.openbaton.nubomedia.paas")
@EntityScan(basePackages = "org.project.openbaton")
@ComponentScan(basePackages = "org.project.openbaton")
public class Main {
  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Main.class, args);
  }
}
