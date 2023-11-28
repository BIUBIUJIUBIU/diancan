package com.ycc.diancan.definition;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")// 设置表名
public class User extends BaseEntity {
	// @Column(comment = "昵称")
	private String nickName;
	// @Column(comment = "年龄")
	private int age;
	// @Column(comment = "邮箱")
	private String email;
	// @Column(comment = "头像")
	private String avatar;
	// @Column(comment = "用户名")
	private String username;
	// @Column(comment = "密码")
	private String password;
	// @Column(comment = "家庭地址")
	private String address;
}

