package com.ycc.diancan.definition.spider;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

/**
 * JianShiShu.
 *
 * @author ycc
 * @date 2023-12-21 2023/12/21
 */
@Data
@TableName("jian_shi_shu")
@Table(name = "jian_shi_shu")
public class JianShiShu extends BaseSpiderEntity {
	@Column(name = "jian_shi_shu_type", comment = "小说类别", type = "varchar", length = 50)
	private String jianShiShuType;
}
