package com.ebibli.infrastructure;

import com.ebibli.infrastructure.rest.RestConfig;
import org.springframework.context.annotation.Import;

@Import(RestConfig.class)
public class InfrastructureConfig {
}
