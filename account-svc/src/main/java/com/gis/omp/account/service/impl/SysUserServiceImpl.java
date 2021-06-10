package com.gis.omp.account.service.impl;

import com.gis.omp.account.constants.StatusEnum;
import com.gis.omp.account.dto.request.CreateUserRequest;
import com.gis.omp.account.dto.modelDto.SysDeptDto;
import com.gis.omp.account.dto.modelDto.SysMenuDto;
import com.gis.omp.account.dto.modelDto.SysUserDto;
import com.gis.omp.account.dto.modelDto.SysUserRoleDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.model.SysRole;
import com.gis.omp.account.model.SysUser;
import com.gis.omp.account.model.SysUserRoleRelation;
import com.gis.omp.account.repo.SysRoleDAO;
import com.gis.omp.account.repo.SysUserDAO;
import com.gis.omp.account.repo.SysUserRoleDAO;
import com.gis.omp.account.service.helper.ModelMapperHelper;
import com.gis.omp.account.service.helper.ServiceHelper;
import com.gis.omp.account.service.iface.SysDeptService;
import com.gis.omp.account.service.iface.SysMenuService;
import com.gis.omp.account.service.iface.SysUserService;
import com.gis.omp.common.api.ResultCode;
import com.gis.omp.common.error.ServiceException;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

