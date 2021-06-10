package com.gis.omp.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseModel {
    /*@Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;  // primary*/
    @Id                                // 标识该属性为主键
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;				// 编号

    private String createBy;		// 创建人

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String lastUpdateBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    public void setCreateTimeAndUser(String userName) {
        createBy = userName;
        lastUpdateBy = userName;
        Date curDate = new Date();
        createTime = curDate;
        lastUpdateTime = curDate;
    }

    public void setUpdateTimeAndUser(String userName) {
        lastUpdateBy = userName;
        Date curDate = new Date();
        lastUpdateTime = curDate;
    }
}
