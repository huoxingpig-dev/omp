package com.gis.omp.account.service.impl;

import com.gis.omp.account.constants.StatusEnum;
import com.gis.omp.account.constants.SysConstants;
import com.gis.omp.account.dto.request.CreateRoleRequest;
import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.dto.modelDto.SysRoleDto;
import com.gis.omp.account.dto.modelDto.SysRoleMenuDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.model.SysMenu;
import com.gis.omp.account.model.SysRole;
import com.gis.omp.account.model.SysRoleMenuRelation;
import com.gis.omp.account.repo.SysMenuDAO;
import com.gis.omp.account.repo.SysRoleDAO;
import com.gis.omp.account.repo.SysRoleMenuDAO;
import com.gis.omp.account.service.helper.ModelMapperHelper;
import com.gis.omp.account.service.helper.ServiceHelper;
import com.gis.omp.account.service.iface.SysRoleService;
import com.gis.omp.common.api.ResultCode;
import com.gis.omp.common.error.ServiceException;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 角色管理Service实现类
 * @author Hongyu Jiang
 * @since  Apr. 27 2020
 * @modify leexiao 2021.06.08
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

	private static ILogger logger = SLoggerFactory.getLogger(SysRoleServiceImpl.class);  //private static Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

	private final ModelMapper modelMapper;
	private final ModelMapperHelper<SysMenu, SysMenuDto> userRoleMapper = new ModelMapperHelper<>(SysMenu.class, SysMenuDto.class);
	private final ModelMapperHelper<SysRoleMenuRelation, SysRoleMenuDto> roleMenuMapper = new ModelMapperHelper<>(SysRoleMenuRelation.class, SysRoleMenuDto.class);

	private final ServiceHelper serviceHelper;

	private final SysRoleDAO sysRoleDAO;
	private final SysMenuDAO sysMenuDAO;
	private final SysRoleMenuDAO sysRoleMenuDAO;

	// 创建
	@Override
	public int createRole(CreateRoleRequest record) {
		// 判断重复
		if (!findByName(record.getName()).isEmpty()) {
			throw new ServiceException(ResultCode.ROLE_EXIST);
		}

		SysRole sysRole = SysRole.builder()
				.name(record.getName())
				.remark(record.getRemark())
				.build();
		sysRole.setCreateTimeAndUser("user"); ///// SecurityContextHolder.getContext().getAuthentication().getName();

		// 写入角色数据
		try {
			sysRoleDAO.save(sysRole);
		} catch (Exception ex) {
			String errMsg = "Could not create role";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}
		return 0;
	}

	// 更新
	@Override
	public int updateRole(SysRoleDto record) {
		// 修改更新时间
		SysRole sysRole = this.convertToModel(record);
		sysRole.setUpdateTimeAndUser("user");

		// 写入用户数据
		try {
			sysRoleDAO.save(sysRole);
		} catch (Exception ex) {
			String errMsg = "Could not update user account";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}

		return 0;
	}

	// 创建/更新
	@Override
	public int save(SysRoleDto record) {
		if (record.getId() == null) {
			CreateRoleRequest request = CreateRoleRequest.builder()
					.name(record.getName())
					.remark(record.getRemark())
					.build();
			return createRole(request);
		} else {
			return updateRole(record);
		}
	}

	// 删除角色
	@Override
	public int delete(SysRoleDto record) {
		try {
			sysRoleDAO.delete(convertToModel(record));
			return 0;
		} catch (Exception ex) {
			String errMsg = "Could not remove role";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}
	}

	// 批量删除
	@Override
	public int delete(List<SysRoleDto> records) {
		try {
			sysRoleDAO.deleteInBatch(convertToModelBatch(records));
			return 0;
		} catch (Exception ex) {
			String errMsg = "Could not remove role list";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}
	}

	// 根据ID查找
	@Override
	public SysRoleDto getById(Long id) {
		try {
			Optional<SysRole> optional = sysRoleDAO.findById(id);
			if(optional != null && optional.isPresent()) {
				return convertToDto(optional.get());
			}
		} catch (Exception ex) {
			String errMsg = "Could not get role by id";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}

		return null;
	}

	@Override
	public PageVO<SysRoleDto> getPageList(PageRequestVO pageRequestVO) {
		PageRequest pageRequest = null;

		int pageNum = pageRequestVO.getPageNum();
		int pageSize = pageRequestVO.getPageSize();

		pageRequest = PageRequest.of(pageNum-1, pageSize);

		Page<SysRole> page = null;
		String name = pageRequestVO.getParamValue("name");

		if (name != null && name.length() != 0 ) {
			page = sysRoleDAO.listByName(name, pageRequest);
		} else{
			page = sysRoleDAO.findAll(pageRequest);
		}

		List<SysRoleDto> content = convertToDtoBatch(page.getContent());
		PageVO<SysRoleDto> rolePageVO = new PageVO<>();
		rolePageVO.setList(content);
		rolePageVO.setPageNum(page.getNumber()+1);
		rolePageVO.setPageSize(page.getSize());
		rolePageVO.setTotalPage(page.getTotalPages());
		rolePageVO.setTotalRow(page.getTotalElements());

		return rolePageVO;
	}

	// 列举所有角色
	@Override
	public List<SysRoleDto> listAll() {
		try {
			return convertToDtoBatch(sysRoleDAO.findAll());
		} catch (Exception ex) {
			String errMsg = "Could not list all role";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}
	}

	//　根据角色名查找
	@Override
	public List<SysRoleDto> findByName(String name) {
		return convertToDtoBatch(sysRoleDAO.findByName(name));
	}

	// 根据角色查找菜单列表
	@Override
	public List<SysMenuDto> findRoleMenus(Long roleId) {
		SysRoleDto sysRoleDO = this.getById(roleId);
		if ( SysConstants.ADMIN.equalsIgnoreCase(sysRoleDO.getName())) {
			return userRoleMapper.convertToDtoBatch(sysMenuDAO.findAll());  // 如果是超级管理员，返回全部菜单信息
		}

		return userRoleMapper.convertToDtoBatch(sysMenuDAO.findByRoleId(roleId)); // 返回角色对应的菜单信息
	}

	@Transactional
	@Override
	public int saveRoleMenus(List<SysRoleMenuDto> records) {
		if (records == null || records.isEmpty()) {
			return 0;
		}
		Long roleId = records.get(0).getRoleId();
		sysRoleMenuDAO.deleteByRoleId(roleId);
		for (SysRoleMenuDto record : records) {
			sysRoleMenuDAO.save(roleMenuMapper.convertToModel(record));
		}
		return 0;
	}

	@Override
	public Boolean existsUserOk(Long id) {
		Byte status = StatusEnum.OK.getCode();
		return  false;//return sysRoleDAO.existsByUserIdAndStatus(id, status) != null;
	}

	@Override
	public List<SysRoleDto> getListBySortOk(Sort sort) {
		return null;//return convertToDtoBatch(sysRoleDAO.findAllByDelFlag(sort, StatusEnum.OK.getCode()));
	}

	@Override
	public boolean duplicateByName(SysRoleDto role) {
		Long id = role.getId() != null ? role.getId() : Long.MIN_VALUE;
		return sysRoleDAO.findByNameAndIdNot(role.getName(), id) != null;
	}


	// mo dto transfer
	private SysRoleDto convertToDto(SysRole role) {
		if (role == null) return null;
		return modelMapper.map(role, SysRoleDto.class);
	}

	private SysRole convertToModel(SysRoleDto roleDto) {
		if (roleDto == null) return null;
		return modelMapper.map(roleDto, SysRole.class);
	}

	private List<SysRole> convertToModelBatch(List<SysRoleDto> roleDtoList) {
		List<SysRole> roleList = new ArrayList<>();
		for (SysRoleDto roleDto : roleDtoList) {
			roleList.add(convertToModel(roleDto));
		}

		return roleList;
	}

	private List<SysRoleDto> convertToDtoBatch(List<SysRole> roleList) {
		List<SysRoleDto> roleDtoList = new ArrayList<>();
		for (SysRole role : roleList) {
			roleDtoList.add(convertToDto(role));
		}

		return roleDtoList;
	}
}