////import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户管理Service实现类
 * @author Hongyu Jiang
 * @since  Apr. 27 2020
 * @modify leexiao 2021.06.08
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

	private static ILogger logger = SLoggerFactory.getLogger(SysDeptServiceImpl.class);//private static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

	private final ModelMapper modelMapper;
	private final ModelMapperHelper<SysUserRoleRelation, SysUserRoleDto> userRoleMapper = new ModelMapperHelper<>(SysUserRoleRelation.class, SysUserRoleDto.class);

	private final PasswordEncoder passwordEncoder;
	private final ServiceHelper serviceHelper;

	private final SysUserDAO sysUserDAO;
	private final SysUserRoleDAO sysUserRoleDAO;
	private final SysDeptService sysDeptService;
	private final SysRoleDAO sysRoleDAO;
	private final SysMenuService sysMenuService;

	// 创建用户
	@Override
	public int createUser(CreateUserRequest record) {
        // 检测是否重名，不允许重名
		if (getByName(record.getUsername()) != null) {
			throw new ServiceException(ResultCode.USER_EXIST);
		}

		// 对密码进行加密
		String pwHash = passwordEncoder.encode(record.getPassword());
		record.setPassword(pwHash);

		SysUser sysUser = SysUser.builder()
				.username(record.getUsername())
				.nickName(record.getNickName())
				.password(pwHash)
				.sex(record.getSex())
				.deptId(record.getDeptId())
				.email(record.getEmail())
				.mobile(record.getMobile())
				.status(StatusEnum.OK.getCode())
				.build();
		sysUser.setCreateTimeAndUser("user");

		// 写入用户数据
		try {
			sysUserDAO.save(sysUser);
		} catch (Exception ex) {
			String errMsg = "Could not create user account";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}

		return 0;
	}

	// 更新用户, todo 指定字段
	@Transactional
	@Override
	public int updateUser(SysUserDto record) {
		// 检测是否重名，不允许重名
		if (getByName(record.getUsername()) != null) {
			throw new ServiceException(ResultCode.USER_EXIST);
		}

		// 对密码进行加密
		String pwHash = passwordEncoder.encode(record.getPassword());
		record.setPassword(pwHash);

		// 修改更新时间
		SysUser sysUser = this.convertToModel(record);
		sysUser.setUpdateTimeAndUser("user");

		// 写入用户数据
		try {
			sysUserDAO.save(sysUser);
		} catch (Exception ex) {
			String errMsg = "Could not update user account";
			//serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}

		return 0;
	}

	// 更新用户角色信息
	@Transactional
	int updateUserRoleInfo(long userId, List<SysUserRoleDto> userRoleDtos, String userName) {
		try {
			// 删除用户之前的用户角色数据
			List<SysUserRoleRelation> originUrs = sysUserRoleDAO.findByUserId(userId);
			if (originUrs != null && originUrs.size() != 0) {
				sysUserRoleDAO.deleteInBatch(originUrs);
			}

			// 写入最新用户角色关联数据
			List<SysUserRoleRelation> userRoles = userRoleMapper.convertToModelBatch(userRoleDtos);
			if (userRoles != null && userRoles.size() != 0) {
				for (SysUserRoleRelation userRole : userRoles) {
					SysUserRoleRelation ur = sysUserRoleDAO.
							findByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());
					if (ur == null) {
						// 新增-设置初次创建信息
						userRole.setCreateTimeAndUser(userName);
					}
					userRole.setUpdateTimeAndUser(userName);
				}
				sysUserRoleDAO.saveAll(userRoles);
			}
		} catch (Exception ex) {
			String errMsg = "update user role info failed";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}

		return 0;
	}

	// 创建/更新用户
    @Transactional
    @Override
    public int save(SysUserDto record) {
        if (getById(record.getId()) == null) {
            // create
            CreateUserRequest request = CreateUserRequest.builder()
                    .username(record.getUsername())
                    .nickName(record.getNickName())
                    .password(record.getPassword())
                    .sex(record.getSex())
                    .deptId(record.getDeptId())
                    .email(record.getEmail())
                    .mobile(record.getMobile())
                    .build();
            return createUser(request);
        } else {
            updateUser(record);
            // 更新用户角色信息
            return updateUserRoleInfo(record.getId(), record.getUserRoles(), "user");
        }
    }

    // 删除用户
    @Transactional
    @Override
    public int delete(SysUserDto record) {
		try {
			// 删除用户角色关联信息
			sysUserRoleDAO.deleteByUserId(record.getId());

			// 删除用户信息
			sysUserDAO.delete(this.convertToModel(record));
		} catch (Exception ex) {
            String errMsg = "Could not remove user account";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
		}
        return 0;
    }

    // 批量删除用户
    @Transactional
    @Override
    public int delete(List<SysUserDto> records) {
		try {
			// 删除用户角色关联信息
			for (SysUserDto record : records) {
				sysUserRoleDAO.deleteByUserId(record.getId());
			}

			// 删除用户信息
			sysUserDAO.deleteInBatch(this.convertToModelBatch(records));
		} catch (Exception ex) {
            String errMsg = "Could not remove user account list";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
		}

        return 0;
    }

	// 根据姓名删除用户,目前的情况是姓名唯一
	@Override
	public int deleteUserByName(String userName) {
		SysUserDto userDto = getByName(userName);
		return delete(userDto);
	}

    // 根据姓名查找用户,姓名唯一
    @Override
    public SysUserDto getByName(String userName) {
        SysUser user = sysUserDAO.findByUsername(userName);
        if (user == null) {
            return null;
        } else {
            SysUserDto userDto = this.convertToDto(user);
			fillUserDeptAndRoleInfo(userDto); // 填写额外信息

            return userDto;
        }
    }

    //根据ID查找
    @Override
    public SysUserDto getById(Long id) {
        Optional<SysUser> optional = sysUserDAO.findById(id);
        if(optional != null && optional.isPresent()) {
            return this.convertToDto(optional.get());
        }
        return null;
    }

    @Override
    public List<SysUserDto> listAll() {
	    try {
            return this.convertToDtoBatch(sysUserDAO.findAll());
        } catch (Exception ex) {
            String errMsg = "Could not list user account";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

	@Override
	public List<SysUserDto> save(List<SysUserDto> userList) {
        try {
            List<SysUser> userMoList = sysUserDAO.saveAll(this.convertToModelBatch(userList));
            return convertToDtoBatch(userMoList);
        } catch (Exception ex) {
            String errMsg = "Could not save user account list";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
	}

	@Override
	public Set<String> getPermissions(String userName) {
		Set<String> perms = new HashSet<>();
		List<SysMenuDto> sysMenus = sysMenuService.findByUserName(userName);
		for (SysMenuDto sysMenu : sysMenus) {
			if (sysMenu.getPerms() != null && !"".equals(sysMenu.getPerms())) {
				perms.add(sysMenu.getPerms());
			}
		}
		return perms;
	}

	@Override
	public List<SysUserRoleDto> findUserRoles(Long userId) {
		return userRoleMapper.convertToDtoBatch(sysUserRoleDAO.findByUserId(userId));
	}

	@Override
	public Boolean duplicateByUserName(SysUserDto userDO) {
		Long id = userDO.getId() != null ? userDO.getId() : Long.MIN_VALUE;
        try {
            return sysUserDAO.findByUsernameAndIdNot(userDO.getUsername(), id) != null;
        } catch (Exception ex) {
            String errMsg = "Could not identify duplicate user";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
	}

	@Transactional
	@Override
	public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
		// 联级删除与角色之间的关联
		if (statusEnum == StatusEnum.DELETE) {
			return sysUserDAO.deleteByIdIn(idList) > 0;
		}
		return sysUserDAO.updateStatus(statusEnum.getCode(), idList) > 0;
	}

	@Override
	public Long countUserGross() {
	    try {
            return sysUserDAO.countUserGross();
        } catch (Exception ex) {
            String errMsg = "Could not count user number";
            //serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
	}

	@Override
	@Transactional
	public PageVO<SysUserDto> getPageList(PageRequestVO pageRequestVO) {
		PageRequest pageRequest = null;

		int pageNum = pageRequestVO.getPageNum();
		int pageSize = pageRequestVO.getPageSize();

		/*使用方法如此
		Object name = pageRequestVO.getParamValue("name");
		Object email = pageRequestVO.getParamValue("email");
		if (name != null) {
			if (email != null) {}
		} else {

		}*/

		pageRequest = PageRequest.of(pageNum-1, pageSize);

		Page<SysUser> page = null;
		String username = pageRequestVO.getParamValue("username");
		String nickName = pageRequestVO.getParamValue("nickName");
		String status = pageRequestVO.getParamValue("status");
		String sex = pageRequestVO.getParamValue("sex");

		try {
			if (username == null &&
				nickName == null &&
				status == null &&
				sex == null) {
				page = sysUserDAO.findAll(pageRequest);
			} else {
				// JPA复杂查询（单表、多条件）
				page = sysUserDAO.findAll(new Specification<SysUser>() {
					@Override
					public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
						List<Predicate> predicates = new ArrayList<>();
						if (username != null && username.length() != 0) {
							predicates.add(criteriaBuilder.like(root.get("username").as(String.class), "%"+username+"%"));
						}
						if (nickName != null && nickName.length() != 0) {
							predicates.add(criteriaBuilder.like(root.get("nickName").as(String.class), "%"+nickName+"%"));
						}
						if (status != null && status.length() != 0) {
							predicates.add(criteriaBuilder.equal(root.get("status").as(Byte.class), status));
						}
						if (sex != null && sex.length() != 0) {
							predicates.add(criteriaBuilder.equal(root.get("sex").as(Byte.class), sex));
						}
						Predicate[] pre = new Predicate[predicates.size()];
						query.where(predicates.toArray(pre));
						return criteriaBuilder.and(predicates.toArray(pre));

						// TODO.. 后续可再增加部门筛选，参Timo
					}
				}, pageRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SysUserDto> content = convertToDtoBatch(page.getContent());
		// 用户信息填充
		for (SysUserDto item : content) {
			fillUserDeptAndRoleInfo(item);
		}

		PageVO<SysUserDto> userPageVO = new PageVO<>();
		userPageVO.setList(content);
		userPageVO.setPageNum(page.getNumber()+1);
		userPageVO.setPageSize(page.getSize());
		userPageVO.setTotalPage(page.getTotalPages());
		userPageVO.setTotalRow(page.getTotalElements());

		return userPageVO;
	}

	/**
	 * 组织用户对应的角色名信息
	 * //@param urs 用户对应的用户角色对象集合
	 * @return 用户对应的角色名信息
	 */
	private String buildRoleNames(List<SysUserRoleRelation> urs) {
		StringBuilder sb = new StringBuilder();
		List<Long> roleIds = new ArrayList<>();
		if (urs != null && urs.size() != 0) {
			for (SysUserRoleRelation ur : urs) {
				roleIds.add(ur.getRoleId());
			}
		}
		if (roleIds != null && roleIds.size() != 0) {
			List<SysRole> roleList = sysRoleDAO.findByIdIn(roleIds);
			if (roleList != null && roleList.size() != 0) {
				for (SysRole role : roleList) {
					sb.append(role.getRemark());
					sb.append("，");
				}
				sb.deleteCharAt(sb.lastIndexOf("，"));
				return sb.toString();
			}
		}
		return null;
	}

	// 填充用户信息，主要是为了前端拿到描述信息直接展示
	private int fillUserDeptAndRoleInfo(SysUserDto userDto) {
		// 填充机构中文名
		SysDeptDto dept = sysDeptService.getById(userDto.getDeptId());
		if (dept != null) {
			userDto.setDeptName(dept.getName());
		}

		// 填充用户角色名集合
		List<SysUserRoleRelation> urs = sysUserRoleDAO.findByUserId(userDto.getId());
		if (urs != null && urs.size() != 0) {
			userDto.setUserRoles(userRoleMapper.convertToDtoBatch(urs));  // 填充用户对应包含的用户角色信息

			String roleNames = buildRoleNames(urs);
			if (roleNames != null && !roleNames.equals("")) {
				userDto.setRoleNames(roleNames); // 填充用户包含的角色名称字符串
			}
		}

		return 0;
	}

	// mo dto transfer
	private SysUserDto convertToDto(SysUser account) {
		if (account == null) return null;
		return modelMapper.map(account, SysUserDto.class);
	}

	private SysUser convertToModel(SysUserDto accountDto) {
		if (accountDto == null) return null;
		return modelMapper.map(accountDto, SysUser.class);
	}

    private List<SysUser> convertToModelBatch(List<SysUserDto> userDtoList) {
	    List<SysUser> userList = new ArrayList<>();
	    for (SysUserDto userDto : userDtoList) {
	        userList.add(convertToModel(userDto));
        }

	    return userList;
    }

    private List<SysUserDto> convertToDtoBatch(List<SysUser> userList) {
        List<SysUserDto> userDtoList = new ArrayList<>();
        for (SysUser user : userList) {
            userDtoList.add(convertToDto(user));
        }

        return userDtoList;
    }

    // user role relation ,
	private SysUserRoleDto convertToDto(SysUserRoleRelation userRoleR) {
		if (userRoleR == null) return null;
		return modelMapper.map(userRoleR, SysUserRoleDto.class);
	}
}
