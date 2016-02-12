/*
 * Copyright 2011-2014 the original author or authors.
 *
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
 */
package org.socialsignin.spring.data.dynamodb.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for Spring Data DynamoDB configuration using JavaConfig.
 * Use {@link org.springframework.data.mongodb.config.AbstractMongoConfiguration} as the starting template
 *
 * @author Vito Limandibhrata
 */
@Configuration
public abstract class AbstractDynamoDBConfiguration {

    public abstract AmazonDynamoDB amazonDynamoDB();

    public abstract AWSCredentials amazonAWSCredentials();

    /**
     * Return the base package to scan for mapped {@link DynamoDBTable}s. Will return the package name of the configuration
     * class' (the concrete class, not this one here) by default. So if you have a {@code com.acme.AppConfig} extending
     * {@link AbstractDynamoDBConfiguration} the base package will be considered {@code com.acme} unless the method is
     * overriden to implement alternate behaviour.
     *
     * @return the base package to scan for mapped {@link DynamoDBTable} classes or {@literal null} to not enable scanning for
     *         entities.
     */
    protected String getMappingBasePackage() {

        Package mappingBasePackage = getClass().getPackage();
        return mappingBasePackage == null ? null : mappingBasePackage.getName();
    }

    /**
     * Creates a {@link DynamoDBMappingContext} equipped with entity classes scanned from the mapping base package.
     *
     * @see #getMappingBasePackage()
     * @return
     * @throws ClassNotFoundException
     */
    @Bean
    public DynamoDBMappingContext dynamoDBMappingContext() throws ClassNotFoundException {

        DynamoDBMappingContext mappingContext = new DynamoDBMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());

        return mappingContext;
    }

    /**
     * Scans the mapping base package for classes annotated with {@link DynamoDBTable}.
     *
     * @see #getMappingBasePackage()
     * @return
     * @throws ClassNotFoundException
     */
    protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {

        String basePackage = getMappingBasePackage();

        Set<Class<?>> initialEntitySet = new HashSet<Class<?>>();

        if (StringUtils.hasText(basePackage)) {
            ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
                    false);
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(DynamoDBTable.class));

            for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
                initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(),
                        AbstractDynamoDBConfiguration.class.getClassLoader()));
            }
        }

        return initialEntitySet;
    }

}
