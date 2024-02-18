package com.ycc.diancan.definition.spider;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

/**
 * BiQu52.
 *
 * @author ycc
 * @date 2023-12-22 2023/12/22
 */
@Data
@TableName("Bi_Qu_52")
@Table(name = "Bi_Qu_52")
public class BiQu52 extends BaseSpiderEntity {
	@Column(name = "book_list_type", comment = "书单类别", type = "varchar")
	private String bookListType;
	@Column(name = "synopsis", comment = "内容摘要", type = "mediumtext")
	private String synopsis;
	@Column(name = "mp3_download_url", comment = "小说MP3下载地址", length = 200)
	private String mp3DownloadUrl;

}
