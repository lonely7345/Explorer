package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Transforms Cells to DTO
 */
public interface CellDTO {

    /**
     * Transform toDTO
     * @param cellWithTable
     * @return DTO
     */
      String toDTO(CellData cellWithTable);
}
