/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition.spider;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

/**
 * ShuHuangBuLuo(书荒部落).
 *
 * @author ycc
 * @date 2023-11-30 16:32:21
 */
@Data
@TableName("shu_huang_bu_luo")
@Table(name = "shu_huang_bu_luo")
public class ShuHuangBuLuo  extends BaseSpiderEntity {
	@Column(name = "un_zip_password", comment = "解压密码")
	private String unZipPassword;
	@Column(name = "shu_huang_bu_luo_novel_type", comment = "小说类别", type = "varchar", length = 50)
	private String shuHuangBuLuoNovelType;
}
