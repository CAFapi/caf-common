/*
 * Copyright 2015-2022 Micro Focus or one of its affiliates.
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
package com.hpe.caf.api;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * Validates a specific String key is present within a Map.
 */
public class ContainsStringKeysValidator implements ConstraintValidator<ContainsStringKeys, Map<?, ?>>
{
    String[] requiredKeys;

    @Override
    public void initialize(final ContainsStringKeys containsStringKeys)
    {
        requiredKeys = containsStringKeys.keys();

    }

    @Override
    public boolean isValid(final Map<?, ?> map, final ConstraintValidatorContext constraintValidatorContext)
    {
        for (final String requiredKey : requiredKeys) {
            if (!map.containsKey(requiredKey)) {
                return false;
            }
        }
        return true;
    }
}
