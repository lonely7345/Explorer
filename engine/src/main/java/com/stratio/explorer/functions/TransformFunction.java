package com.stratio.explorer.functions;


public interface TransformFunction <EntryType,OutputType>{


    /**
     * Transforms entry type object in outputType object
     * @param objetcToTransform input object
     * @return object of EntryType
     */
    OutputType transform(EntryType objetcToTransform);


}
