package io.cloudslang.content.excel.entities;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;

/**
 * Created by alexandra boicu 20/2/2019
 */
public class DeleteCellInputs {
    private final ExcelCommonInputs commonInputs;
    private final String rowIndex;
    private final String columnIndex;

    @ConstructorProperties({"commonInputs","rowIndex","columnIndex"})
    private DeleteCellInputs(ExcelCommonInputs commonInputs,String rowIndex,String columnIndex){
        this.columnIndex=columnIndex;
        this.rowIndex=rowIndex;
        this.commonInputs=commonInputs;
    }

    @NotNull
    public static DeleteCellInputsBuilder builder(){return new DeleteCellInputsBuilder();}


    @NotNull
    public ExcelCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getRowIndex() {
        return rowIndex;
    }

    @NotNull
    public String getColumnIndex() {
        return columnIndex;
    }

    public static class DeleteCellInputsBuilder {
        private ExcelCommonInputs commonInputs;
        private String rowIndex;
        private String columnIndex;

        @NotNull
        public DeleteCellInputsBuilder commonInputs(@NotNull final ExcelCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public DeleteCellInputsBuilder rowIndex(@NotNull final String rowIndex){
            this.rowIndex=rowIndex;
            return this;
        }

        @NotNull
        public DeleteCellInputsBuilder columnIndex(@NotNull final String columnIndex){
            this.columnIndex=columnIndex;
            return this;
        }

        public DeleteCellInputs build(){
            return new DeleteCellInputs(commonInputs,rowIndex,columnIndex);
        }
    }

}
