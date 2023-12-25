package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.spider.LongTeng;

import java.util.List;

/**
 * LongTengService.
 *
 * @author ycc
 * @date 2023-12-25 2023/12/25
 */
public interface LongTengService extends IService<LongTeng>, SpiderService {

	List<LongTeng> searchByTitle(String title);

	List<LongTeng> searchByAuthor(String author);
}
