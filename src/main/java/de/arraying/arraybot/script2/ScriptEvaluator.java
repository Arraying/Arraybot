package de.arraying.arraybot.script2;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private final Set<String> imports = new HashSet<>();
    private final String code;

    /**
     * Creates a new script evaluator.
     * @param code The code.
     */
    public ScriptEvaluator(String code) {
        this.code = code;
    }

    /**
     * Allows the given class to be exposed to the script engine.
     * @param clazz The class.
     * @return This class, for chaining purposes.
     */
    public ScriptEvaluator allow(Class<?> clazz) {
        filter.whitelist(clazz);
        return this;
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
     * Imports the specified
     * @param imports A set of packages.
     * @return This classm for chaining purposes.
     */
    public ScriptEvaluator imports(Set<String> imports) {
        this.imports.addAll(imports);
        return this;
    }

    /**
     * Evaluates the code.
     */
    public void evaluate()
            throws ScriptException {
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine(filter);
        for(Map.Entry<String, Object> entry : variables.entrySet()) {
            engine.put(entry.getKey(), entry.getValue());
        }
        StringBuilder importBuilder = new StringBuilder();
        for(String imported : imports) {
            importBuilder.append(imported)
                    .append(",");
        }
        String imports = importBuilder.toString();
        if(!imports.isEmpty()) {
            imports = imports.substring(0, imports.length()-1);
        }
        engine.eval("var imports = new JavaImporter(" + imports + ")");
        String code = "function exec() {\n\twith(imports) {\n\t\t" + this.code + "\n\t}\n}exec();";
        engine.eval(code);
    }

}
