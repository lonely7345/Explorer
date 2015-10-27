
package com.stratio.explorer.doubles;

import com.stratio.explorer.functions.TransformFunction;

/**
 * Created by afidalgo on 14/10/15.
 */
public class TransformFunctionDouble implements TransformFunction<String,TypeTestOneDouble> {

    @Override
    public TypeTestOneDouble transform(String objetcToTransform) {
        return new TypeTestOneDouble(objetcToTransform);
    }
}
