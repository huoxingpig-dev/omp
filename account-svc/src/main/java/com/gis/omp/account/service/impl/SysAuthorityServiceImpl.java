package com.gis.omp.account.service.impl;

import com.gis.omp.account.dto.modelDto.SysAuthorityDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.dto.request.CreateAuthorityRequest;
import com.gis.omp.account.model.SysAuthority;
import com.gis.omp.account.repo.SysAuthorityDAO;
import com.gis.omp.account.service.helper.ModelMapperHelper;
import com.gis.omp.account.service.helper.ServiceHelper;
import com.gis.omp.account.service.iface.SysAuthorityService;
import com.gis.omp.common.api.ResultCode;
import com.gis.omp.common.error.ServiceException;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SysAuthorityServiceImpl implements SysAuthorityService {
    private static ILogger logger = SLoggerFactory.getLogger(SysAuthorityService.class);

    private final ModelMapperHelper<SysAuthority, SysAuthorityDto> userAuthorityMapper = new ModelMapperHelper<>(SysAuthority.class, SysAuthorityDto.class);
    private final ServiceHelper serviceHelper;

    private final SysAuthorityDAO sysAuthorityDAO;

    @Override
    public int createAuthority(CreateAuthorityRequest record) {
        // 检测是否重名，不允许重名
        if (getByName(record.getName()) != null) {
            throw new ServiceException(ResultCode.AUTHORITY_EXIST);
        }

        SysAuthority sysAuthority = SysAuthority.builder()
                .name(record.getName())
                .desc(record.getDesc())
                .parentId(record.getParentId())
                .build();
        sysAuthority.setCreateTimeAndUser("user");

        // 写入权限数据
        try {
            sysAuthorityDAO.save(sysAuthority);
        } catch (Exception ex) {
            String errMsg = "Could not create authority";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return 0;
    }

    @Override
    public int updateAuthority(SysAuthorityDto record) {
        // 检测是否重名，不允许重名
        if (getByName(record.getName()) != null) {
            throw new ServiceException(ResultCode.AUTHORITY_EXIST);
        }

        SysAuthority sysAuthority = userAuthorityMapper.convertToModel(record);
        sysAuthority.setUpdateTimeAndUser("user");

        // 写入权限数据
        try {
            sysAuthorityDAO.save(sysAuthority);
        } catch (Exception ex) {
            String errMsg = "Could not create authority";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return 0;
    }

    @Override
    public int save(SysAuthorityDto record) {
        if (getById(record.getId()) == null) {
            CreateAuthorityRequest request = CreateAuthorityRequest.builder()
                    .name(record.getName())
                    .desc(record.getDesc())
                    .parentId(record.getParentId())
                    .build();
            return createAuthority(request);
        } else {
            return updateAuthority(record);
        }
    }

    @Override
    public List<SysAuthorityDto> save(List<SysAuthorityDto> list) {
        try {
            List<SysAuthority> userMoList = sysAuthorityDAO.saveAll(userAuthorityMapper.convertToModelBatch(list));
            return userAuthorityMapper.convertToDtoBatch(userMoList);
        } catch (Exception ex) {
            String errMsg = "Could not save user authority list";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

    @Override
    public SysAuthorityDto getByName(String name) {
        SysAuthority authority = sysAuthorityDAO.findByName(name);
        if (authority == null) {
            return null;
        } else {
            SysAuthorityDto userDto = userAuthorityMapper.convertToDto(authority);

            return userDto;
        }
    }

    @Override
    public int deleteAuthorityByName(String name) {
        SysAuthority authority = sysAuthorityDAO.findByName(name);
        if (authority != null) {
            try {
                sysAuthorityDAO.delete(authority);
            } catch (Exception ex) {
                String errMsg = "Could not delete authority";
                serviceHelper.handleException(logger, ex, errMsg);
                throw new ServiceException(errMsg, ex);
            }

        }
        return 0;
    }

    @Override
    public Boolean duplicateByUserName(SysAuthorityDto authorityDto) {
        Long id = authorityDto.getId() != null ? authorityDto.getId() : Long.MIN_VALUE;
        try {
            return sysAuthorityDAO.findByNameAndIdNot(authorityDto.getName(), id) != null;
        } catch (Exception ex) {
            String errMsg = "Could not identify duplicate authority";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

    @Override
    public List<SysAuthorityDto> listAll() {
        return userAuthorityMapper.convertToDtoBatch(sysAuthorityDAO.findAll());
    }

    @Override
    public int delete(SysAuthorityDto record) {
        sysAuthorityDAO.delete(userAuthorityMapper.convertToModel(record));
        return 0;
    }

    @Override
    public int delete(List<SysAuthorityDto> records) {
        sysAuthorityDAO.deleteInBatch(userAuthorityMapper.convertToModelBatch(records));
        return 0;
    }

    @Override
    public SysAuthorityDto getById(Long id) {

        Optional<SysAuthority> optional = sysAuthorityDAO.findById(id);
        if(optional != null && optional.isPresent()) {
            return userAuthorityMapper.convertToDto(optional.get());
        }
        return null;
    }

    @Override
    public PageVO<SysAuthorityDto> getPageList(PageRequestVO pageRequestVO) {
        return null;
    }
}
