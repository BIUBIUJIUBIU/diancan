package com.ycc.diancan.definition.spider;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

/**
 * LongTeng.
 * @author ycc
 * @date 2023-12-25 2023/12/25
 */
@Data
@TableName("long_teng")
@Table(name = "long_teng")
public class LongTeng  extends BaseSpiderEntity {
	@Column(name = "long_teng_type", comment = "小说类别", type = "varchar", length = 50)
	private String longTengType;
}
