package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Create CellsDTO
 */
public class CellDTOFactory {

    /**
     *  Create CEllDTO depend of type celData
     * @param cellData
     * @return CellDTO
     */
    public static CellDTO getDTO(CellData cellData){

        CellDTO dto = new CellStringDTO();
        if (cellData.getValue() instanceof Table){
            dto = new CellTableDTO();
        }
        return dto;
    }
}
