package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;

import java.util.ArrayList;
import java.util.List;

public class PathCalculatorListBuilder {

    /**
     * Build list with all pathCalculators
     * @return List with path Calculators
     */
    public static List<PathCalculator> build(){
        List<PathCalculator> listCalculators = new ArrayList<>();
        listCalculators.add(new NormalPathCalculator(ConstantsFolder.CT_FOLDER_CONFIGURATION));
        listCalculators.add(new EnvironmentPathCalculator(ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV));
        return listCalculators;
    }
}
