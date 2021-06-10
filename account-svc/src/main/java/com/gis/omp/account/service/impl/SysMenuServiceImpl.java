package com.gis.omp.account.service.impl;

import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.model.SysMenu;
import com.gis.omp.account.repo.SysMenuDAO;
import com.gis.omp.account.repo.SysRoleMenuDAO;
import com.gis.omp.account.service.helper.ServiceHelper;
import com.gis.omp.account.service.iface.SysMenuService;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 菜单管理Service实现类
 * @author Hongyu Jiang
 * @since  Apr. 27 2020
 * @modify leexiao 2021.06.08
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

	private static ILogger logger = SLoggerFactory.getLogger(SysMenuServiceImpl.class);//private static Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

	private final ModelMapper modelMapper;
	private final ServiceHelper serviceHelper;
	private final SysMenuDAO sysMenuDAO;
	private final SysRoleMenuDAO sysRoleMenuDAO;

	@Override
	public int save(SysMenuDto record) {
		try {
			sysMenuDAO.save(convertToModel(record));
			return 0;
		} catch(Exception e) {
			logger.error("save SysMenuDO error, {}", e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int delete(SysMenuDto record) {
		try {
			sysMenuDAO.delete(convertToModel(record));
			return 0;
		} catch (Exception e) {
			logger.error("remove SysMenuDO error, {}", e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int delete(List<SysMenuDto> records) {
		try {
			sysMenuDAO.deleteInBatch(convertToModelBatch(records));
			return 0;
		} catch (Exception e) {
			logger.error("remove SysMenuDO list error, {}", e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public SysMenuDto getById(Long id) {
		Optional<SysMenu> optional = sysMenuDAO.findById(id);
		if(optional != null && optional.isPresent()) {
			return convertToDto(optional.get());
		}
		return null;
	}

	@Override
	public PageVO<SysMenuDto> getPageList(PageRequestVO pageRequestVO) {
		PageRequest pageRequest = null;

		int pageNum = pageRequestVO.getPageNum();
		int pageSize = pageRequestVO.getPageSize();

		pageRequest = PageRequest.of(pageNum-1, pageSize);

		Page<SysMenu> page = null;

		page = sysMenuDAO.findAll(pageRequest);

		List<SysMenu> content = page.getContent();
		PageVO<SysMenuDto> menutVO = new PageVO<>();
		menutVO.setList(convertToDtoBatch(content));
		menutVO.setPageNum(page.getNumber()+1);
		menutVO.setPageSize(page.getSize());
		menutVO.setTotalPage(page.getTotalPages());
		menutVO.setTotalRow(page.getTotalElements());

		return menutVO;
	}

	@Override
	public List<SysMenuDto> findTree(String userName, int menuType) {
		List<SysMenuDto> sysMenus = new ArrayList<>();
		List<SysMenuDto> menus = findByUserName(userName);
		for (SysMenuDto menu : menus) {
			if (menu.getParentId() == null || menu.getParentId() == 0) {
				menu.setLevel(0);
				if (!exists(sysMenus, menu)) {
					sysMenus.add(menu);
				}
			}
		}
		//sysMenus.sort(((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum())));
		findChildren(sysMenus, menus, menuType);
		return sysMenus;
	}

	@Override
	public List<SysMenuDto> findTreeByName(String name, int menuType) {
		List<SysMenuDto> sysMenus = new ArrayList<>();
		List<SysMenuDto> menus = convertToDtoBatch(sysMenuDAO.findByName(name));
		for (SysMenuDto menu : menus) {
			if (menu.getParentId() == null || menu.getParentId() == 0) {
				menu.setLevel(0);
				if (!exists(sysMenus, menu)) {
					sysMenus.add(menu);
				}
			}
		}
		findChildren(sysMenus, menus, menuType);
		return sysMenus;
	}

	@Override
	public List<SysMenuDto> findByName(String name) {
		return convertToDtoBatch(sysMenuDAO.findByName(name));
	}

	@Override
	public List<SysMenuDto> findByUserName(String userName) {
		/*
		 * admin超级管理员给全部菜单权限
		 * 其他根据用户名查询对应包含的菜单信息
		 * 超级管理员默认只有admin一个,非admin用户若指定了超级管理员角色，其菜单信息会为空,无权访问。
		 * 	故，一般不应为新建立用户赋予超级管理员权限。
		 */
		/*if (userName == null || "".equals(userName) || SysConstants.ADMIN.equalsIgnoreCase(userName)) {
			return sysMenuDAO.findAll();
		}*/
		return convertToDtoBatch(sysMenuDAO.findByUserName(userName));
	}

	@Override
	public List<SysMenuDto> findByRoleId(Long roleId) {
		return convertToDtoBatch(sysMenuDAO.findByRoleId(roleId));
	}

//	@Override
//	public List<SysRoleMenuDO> findByMenuId(Long menuId) {
//		return sysRoleMenuDAO.findByMenuId(menuId);
//	}

	@Override
	public List<SysMenuDto> getListByExample(Example<SysMenu> example, Sort sort) {
		return convertToDtoBatch(sysMenuDAO.findAll(example, sort));
	}

	@Override
	public SysMenuDto getByMenuToExample(SysMenu menu) {
		return convertToDto(sysMenuDAO.findOne(Example.of(menu)).orElse(null));
	}

	@Override
	public SysMenuDto getByUrl(String url) {
		return convertToDto(sysMenuDAO.findByUrl(url));
	}


	/**
	 *
	 * @param sysMenus 父菜单实体
	 * @param menus 所有菜单实体
	 * @param menuType 菜单类型
	 */
	private void findChildren(List<SysMenuDto> sysMenus, List<SysMenuDto> menus, int menuType) {
		for (SysMenuDto sysMenu : sysMenus) {
			List<SysMenuDto> children = new ArrayList<>();
			for (SysMenuDto menu : menus) {
				if (menuType ==1 && menu.getType() == 2) {
					// 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
					continue;
				}
				if (sysMenu.getId() != null && sysMenu.getId().equals(menu.getParentId())) {
					menu.setParentName(sysMenu.getName());
					menu.setLevel(sysMenu.getLevel() + 1);
					if (!exists(children, menu)) {
						children.add(menu);
					}
				}
			}
			sysMenu.setChildren(children);
			children.sort(((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum())));
			findChildren(children, menus, menuType);
		}
	}


	/**
	 * 判断列表中是否已经存在指定菜单
	 * @param sysMenus 指定列表
	 * @param sysMenu 被判断是否包含在列表中的菜单实体
	 * @return 是否包含在列表中
	 */
	private boolean exists(List<SysMenuDto> sysMenus, SysMenuDto sysMenu) {
		boolean exist = false;
		for (SysMenuDto menu : sysMenus) {
			if (menu.getId().equals(sysMenu.getId())) {
				exist = true;
			}
		}
		return exist;
	}

	// mo dto transfer
	private SysMenuDto convertToDto(SysMenu dept) {
		if (dept == null) return null;
		return modelMapper.map(dept, SysMenuDto.class);
	}

	private SysMenu convertToModel(SysMenuDto deptDto) {
		if (deptDto == null) return null;
		return modelMapper.map(deptDto, SysMenu.class);
	}

	private List<SysMenu> convertToModelBatch(List<SysMenuDto> deptDtoList) {
		List<SysMenu> deptList = new ArrayList<>();
		for (SysMenuDto deptDto : deptDtoList) {
			deptList.add(convertToModel(deptDto));
		}

		return deptList;
	}

	private List<SysMenuDto> convertToDtoBatch(List<SysMenu> roleList) {
		List<SysMenuDto> roleDtoList = new ArrayList<>();
		for (SysMenu role : roleList) {
			roleDtoList.add(convertToDto(role));
		}

		return roleDtoList;
	}
}
