package com.ycc.diancan.definition;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@TableName("user")
@Table(name = "user")
public class User extends BaseEntity {
	@Column(name = "nick_name", comment = "昵称")
	private String nickName;
	@Column(name = "age", comment = "年龄")
	private int age;
	@Column(name = "email", comment = "邮箱")
	private String email;
	@Column(name = "avatar", comment = "头像")
	private String avatar;
	@Column(name = "username", comment = "用户名")
	private String username;
	@Column(name = "password", comment = "密码")
	private String password;
	@Column(name = "address", comment = "家庭地址")
	private String address;
}

