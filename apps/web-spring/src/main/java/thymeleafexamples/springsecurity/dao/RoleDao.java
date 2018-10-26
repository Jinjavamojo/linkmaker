package thymeleafexamples.springsecurity.dao;


import thymeleafexamples.springsecurity.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
