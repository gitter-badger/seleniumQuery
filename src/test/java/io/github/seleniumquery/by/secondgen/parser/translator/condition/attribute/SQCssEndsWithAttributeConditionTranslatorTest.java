/*
 * Copyright (c) 2015 seleniumQuery authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.seleniumquery.by.secondgen.parser.translator.condition.attribute;

import io.github.seleniumquery.by.secondgen.csstree.condition.attribute.SQCssEndsWithAttributeCondition;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class SQCssEndsWithAttributeConditionTranslatorTest {

    @Test
    public void translate() {
        AttributeConditionTestUtils.verifySelectorYieldsAttrCondition(SQCssEndsWithAttributeCondition.class, "[attr$=\"end\"]", "attr", is("end"));
    }

}