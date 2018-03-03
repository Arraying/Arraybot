package de.arraying.arraybot.script;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2017 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class ScriptEvaluator {

    private final ClassFilter filter = new ClassFilter();
    private final Map<String, Object> variables = new HashMap<>();
    private final String code;

    /**
     * Creates a new script evaluator.
     * @param code The code.
     */
    public ScriptEvaluator(String code) {
        this.code = code;
    }

    /**
     * Predefines a variable.
     * @param name The name of the variable.
     * @param value The value of the variable.
     * @return This class, for chaining purposes.
     */
    public ScriptEvaluator variable(String name, Object value) {
        filter.whitelist(value.getClass());
        variables.put(name, value);
        return this;
    }

    /**
     * Evaluates the code.
     * @throws Exception If an error occurs.
     */
    public void evaluate()
            throws Exception {
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine(filter);
        for(Map.Entry<String, Object> entry : variables.entrySet()) {
            engine.put(entry.getKey(), entry.getValue());
        }
        String code = "(function(){" + this.code + "})();";
        engine.eval(code);
    }

}
