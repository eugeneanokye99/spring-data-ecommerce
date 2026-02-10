package com.shopjoy.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for MapStruct integration with Spring.
 * Enables automatic discovery and injection of MapStruct mappers.
 */
@Configuration
@ComponentScan(basePackages = "com.shopjoy.dto.mapper.mapstruct")
public class MapStructConfig {
    
    /*
     * MapStruct mappers are automatically discovered and registered as Spring components
     * due to the @Mapper(componentModel = "spring") annotation.
     * 
     * This configuration class ensures proper component scanning for the mapstruct package.
     * 
     * Benefits of MapStruct over manual mapping:
     * 1. Type Safety: Compile-time checking prevents runtime mapping errors
     * 2. Performance: Generated code is faster than reflection-based mapping
     * 3. Maintainability: Reduces boilerplate code significantly  
     * 4. IDE Support: Full IDE support with auto-completion and refactoring
     * 5. Debugging: Generated mappers are debuggable and inspectable
     */
}