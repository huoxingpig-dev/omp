package com.gis.omp.account.service.helper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ModelMapperHelper<MO,DTO> {

    private ModelMapper modelMapper;

    private Class<DTO> clazzDto;

    private Class<MO> clazzMo;

    public ModelMapperHelper(Class<MO> clazzMo, Class<DTO> clazzDto) {
        this.clazzDto = clazzDto;
        this.clazzMo = clazzMo;
        modelMapper = new ModelMapper();
    }

    // mo dto transfer
    public DTO convertToDto(MO mo) {
        if (mo == null) return null;
        return modelMapper.map(mo, clazzDto);
    }

    public MO convertToModel(DTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, clazzMo);
    }

    public List<MO> convertToModelBatch(List<DTO> dtoList) {
        List<MO> moList = new ArrayList<>();
        for (DTO dto : dtoList) {
            moList.add(convertToModel(dto));
        }

        return moList;
    }

    public List<DTO> convertToDtoBatch(List<MO> moList) {
        List<DTO> dtoList = new ArrayList<>();
        for (MO mo : moList) {
            dtoList.add(convertToDto(mo));
        }

        return dtoList;
    }


    // useless
    static class ClassHelper<T> {
        public Class<T> getObjectClass() {
            Type type = this.getClass().getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type[] types = parameterizedType.getActualTypeArguments();
            return (Class<T>)types[0];
        }
    }
}
