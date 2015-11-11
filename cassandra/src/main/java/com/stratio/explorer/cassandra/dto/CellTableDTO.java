package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.Table;
import org.apache.commons.lang.StringUtils;

/**
 * Transform to DTO cell with Table value
 */
public class CellTableDTO implements CellDTO {


    /**
     * Transform to DTO cell with Table value.
     * @param cellWithTable
     * @return DTO of table
     */
    @Override
    public String toDTO(CellData cellWithTable) {
        Table table = (Table)cellWithTable.getValue();
        return StringUtils.join(table.header(), StringConstants.TABULATOR) + System.getProperty("line.separator") + new RowsDTO().toDTO(table.rows());
    }
}
