package com.gis.omp.account.service.impl;


import com.gis.omp.account.dto.modelDto.SysDeptDto;
import com.gis.omp.account.dto.paging.PageRequestVO;
import com.gis.omp.account.dto.paging.PageVO;
import com.gis.omp.account.model.SysDept;
import com.gis.omp.account.repo.SysDeptDAO;
import com.gis.omp.account.service.helper.ServiceHelper;
import com.gis.omp.account.service.iface.SysDeptService;
import com.gis.omp.common.error.ServiceException;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 机构管理Service实现类
 * @author Hongyu Jiang
 * @since  Apr. 27 2020
 * @modify leexiao 2021.06.08
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl implements SysDeptService {

	private static ILogger logger = SLoggerFactory.getLogger(SysDeptServiceImpl.class);//private static Logger logger = LoggerFactory.getLogger(SysDeptServiceImpl.class);

	private final SysDeptDAO sysDeptDAO;
	private final ModelMapper modelMapper;
	private final ServiceHelper serviceHelper;

	@Override
	public int save(SysDeptDto record) {
		try {
			String userName = "user";// SecurityContextHolder.getContext().getAuthentication().getName();
			Example<SysDeptDto> example = Example.of(record);

			Long curId = record.getId();
			SysDeptDto dept = this.getById(curId);
			if (dept == null) {
				// 新增
//				Date curDate = new Date();
//				record.setCreateTime(curDate);
//				record.setLastUpdateTime(curDate);
//				record.setCreateBy(userName);
//				record.setLastUpdateBy(userName);
			} else {
				// 编辑更新
				record.setLastUpdateBy(userName);
				////record.setLastUpdateTime(new Date());
			}

			//sysDeptDAO.save(record);
			return 0;
		} catch (Exception e) {
			logger.error("save SysDeptDO error, {}", e.getMessage());
			e.printStackTrace();
			return -1;
		}
		//return 0;
	}

	@Override
	public int delete(SysDeptDto record) {
		try {
			sysDeptDAO.delete(convertToModel(record));
			return 0;
		} catch (Exception ex) {
			String errMsg = "Could not delete dept";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}
	}

	@Override
	public int delete(List<SysDeptDto> records) {
		try {
			sysDeptDAO.deleteInBatch(convertToModelBatch(records));
		} catch (Exception ex) {
			String errMsg = "Could not delete dept list";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}

		return 0;
	}

	@Override
	public SysDeptDto getById(Long id) {
		Optional<SysDept> optional = sysDeptDAO.findById(id);
		if(optional != null && optional.isPresent()) {
			return convertToDto(optional.get());
		}
		return null;
	}

	@Override
	public PageVO<SysDeptDto> getPageList(PageRequestVO pageRequestVO) {
		try {
			PageRequest pageRequest = null;

			int pageNum = pageRequestVO.getPageNum();
			int pageSize = pageRequestVO.getPageSize();

			pageRequest = PageRequest.of(pageNum-1, pageSize);

			Page<SysDept> page = null;
			String name = pageRequestVO.getParamValue("name");
			String address = pageRequestVO.getParamValue("address");
			String contact = pageRequestVO.getParamValue("contact");

			try {
				if (name == null &&
					address == null &&
					contact == null){
					page = sysDeptDAO.findAll(pageRequest);
				} else {
					// JPA复杂查询（单表、多条件）
					page = sysDeptDAO.findAll(new Specification<SysDept>() {
						@Override
						public Predicate toPredicate(Root<SysDept> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
							List<Predicate> predicates = new ArrayList<>();
							if (name != null && name.length() != 0) {
								predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%"+name+"%"));
							}
							if (address != null && address.length() != 0) {
								predicates.add(criteriaBuilder.like(root.get("address").as(String.class), "%"+address+"%"));
							}
							if (contact != null && contact.length() != 0) {
								predicates.add(criteriaBuilder.like(root.get("contact").as(String.class), "%"+contact+"%"));
							}
							Predicate[] pre = new Predicate[predicates.size()];
							query.where(predicates.toArray(pre));
							return criteriaBuilder.and(predicates.toArray(pre));
						}
					}, pageRequest);
				}
				sysDeptDAO.findAll(pageRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<SysDept> content = page.getContent();
			PageVO<SysDeptDto> deptPageVO = new PageVO<>();
			deptPageVO.setList(convertToDtoBatch(content));
			deptPageVO.setPageNum(page.getNumber()+1);
			deptPageVO.setPageSize(page.getSize());
			deptPageVO.setTotalPage(page.getTotalPages());
			deptPageVO.setTotalRow(page.getTotalElements());

			return deptPageVO;
		} catch (Exception ex) {
			String errMsg = "get SysDeptDO page list error";
			serviceHelper.handleException(logger, ex, errMsg);
			throw new ServiceException(errMsg, ex);
		}
	}

	@Override
	public List<SysDeptDto> findTree() {
		List<SysDeptDto> sysDepts = new ArrayList<>();
		List<SysDeptDto> depts = convertToDtoBatch(sysDeptDAO.findAll());
		// 遍历所有根机构
		for (SysDeptDto dept : depts) {
			if (dept.getParentId() == null || dept.getParentId() == 0) {
				dept.setLevel(0);
				sysDepts.add(dept);
			}
		}
		// 查找子机构
		findChildren(sysDepts, depts);
		return sysDepts;
	}

	/**
	 * 递归查询组织子机构
	 * @param sysDepts 所有根机构信息
	 * @param depts 所有机构信息
	 */
	private void findChildren(List<SysDeptDto> sysDepts, List<SysDeptDto> depts) {
		for (SysDeptDto sysDept : sysDepts) {
			List<SysDeptDto> children = new ArrayList<>();
			for (SysDeptDto dept : depts) {
				if (sysDept.getId() != null && sysDept.getId().equals(dept.getParentId())) {
					dept.setParentName(sysDept.getName());
					dept.setLevel(sysDept.getLevel() + 1);
					children.add(dept);
				}
			}
			sysDept.setChildren(children);
			findChildren(children, depts);
		}
	}


	@Override
	public Page<SysDeptDto> listSysDept(Integer pageNum, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);

		return null;////return sysDeptDAO.findAll(pageRequest);
	}

	@Override
	public List<SysDeptDto> findTreeByName(String name) {
		List<SysDeptDto> sysDepts = new ArrayList<>();
		List<SysDeptDto> depts = convertToDtoBatch(sysDeptDAO.listByDeptName(name));
		// 遍历所有根机构
		for (SysDeptDto dept : depts) {
			if (dept.getParentId() == null || dept.getParentId() == 0) {
				dept.setLevel(0);
				sysDepts.add(dept);
			}
		}
		// 查找子机构
		List<SysDeptDto> wholeDepts = convertToDtoBatch(sysDeptDAO.findAll());
		findChildren(sysDepts, wholeDepts);
		return sysDepts;
	}


	// mo dto transfer
	private SysDeptDto convertToDto(SysDept dept) {
		if (dept == null) return null;
		return modelMapper.map(dept, SysDeptDto.class);
	}

	private SysDept convertToModel(SysDeptDto deptDto) {
		if (deptDto == null) return null;
		return modelMapper.map(deptDto, SysDept.class);
	}

	private List<SysDept> convertToModelBatch(List<SysDeptDto> deptDtoList) {
		List<SysDept> deptList = new ArrayList<>();
		for (SysDeptDto deptDto : deptDtoList) {
			deptList.add(convertToModel(deptDto));
		}

		return deptList;
	}

	private List<SysDeptDto> convertToDtoBatch(List<SysDept> roleList) {
		List<SysDeptDto> roleDtoList = new ArrayList<>();
		for (SysDept role : roleList) {
			roleDtoList.add(convertToDto(role));
		}

		return roleDtoList;
	}
}
